package com.ift604.udes.myspot.Entites;

import com.google.android.gms.maps.model.LatLng;
import com.ift604.udes.myspot.Entites.Enumerable.TerritoryType;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Squirrel on 2017-11-20.
 */

public class Territory implements Serializable {

    private long id;

    private List<LatLng> positions;

    private TerritoryType territoryType;

    private LatLng center;

    private String name;

    private List<Mark> marks;

    public Territory() {
        this(new ArrayList<LatLng>());
    }

    public Territory(List<LatLng> positions) {
        this.positions = positions;
        marks = new ArrayList<>();
    }

    public Territory(long id, TerritoryType territoryType, List<LatLng> positions) {
        this(positions);
        this.id = id;
        this.territoryType = territoryType;
    }

    public void addAll(LatLng ... latLngs){
        Collections.addAll(positions, latLngs);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<LatLng> getPositions() {
        return positions;
    }

    public void setPositions(List<LatLng> positions) {
        this.positions = positions;
    }

    public TerritoryType getTerritoryType() {
        return territoryType;
    }

    public void setTerritoryType(TerritoryType territoryType) {
        this.territoryType = territoryType;
    }

    public LatLng getCenter() {
        return center;
    }

    public void setCenter(LatLng center) {
        this.center = center;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }
}
