package com.inspur.hebeiline.entity.post_bean;

import com.amap.api.maps.model.LatLng;
import com.inspur.hebeiline.entity.MyLatLng;

import java.util.List;

/**
 * Created by lixu on 2018/6/20.
 */

public class LXGetResPostBean {
    private String coordtype;
    private String where;
    private String layerId;
    private String indentify_tpl;
    private String orderByFields;
    private List<MyLatLng> path;

    public String getCoordtype() {
        return coordtype;
    }

    public void setCoordtype(String coordtype) {
        this.coordtype = coordtype;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getIndentify_tpl() {
        return indentify_tpl;
    }

    public void setIndentify_tpl(String indentify_tpl) {
        this.indentify_tpl = indentify_tpl;
    }

    public String getOrderByFields() {
        return orderByFields;
    }

    public void setOrderByFields(String orderByFields) {
        this.orderByFields = orderByFields;
    }

    public List<MyLatLng> getPath() {
        return path;
    }

    public void setPath(List<MyLatLng> path) {
        this.path = path;
    }
}
