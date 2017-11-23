package com.ift604.udes.myspot.Entites.GmapGIS;

import java.util.ArrayList;

/**
 * Created by Squirrel on 2017-11-22.
 */

public class GmapGIS {

    private String type;

    public String getType() { return this.type; }

    public void setType(String type) { this.type = type; }

    private String source;

    public String getSource() { return this.source; }

    public void setSource(String source) { this.source = source; }

    private ArrayList<Feature> features;

    public ArrayList<Feature> getFeatures() { return this.features; }

    public void setFeatures(ArrayList<Feature> features) { this.features = features; }
}
