package com.ift604.udes.myspot.Entites.GmapGIS;

import java.util.ArrayList;

/**
 * Created by Squirrel on 2017-11-22.
 */

public class Geometry {

    private String type;

    public String getType() { return this.type; }

    public void setType(String type) { this.type = type; }

    private ArrayList<ArrayList<ArrayList<Double>>> coordinates;

    public ArrayList<ArrayList<ArrayList<Double>>> getCoordinates() { return this.coordinates; }

    public void setCoordinates(ArrayList<ArrayList<ArrayList<Double>>> coordinates) { this.coordinates = coordinates; }
}
