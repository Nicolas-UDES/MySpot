package com.ift604.udes.myspot.Android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ift604.udes.myspot.Entites.Enumerable.TerritoryType;
import com.ift604.udes.myspot.Entites.Territory;
import com.ift604.udes.myspot.R;
import com.ift604.udes.myspot.Utility.Delayer;

import java.lang.reflect.Type;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_LOCATION_REQUEST_CODE = 245;
    private HashMap<Territory, PolygonOptions> territories;
    private GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        setCamera();
        if(territories != null){
            showTerritories();
        }
        else {
            fetchNearTerritories();
        }
    }

    private void setCamera() {
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(new LatLng(45.38072, -71.92613), 15);
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
        String url = "http://192.168.1.100:8080/getTerritories";

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Result handling
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Territory>>(){}.getType();
                List<Territory> territories = gson.fromJson(response, listType);
                showTerritories(territories);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                gettingNearTerritoriesError(error);
            }
        });

        // Add the request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void showTerritories(List<Territory> territoryList) {
        territories = new HashMap<>();
        for(Territory territory : territoryList){
            PolygonOptions polygon = getPolygonOptions(territory);
            myMap.addPolygon(polygon);
            territories.put(territory, polygon);
        }
    }

    private void showTerritories() {
        territories = new HashMap<>();
        for(PolygonOptions territory : territories.values()){
            myMap.addPolygon(territory);
        }
    }

    private PolygonOptions getPolygonOptions(Territory territory) {
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(territory.getPositions());
        polygonOptions.strokeColor(Color.BLACK);
        polygonOptions.strokeWidth(5.0f);
        polygonOptions.fillColor(getColor(territory.getTerritoryType()));

        return polygonOptions;
    }

    private void gettingNearTerritoriesError(VolleyError error) {
        Log.e("gettingNearTerritoriesError", error.getMessage());
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
}
