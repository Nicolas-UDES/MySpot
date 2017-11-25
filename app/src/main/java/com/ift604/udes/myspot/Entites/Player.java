package com.ift604.udes.myspot.Entites;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Squirrel on 2017-11-24.
 */

public class Player {

    private int id;
    private String username;

    private double blader;
    private int urineStrength;

    private LatLng lastPosition;
    private Date lastSeen;

    private List<Territory> controlledTerritories;

    public Player() {
        this(new ArrayList<Territory>());
    }

    public Player(List<Territory> controlledTerritories) {
        this.controlledTerritories = controlledTerritories;
    }

    public Player(int id, String username, double blader, int urineStrength, LatLng lastPosition, Date lastSeen, List<Territory> controlledTerritories) {
        this(controlledTerritories);
        this.id = id;
        this.username = username;
        this.blader = blader;
        this.urineStrength = urineStrength;
        this.lastPosition = lastPosition;
        this.lastSeen = lastSeen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBlader() {
        return blader;
    }

    public void setBlader(double blader) {
        this.blader = blader;
    }

    public int getUrineStrength() {
        return urineStrength;
    }

    public void setUrineStrength(int urineStrength) {
        this.urineStrength = urineStrength;
    }

    public LatLng getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(LatLng lastPosition) {
        this.lastPosition = lastPosition;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public List<Territory> getControlledTerritories() {
        return controlledTerritories;
    }

    public void setControlledTerritories(List<Territory> controlledTerritories) {
        this.controlledTerritories = controlledTerritories;
    }
}
