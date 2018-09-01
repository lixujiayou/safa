package com.inspur.hebeiline.entity.tmline;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by lixu on 2018/7/6.
 */
@Entity(indices = {@Index("lineId")} )
public class MyWayBean {

    @PrimaryKey(autoGenerate = true)
    private int mid;

    private String routeId;
    private String lineId;
    private double lat;
    private double lon;
    @Ignore
    private double cHeight;
    private int allLength; //轨迹总距离
    private double matchingRatio;//匹配率
    private int state;//是否巡检成功



    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public double getMatchingRatio() {
        return matchingRatio;
    }

    public void setMatchingRatio(double matchingRatio) {
        this.matchingRatio = matchingRatio;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAllLength() {
        return allLength;
    }

    public void setAllLength(int allLength) {
        this.allLength = allLength;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getcHeight() {
        return cHeight;
    }

    public void setcHeight(double cHeight) {
        this.cHeight = cHeight;
    }
}
