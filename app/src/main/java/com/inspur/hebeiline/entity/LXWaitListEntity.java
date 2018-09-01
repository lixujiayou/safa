package com.inspur.hebeiline.entity;

import java.io.Serializable;

/**
 * Created by lixu on 2018/6/21.
 */

public class LXWaitListEntity implements Serializable{

    /**
     * name : 一干一号线
     * startDate : 2018-06-13 13:34:52
     * endDate : 2018-06-14 13:34:56
     * taskStatus : 一年两次
     * resLevel : 1
     * routeLenght : “100”
     * doneRate : “90%”
     * doneLenght : “90”
     * undoLenght : “10”
     * totalTime : “250”
     */

    private String name;
    private String startDate;
    private String endDate;
    private String taskStatus;
    private String resLevel;
    private String routeLenght;
    private String doneRate;
    private String doneLenght;
    private String undoLenght;
    private String totalTime;

    private String taskUUID;

    private String uuid;
    private Object resType;
    private String aPoint;
    private String zPoint;
    private String aPointLng;
    private String aPointLat;
    private String zPointLng;
    private String zPointLat;
    private String status;
    private String routeUUID;
    private String routeName;

    //1：title  0：other
    private String myType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getResLevel() {
        return resLevel;
    }

    public void setResLevel(String resLevel) {
        this.resLevel = resLevel;
    }

    public String getRouteLenght() {
        return routeLenght;
    }

    public void setRouteLenght(String routeLenght) {
        this.routeLenght = routeLenght;
    }

    public String getDoneRate() {
        return doneRate;
    }

    public void setDoneRate(String doneRate) {
        this.doneRate = doneRate;
    }

    public String getDoneLenght() {
        return doneLenght;
    }

    public void setDoneLenght(String doneLenght) {
        this.doneLenght = doneLenght;
    }

    public String getUndoLenght() {
        return undoLenght;
    }

    public void setUndoLenght(String undoLenght) {
        this.undoLenght = undoLenght;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Object getResType() {
        return resType;
    }

    public void setResType(Object resType) {
        this.resType = resType;
    }

    public String getTaskUUID() {
        return taskUUID;
    }

    public void setTaskUUID(String taskUUID) {
        this.taskUUID = taskUUID;
    }

    public String getaPoint() {
        return aPoint;
    }

    public void setaPoint(String aPoint) {
        this.aPoint = aPoint;
    }

    public String getzPoint() {
        return zPoint;
    }

    public void setzPoint(String zPoint) {
        this.zPoint = zPoint;
    }

    public String getaPointLng() {
        return aPointLng;
    }

    public void setaPointLng(String aPointLng) {
        this.aPointLng = aPointLng;
    }

    public String getaPointLat() {
        return aPointLat;
    }

    public void setaPointLat(String aPointLat) {
        this.aPointLat = aPointLat;
    }

    public String getzPointLng() {
        return zPointLng;
    }

    public void setzPointLng(String zPointLng) {
        this.zPointLng = zPointLng;
    }

    public String getzPointLat() {
        return zPointLat;
    }

    public void setzPointLat(String zPointLat) {
        this.zPointLat = zPointLat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRouteUUID() {
        return routeUUID;
    }

    public void setRouteUUID(String routeUUID) {
        this.routeUUID = routeUUID;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }


    public String getMyType() {
        return myType;
    }

    public void setMyType(String myType) {
        this.myType = myType;
    }
}
