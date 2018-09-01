package com.inspur.hebeiline.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lixu on 2018/6/26.
 */

public class LineResultEntity {

    /**
     * cLength : 100
     * resLevel : 1
     * routeUUID : 100010
     * status : 1
     * subresName : 线资源的名称
     * “lineUUID” : ”线资源的id”
     * taskUUID : 120012
     * totalTime : 200
     * userId : 123
     */

    private String cLength;
    private String resLevel;
    private String routeUUID;
    private String status;
    private String subresName;
    private String lineUUID; // FIXME check this code
    private String taskUUID;
    private String totalTime;
    private String userId;

    @Override
    public String toString() {
        return "LineResultEntity{" +
                "cLength='" + cLength + '\'' +
                ", resLevel='" + resLevel + '\'' +
                ", routeUUID='" + routeUUID + '\'' +
                ", status='" + status + '\'' +
                ", subresName='" + subresName + '\'' +
                ", lineUUID='" + lineUUID + '\'' +
                ", taskUUID='" + taskUUID + '\'' +
                ", totalTime='" + totalTime + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getCLength() {
        return cLength;
    }

    public void setCLength(String cLength) {
        this.cLength = cLength;
    }

    public String getResLevel() {
        return resLevel;
    }

    public void setResLevel(String resLevel) {
        this.resLevel = resLevel;
    }

    public String getRouteUUID() {
        return routeUUID;
    }

    public void setRouteUUID(String routeUUID) {
        this.routeUUID = routeUUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubresName() {
        return subresName;
    }

    public void setSubresName(String subresName) {
        this.subresName = subresName;
    }

    public String getLineUUID() {
        return lineUUID;
    }

    public void setLineUUID(String lineUUID) {
        this.lineUUID = lineUUID;
    }

    public String getTaskUUID() {
        return taskUUID;
    }

    public void setTaskUUID(String taskUUID) {
        this.taskUUID = taskUUID;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
