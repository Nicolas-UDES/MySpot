package com.ift604.udes.myspot.Android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ift604.udes.myspot.DAO.PlayerDAO;
import com.ift604.udes.myspot.DAO.TerritoryDAO;
import com.ift604.udes.myspot.Entites.Enumerable.TerritoryType;
import com.ift604.udes.myspot.Entites.LatLng;
import com.ift604.udes.myspot.Entites.Player;
import com.ift604.udes.myspot.Entites.ServerId;
import com.ift604.udes.myspot.Entites.Territory;
import com.ift604.udes.myspot.R;
import com.ift604.udes.myspot.Utility.Delayer;

import java.lang.reflect.Type;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TerritoryDAO.OnGetTerritories, PlayerDAO.OnCreatePlayer {

    private static final int MY_LOCATION_REQUEST_CODE = 245;
    private static final String SAVED_TERRITORIES_KEY = "SAVED_TERRITORIES";
    private static final double UNIVERSITY_LAT = 45.38072;
    private static final double UNIVERSITY_LNG = -71.92613;
    private static final int UNIVERSITY_ZOOM = 15;

    private HashMap<Territory, PolygonOptions> territories;
    private GoogleMap myMap;

    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.savedInstanceState = savedInstanceState;

        setButtons();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getServerId() {
        List<ServerId> serverIds = ServerId.listAll(ServerId.class);
        if(serverIds.size() == 0) {
            PlayerDAO.createPlayer(this, getApplicationContext(), "");
        }
    }

    private void setButtons() {
        final Button meButton = (Button) findViewById(R.id.me_button);
        meButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                meButtonClick(v);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        setCamera();
        askForFineLocation();
        if(territories != null){
            showTerritories();
        }
        else {
            fetchNearTerritories();
        }
    }

    private void setCamera() {
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(new LatLng(UNIVERSITY_LAT, UNIVERSITY_LNG).toGoogle(), UNIVERSITY_ZOOM);
        myMap.moveCamera(center);
    }

    private boolean askForFineLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
            return true;
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length > 0 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    myMap.setMyLocationEnabled(true);

                    setCamera();
                    fetchNearTerritories();
                } catch (SecurityException e) {
                    // Permission was denied. Display an error message.
                }
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    private void fetchNearTerritories() {
        if(savedInstanceState != null) {
            territories = (HashMap<Territory, PolygonOptions>) savedInstanceState.getSerializable(SAVED_TERRITORIES_KEY);
            showTerritories();
            return;
        }

        TerritoryDAO.getTerritories(this, getApplicationContext());
    }

    private void showTerritories() {
        for(PolygonOptions territory : territories.values()){
            myMap.addPolygon(territory);
        }
    }

    private PolygonOptions getPolygonOptions(Territory territory) {
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(territory.getGooglePositions());
        polygonOptions.strokeColor(Color.BLACK);
        polygonOptions.strokeWidth(5.0f);
        polygonOptions.fillColor(getColor(territory.getTerritoryType()));

        return polygonOptions;
    }

    private int getColor(TerritoryType type){
        final int alpha = 30;
        switch (type){
            case Water:
                return Color.argb(alpha, 0, 0, 200);
            case Gainable:
                return Color.argb(alpha, 0, 200, 0);
            default:
                return Color.argb(alpha, 200, 0, 0);
        }
    }

    public void meButtonClick(View view) {
        Intent intent = new Intent(this, MeActivity.class);
        startActivity(intent);
    }

    @Override
    public void getTerritories(List<Territory> territories) {
        this.territories = new HashMap<>();
        for(Territory territory : territories){
            PolygonOptions polygon = getPolygonOptions(territory);
            myMap.addPolygon(polygon);
            this.territories.put(territory, polygon);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVED_TERRITORIES_KEY, this.territories);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void errorOnGetTerritories(VolleyError error) {
        NetworkResponse response = error.networkResponse;
        if(response != null && response.data != null){
            Log.e("tag","errorMessage:"+response.statusCode);
        }else{
            String errorMessage=error.getClass().getSimpleName();
            if(!TextUtils.isEmpty(errorMessage)){
                Log.e("tag","errorMessage:"+errorMessage);
            }
        }
    }

    @Override
    public void onCreatePlayer(Player player) {
        new ServerId(player.getId()).save();
    }

    @Override
    public void errorOnGetPlayer(VolleyError error) {

    }
}
