package com.ift604.udes.myspot.Entites;

import com.google.android.gms.maps.model.LatLng;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Squirrel on 2017-11-24.
 */

public class Player {

    private int id;
    private String username;
    private int level;

    private double blader;
    private double urineStrength;
    private double stomach;

    private LatLng lastPosition;
    private Date lastSeen;

    private List<Drinking> drinks;
    private List<Marking> marks;

    public Player() {
        drinks = new ArrayList<>();
        marks = new ArrayList<>();
    }

    public Player(int id, String username, double blader, double urineStrength, double stomach, int level, LatLng lastPosition, Date lastSeen) {
        this();

        this.id = id;
        this.username = username;
        this.blader = blader;
        this.urineStrength = urineStrength;
        this.stomach = stomach;
        this.lastPosition = lastPosition;
        this.lastSeen = lastSeen;
        this.level = level;
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

    public double getUrineStrength() {
        return urineStrength;
    }

    public void setUrineStrength(double urineStrength) {
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

    public double getStomach() {
        return stomach;
    }

    public void setStomach(double stomach) {
        this.stomach = stomach;
    }

    public List<Drinking> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drinking> drinks) {
        this.drinks = drinks;
    }

    public List<Marking> getMarks() {
        return marks;
    }

    public void setMarks(List<Marking> marks) {
        this.marks = marks;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
