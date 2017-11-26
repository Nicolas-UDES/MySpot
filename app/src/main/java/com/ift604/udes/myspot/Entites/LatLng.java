package com.ift604.udes.myspot.Entites;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Squirrel on 2017-11-26.
 */

public class LatLng implements Serializable {

    public final static int PRECISION = 100000;

    private long id;

    private double latitude;

    private double longitude;

    public LatLng() {
    }

    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double distance(LatLng latLng) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(latLng.getLatitude() - getLatitude());
        double lngDiff = Math.toRadians(latLng.getLongitude() - getLongitude());
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(getLatitude())) * Math.cos(Math.toRadians(latLng.getLatitude())) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return distance * meterConversion;
    }

    public boolean isPointInPolygon(List<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }

    private boolean rayCastIntersect(LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = latitude;
        double pX = longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LatLng)) {
            return false;
        }

        return equals((LatLng)obj);
    }

    public boolean equals(LatLng latLng) {
        return (int)(getLatitude() * PRECISION) == (int)(latLng.getLatitude() * PRECISION) &&
                (int)(getLongitude() * PRECISION) == (int)(latLng.getLongitude() * PRECISION);
    }

    @Override
    public int hashCode() {
        return new Integer((int) (getLatitude() * PRECISION)).hashCode() ^
                new Integer((int) (getLongitude() * PRECISION)).hashCode();
    }

    public final com.google.android.gms.maps.model.LatLng toGoogle()
    {
        return new com.google.android.gms.maps.model.LatLng(getLatitude(), getLongitude());
    }

    public static final LatLng fromGoogle(final com.google.android.gms.maps.model.LatLng latLng)
    {
        return new LatLng(latLng.latitude, latLng.longitude);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
}
