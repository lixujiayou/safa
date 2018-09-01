package com.inspur.hebeiline.entity;

public class ACodeBean {


    /**
     * cityUUID : 32
     * countyUUID : 151
     * cityName : 石家庄
     * countyName : 裕华区
     */

    private String cityUUID;
    private String countyUUID;
    private String cityName;
    private String countyName;

    @Override
    public String toString() {
        return "ACodeBean{" +
                "cityUUID='" + cityUUID + '\'' +
                ", countyUUID='" + countyUUID + '\'' +
                ", cityName='" + cityName + '\'' +
                ", countyName='" + countyName + '\'' +
                '}';
    }

    public String getCityUUID() {
        return cityUUID;
    }

    public void setCityUUID(String cityUUID) {
        this.cityUUID = cityUUID;
    }

    public String getCountyUUID() {
        return countyUUID;
    }

    public void setCountyUUID(String countyUUID) {
        this.countyUUID = countyUUID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
}
