package com.ift604.udes.myspot.Android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.ift604.udes.myspot.DAO.DrinkingDAO;
import com.ift604.udes.myspot.DAO.PlayerDAO;
import com.ift604.udes.myspot.DAO.Server;
import com.ift604.udes.myspot.DAO.TerritoryDAO;
import com.ift604.udes.myspot.Entites.ServerId;
import com.ift604.udes.myspot.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import MySpotLibrary.BLL.PlayerBLL;
import MySpotLibrary.Entites.*;
import MySpotLibrary.Entites.Enumerable.TerritoryType;

import static MySpotLibrary.BLL.GeoPosBLL.isPointInPolygon;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, View.OnTouchListener, TerritoryDAO.OnGetTerritories, PlayerDAO.OnCreatePlayer, DrinkingDAO.OnSendDrinking {

    private static final int MY_LOCATION_REQUEST_CODE = 245;
    private static final String SAVED_TERRITORIES_KEY = "SAVED_TERRITORIES";
    private static final double UNIVERSITY_LAT = 45.38072;
    private static final double UNIVERSITY_LNG = -71.92613;
    private static final int UNIVERSITY_ZOOM = 15;

    private HashMap<Territory, PolygonOptions> territories;
    private String provider;
    private Location firstPosition;
    private Territory currentTerritory;
    private long actionBegining;

    private GoogleMap myMap;
    private MediaPlayer player;
    private LocationManager locationManager;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.savedInstanceState = savedInstanceState;

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        firstPosition = getLastKnownLocation();

        ServerId.clearServerId();
        getServerId();
        setButtons();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        String bestProvider = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                continue;
            }

            Location location = locationManager.getLastKnownLocation(provider);
            if (location == null) {
                continue;
            }
            if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = location;
                bestProvider = provider;
            }
        }

        provider = bestProvider;
        return bestLocation;
    }

    private void getServerId() {
        List<ServerId> serverIds = null;
        try {
            serverIds = ServerId.listAll(ServerId.class);
        } catch (SQLiteException e) {
        }

        if (serverIds == null || serverIds.size() == 0) {
            PlayerDAO.createPlayer(this, getApplicationContext(), "Steinsky");
        }
    }

    private void setButtons() {
        final Button meButton = (Button) findViewById(R.id.me_button);
        meButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                meButtonClick(v);
            }
        });

        final Button peeButton = (Button) findViewById(R.id.pee_button);
        peeButton.setOnTouchListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        setCamera();
        askForFineLocation();
        if (territories != null) {
            showTerritories();
        } else {
            fetchNearTerritories();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || provider == null) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 500, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void loadDataFromAsset(String file, boolean loop) {
        try {
            AssetFileDescriptor afd = getAssets().openFd(file);
            player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.setLooping(loop);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<com.google.android.gms.maps.model.LatLng> getGooglePositions(Iterable<GeoPos> points) {
        List<com.google.android.gms.maps.model.LatLng> result = new ArrayList<>();
        for(GeoPos position : points){
            result.add(getGooglePosition(position));
        }
        return result;
    }

    private com.google.android.gms.maps.model.LatLng getGooglePosition(GeoPos point) {
        return new com.google.android.gms.maps.model.LatLng(point.getLatitude(), point.getLongitude());
    }

    private void setCamera() {
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(getGooglePosition(new GeoPos(UNIVERSITY_LAT, UNIVERSITY_LNG)), UNIVERSITY_ZOOM);
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
        polygonOptions.addAll(getGooglePositions(territory.getPositions()));
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

    public void peeButtonClick(View view) {
        Intent intent = new Intent(this, MeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onGetTerritories(List<Territory> territories) {
        this.territories = new HashMap<>();
        for(Territory territory : territories){
            PolygonOptions polygon = getPolygonOptions(territory);
            myMap.addPolygon(polygon);
            this.territories.put(territory, polygon);
        }

        onLocationChanged(firstPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVED_TERRITORIES_KEY, this.territories);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void errorOnGetTerritories(VolleyError error) {
        Log.e("errorOnGetTerritories", Server.toString(error));
    }

    @Override
    public void onCreatePlayer(Player player) {
        new ServerId(player.getId()).save();
    }

    @Override
    public void errorOnGetPlayer(VolleyError error) {
        Log.e("errorOnGetPlayer", Server.toString(error));
    }

    @Override
    public void onLocationChanged(Location location) {
        final Button markButton = (Button) findViewById(R.id.pee_button);
        boolean found = false;

        if(location != null) {
            GeoPos latLng = new GeoPos(location.getLatitude(), location.getLongitude());

            for (Territory territory : territories.keySet()) {
                if (!isPointInPolygon(latLng, territory.getPositions())) {
                    continue;
                }

                found = true;
                if (territory.getTerritoryType() == TerritoryType.Water) {
                    if (currentTerritory == null || currentTerritory.getTerritoryType() == TerritoryType.Gainable) {
                        setModeDrink(markButton);
                    }
                } else if (currentTerritory == null || currentTerritory.getTerritoryType() == TerritoryType.Water) {
                    setModeMark(markButton);
                }

                currentTerritory = territory;
                break;
            }
        }

        if(!found && currentTerritory != null) {
            markButton.setAlpha(0.0f);
            currentTerritory = null;
        }
    }

    private void setModeDrink(Button button) {
        setMode(button, "Drink", "#b3c3ff", "Drinking.mp3", true);
    }

    private void setModeMark(Button button) {

        setMode(button, "Mark", "#ffffb3", "Male urinating.mp3", false);
    }

    private void setMode(Button button, String text, String color, String audioFile, boolean loopAudio) {
        button.setText(text);
        button.setAlpha(1.0f);
        button.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY);
        loadDataFromAsset(audioFile, loopAudio);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            actionBegining = new Date().getTime();
            player.start();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            long delay = new Date().getTime() - actionBegining;
            player.stop();
            try {
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            applyAction(delay);
        }
        return false;
    }

    private void applyAction(long delay) {
        DrinkingDAO.sendDrinking(this, getApplicationContext(), ServerId.getServerId(), currentTerritory.getId(), delay);
    }

    @Override
    public void onSendDrinking(Drinking drinking) {
        String drinkingText = String.valueOf(drinking.getAmount());

        Context context = getApplicationContext();
        CharSequence text = "You drinked for " + drinkingText.substring(0, Math.min(5, drinkingText.length())) + " ml.";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void errorOnSendDrinking(VolleyError error) {
        Log.e("errorOnSendDrinking", Server.toString(error));
    }
}
