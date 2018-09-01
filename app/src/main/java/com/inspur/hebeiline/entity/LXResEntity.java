package com.inspur.hebeiline.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lixu on 2018/6/21.
 */
@Entity(indices = {@Index("routeUUID")} )
public class LXResEntity implements Serializable{

    @NonNull
    @PrimaryKey
    private String routeUUID;
    private String routeName;
    private String aPointUUID;
    private String zPointUUID;
    private String taskUUID;

    @Ignore
    private List<PointsBean> points;
    @Ignore
    private List<LinesBean> lines;

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

    public String getAPointUUID() {
        return aPointUUID;
    }

    public void setAPointUUID(String aPointUUID) {
        this.aPointUUID = aPointUUID;
    }

    public String getZPointUUID() {
        return zPointUUID;
    }

    public void setZPointUUID(String zPointUUID) {
        this.zPointUUID = zPointUUID;
    }

    public List<PointsBean> getPoints() {
        return points;
    }

    public void setPoints(List<PointsBean> points) {
        this.points = points;
    }

    public String getTaskUUID() {
        return taskUUID;
    }

    public void setTaskUUID(String taskUUID) {
        this.taskUUID = taskUUID;
    }

    public List<LinesBean> getLines() {
        return lines;
    }

    public void setLines(List<LinesBean> lines) {
        this.lines = lines;
    }

    public static class PointsBean {
        /**
         * uuid : 40491
         * name : 保定涿州杨家坟（北蔡）
         * resType : 9503
         * lng : 116.20753568649037
         * lat : 39.56827153279531
         */

        private String uuid;
        private String name;
        private String resType;
        private String lng;
        private String lat;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResType() {
            return resType;
        }

        public void setResType(String resType) {
            this.resType = resType;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }
    }

    @Entity(indices = {@Index("taskUUID")} )
//    ,foreignKeys = @ForeignKey(entity = LXResEntity.class,
//            parentColumns = "routeUUID",
//            childColumns = "route_uuid")
    public static class LinesBean implements Serializable{
        /**
         * uuid : 392046951
         * name : 3392004593-里渠至北蔡002号杆-保定涿州杨家坟（北蔡）
         * resType : null
         * aPoint : 391372226
         * zPoint : 40491
         * aPointLng : 116.20754361648505
         * aPointLat : 39.56818050657577
         * zPointLng : 116.20753568649037
         * zPointLat : 39.56827153279531
         * status : 0
         */

        @PrimaryKey(autoGenerate = true)
        private int mid;

        private String taskUUID;

        @ColumnInfo(name = "route_uuid")
        private String routeUUID;

        private String uuid;
        private String name;
        private String resType;
        private String aPoint;
        private String zPoint;
        private String aPointLng;
        private String aPointLat;
        private String zPointLng;
        private String zPointLat;
        private String status;

        private int continuousNum;//连续在150米之外

        @Override
        public String toString() {
            return "LinesBean{" +
                    "mid=" + mid +
                    ", taskUUID='" + taskUUID + '\'' +
                    ", routeUUID='" + routeUUID + '\'' +
                    ", uuid='" + uuid + '\'' +
                    ", name='" + name + '\'' +
                    ", resType='" + resType + '\'' +
                    ", aPoint='" + aPoint + '\'' +
                    ", zPoint='" + zPoint + '\'' +
                    ", aPointLng='" + aPointLng + '\'' +
                    ", aPointLat='" + aPointLat + '\'' +
                    ", zPointLng='" + zPointLng + '\'' +
                    ", zPointLat='" + zPointLat + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public int getContinuousNum() {
            return continuousNum;
        }

        public void setContinuousNum(int continuousNum) {
            this.continuousNum = continuousNum;
        }

        public String getRouteUUID() {
            return routeUUID;
        }

        public void setRouteUUID(String routeUUID) {
            this.routeUUID = routeUUID;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getTaskUUID() {
            return taskUUID;
        }

        public void setTaskUUID(String taskUUID) {
            this.taskUUID = taskUUID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResType() {
            return resType;
        }

        public void setResType(String resType) {
            this.resType = resType;
        }

        public String getAPoint() {
            return aPoint;
        }

        public void setAPoint(String aPoint) {
            this.aPoint = aPoint;
        }

        public String getZPoint() {
            return zPoint;
        }

        public void setZPoint(String zPoint) {
            this.zPoint = zPoint;
        }

        public String getAPointLng() {
            return aPointLng;
        }

        public void setAPointLng(String aPointLng) {
            this.aPointLng = aPointLng;
        }

        public String getAPointLat() {
            return aPointLat;
        }

        public void setAPointLat(String aPointLat) {
            this.aPointLat = aPointLat;
        }

        public String getZPointLng() {
            return zPointLng;
        }

        public void setZPointLng(String zPointLng) {
            this.zPointLng = zPointLng;
        }

        public String getZPointLat() {
            return zPointLat;
        }

        public void setZPointLat(String zPointLat) {
            this.zPointLat = zPointLat;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
