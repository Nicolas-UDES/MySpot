package com.ift604.udes.myspot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ift604.udes.myspot.Entites.Territory;
import com.ift604.udes.myspot.Utility.Delayer;

import java.lang.reflect.Type;
import java.util.List;

import static java.security.AccessController.getContext;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_LOCATION_REQUEST_CODE = 245;
    private GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        if(askForFineLocation()) {
            Delayer.delay(1000, new Runnable() {
                @Override
                public void run() {
                    setCamera();
                    fetchNearTerritories();
                }
            });
        }
    }

    private void setCamera() {
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(45.38072, -71.92613));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        myMap.moveCamera(center);
        myMap.animateCamera(zoom);
    }

    private boolean askForFineLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
            return true;
        } else {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
            }

        }

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

    private void showTerritories(List<Territory> territories) {
        for(Territory territory : territories){
            PolygonOptions polygonOptions = new PolygonOptions();
            polygonOptions.addAll(territory.getPositions());
            polygonOptions.strokeColor(Color.BLACK);
            polygonOptions.strokeWidth(5.0f);
            polygonOptions.fillColor(Color.argb(30, 0, 200, 0));

            Polygon polygon = myMap.addPolygon(polygonOptions);
        }
    }

    private void gettingNearTerritoriesError(VolleyError error) {

    }
}
