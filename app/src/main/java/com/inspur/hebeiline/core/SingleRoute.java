package com.inspur.hebeiline.core;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;

import com.amap.api.maps.model.LatLng;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.tmline.RouteInfoBean;
import com.inspur.hebeiline.utils.tools.StringUtils;

import java.util.List;

/**
 * Created by lixu on 2018/3/30.
 */

public class SingleRoute {


    private String length;
    private static final SingleRoute ourInstance = new SingleRoute();
    private LXResNewEntity mCLXResNewEntity;//当前的路由
    private LXResNewEntity.LinesNewBean mCNearLine;//最近的点
    private List<LXResNewEntity.LinesNewBean> mCBothLines;//最近点的两端点
    private String AORZ;
    private String strUUID;

    private LXResNewEntity.LinesNewBean endLine;



    private List<LXResNewEntity.LinesNewBean> currentLines;//当前匹配到的线
    private LatLng mateLatLng;//匹配到的线，又再80米内的资源


    //当前的
    private int mid;
    private String taskUUID;
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


    public static SingleRoute getInstance() {
        return ourInstance;
    }

    private SingleRoute() {
    }

    public List<LXResNewEntity.LinesNewBean> getCurrentLine() {
        return currentLines;
    }

    public LatLng getMateLatLng() {
        return mateLatLng;
    }

    public void setMateLatLng(LatLng mateLatLng) {
        this.mateLatLng = mateLatLng;
    }

    public void setCurrentLine(List<LXResNewEntity.LinesNewBean> currentLine) {
        this.currentLines = currentLine;
    }

    public List<LXResNewEntity.LinesNewBean> getmCBothLines() {
        return mCBothLines;
    }

    public void setmCBothLines(List<LXResNewEntity.LinesNewBean> mCBothLines) {
        this.mCBothLines = mCBothLines;
    }

    public LXResNewEntity.LinesNewBean getEndLine() {
        return endLine;
    }

    public void setEndLine(LXResNewEntity.LinesNewBean endLine) {
        this.endLine = endLine;
    }

    public String getAORZ() {
        return AORZ;
    }

    public void setAORZ(String AORZ) {
        this.AORZ = AORZ;
    }

    public LXResNewEntity getmCLXResNewEntity() {
        return mCLXResNewEntity;
    }

    public void setmCLXResNewEntity(LXResNewEntity mCLXResNewEntity) {
        this.mCLXResNewEntity = mCLXResNewEntity;
    }

    public LXResNewEntity.LinesNewBean getmCNearLine() {
        return mCNearLine;
      /*  if(mCNearLine != null) {
            return mCNearLine;
        }else{
            mCNearLine = new LXResNewEntity.LinesNewBean();
//            if (!StringUtils.isEmpty(taskUUID)) {
//                mCNearLine.setTaskUUID(this.taskUUID);
//            }
            if (!StringUtils.isEmpty(routeUUID)) {
                mCNearLine.setRouteUUID(routeUUID);
            }
            if (!StringUtils.isEmpty(uuid)) {
                mCNearLine.setUuid(uuid);
            }
            if (!StringUtils.isEmpty(name)) {
                mCNearLine.setName(name);
            }
            if (!StringUtils.isEmpty(resType)) {
                mCNearLine.setResType(resType);
            }
            if (!StringUtils.isEmpty(aPoint)) {
                mCNearLine.setAPoint(aPoint);
            }
            if (!StringUtils.isEmpty(zPoint)) {
                mCNearLine.setZPoint(zPoint);
            }
            if (!StringUtils.isEmpty(aPointLng)) {
                mCNearLine.setAPointLng(aPointLng);
            }
            if (!StringUtils.isEmpty(aPointLat)) {
                mCNearLine.setAPointLat(aPointLat);
            }
            if (!StringUtils.isEmpty(zPointLng)) {
                mCNearLine.setZPointLng(zPointLng);
            }
            if (!StringUtils.isEmpty(zPointLat)) {
                mCNearLine.setZPointLat(zPointLat);
            }
            if (!StringUtils.isEmpty(status)) {
                mCNearLine.setStatus(status);
            }
            return mCNearLine;

        }*/
    }

    public void setmCNearLine(LXResNewEntity.LinesNewBean mCNearLine) {
        this.mCNearLine = mCNearLine;
//        if (!StringUtils.isEmpty(mCNearLine.getTaskUUID())) {
//            this.taskUUID = mCNearLine.getTaskUUID();
//        }
//        if (!StringUtils.isEmpty(mCNearLine.getRouteUUID())) {
//
//            this.routeUUID = mCNearLine.getRouteUUID();
//        }
//        if (!StringUtils.isEmpty(mCNearLine.getUuid())) {
//
//            this.uuid = mCNearLine.getUuid();
//        }
//        if (!StringUtils.isEmpty(mCNearLine.getName())) {
//
//            this.name = mCNearLine.getName();
//        }
//        if (!StringUtils.isEmpty(mCNearLine.getResType())) {
//
//            this.resType = mCNearLine.getResType();
//        }
//        if (!StringUtils.isEmpty(mCNearLine.getAPoint())) {
//
//            this.aPoint = mCNearLine.getAPoint();
//        }
//        if (!StringUtils.isEmpty(mCNearLine.getZPoint())) {
//
//            this.zPoint = mCNearLine.getZPoint();
//        }
//        if (!StringUtils.isEmpty(mCNearLine.getAPointLng())) {
//
//            this.aPointLng = mCNearLine.getAPointLng();
//        }
//        if (!StringUtils.isEmpty(mCNearLine.getAPointLat())) {
//
//            this.aPointLat = mCNearLine.getAPointLat();
//        }
//        if (!StringUtils.isEmpty(mCNearLine.getZPointLng())) {
//            this.zPointLng = mCNearLine.getZPointLng();
//        }
//        if (!StringUtils.isEmpty(mCNearLine.getZPointLat())) {
//            this.zPointLat = mCNearLine.getZPointLat();
//        }
//        if (!StringUtils.isEmpty(mCNearLine.getStatus())) {
//            this.status = mCNearLine.getStatus();
//        }


    }

    private RouteInfoBean routeInfoBean;

    public RouteInfoBean getRouteInfoBean() {
        return routeInfoBean;
    }

    public void setRouteInfoBean(RouteInfoBean routeInfoBean) {
        this.routeInfoBean = routeInfoBean;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }


    public String getStrUUID() {
        return strUUID;
    }

    public void setStrUUID(String strUUID) {
        this.strUUID = strUUID;
    }
}
