package com.ift604.udes.myspot.Entites.GmapGIS;

/**
 * Created by Squirrel on 2017-11-22.
 */

public class Feature {

    private String type;

    public String getType() { return this.type; }

    public void setType(String type) { this.type = type; }

    private Properties properties;

    public Properties getProperties() { return this.properties; }

    public void setProperties(Properties properties) { this.properties = properties; }

    private Geometry geometry;

    public Geometry getGeometry() { return this.geometry; }

    public void setGeometry(Geometry geometry) { this.geometry = geometry; }
}
