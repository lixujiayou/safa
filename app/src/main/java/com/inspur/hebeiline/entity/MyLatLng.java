package com.inspur.hebeiline.entity;

/**
 * Created by lixu on 2018/6/20.
 */

public class MyLatLng {
    private double lat;
    private double lng;

    public MyLatLng() {
    }

    public MyLatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "{" +
                "\"lat\":" + lat +
                ", \"lng\":" + lng +
                '}';
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
