package com.inspur.hebeiline.entity;

import com.amap.api.maps.model.LatLng;

public class RecordTestBean {
    private double resHeight; //垂直高度
    private LatLng currentLocation;//当前位置


    public double getResHeight() {
        return resHeight;
    }

    public void setResHeight(double resHeight) {
        this.resHeight = resHeight;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }
}
