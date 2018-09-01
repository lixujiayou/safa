package com.inspur.hebeiline.utils.tools;

import android.content.Context;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

/**
 * Created by lixu on 2018/4/19.
 *
 * WGS-84：是国际标准，GPS坐标（Google Earth使用、或者GPS模块）
 * GCJ-02：中国坐标偏移标准，Google Map、高德、腾讯使用
 * BD-09：百度坐标偏移标准，Baidu Map使用

 */

public class AMapToWGS {
    private final static double a=6378245.0;
    private final static double pi=3.14159265358979324;
    private final static double ee=0.00669342162296594626;

    /**
     *
     *
     * gcj-02  to  wgs-84
     * 高德转GPS
     * @param latitude
     * @param longitude
     * @return
     */
    public static LatLng toWGS84Point(double latitude,double longitude){
        LatLonPoint dev=calDev(latitude, longitude);
        double retLat = latitude-dev.getLatitude();
        double retLon=longitude-dev.getLongitude();
        dev=calDev(retLat, retLon);
        retLat=latitude-dev.getLatitude();
        retLon=longitude-dev.getLongitude();

        return new LatLng(retLat, retLon);

    }


    /**
     * GPS转高德
     * @param latitude
     * @param longitude
     * @return
     */
    //wsg84 to  gcj02
    public static LatLng toGCJ02Piont(Context context,double latitude, double longitude){


//        LatLonPoint dev=calDev(latitude, longitude);
//        double retLat = latitude-dev.getLatitude();
//        double retLon=longitude-dev.getLongitude();
//        return new LatLng(retLat, retLon);


        CoordinateConverter converter  = new CoordinateConverter(context);
// CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
// sourceLatLng待转换坐标点 LatLng类型
        converter.coord(new LatLng(latitude,longitude));
// 执行转换操作
        LatLng desLatLng = converter.convert();

        return desLatLng;

    }


    private static LatLonPoint calDev(double wgLat,double wgLon){
        if(isOutofChina(wgLat,wgLon)){
            return new LatLonPoint(0,0);
        }
        double dLat=calLat(wgLon-105.0,wgLat-35.0);
        double dLon=calLon(wgLon-105.0, wgLat-35.0);
        double radLat=wgLat/180.0*pi;
        double magic=Math.sin(radLat);
        magic=1-ee*magic*magic;
        double sqrtMagic=Math.sqrt(magic);
        dLat=(dLat*180.0)/((a*(1-ee))/(magic*sqrtMagic)*pi);
        dLon=(dLon*180.0)/(a/sqrtMagic*Math.cos(radLat)*pi);
        return new LatLonPoint(dLat,dLon);
    }

    private static double calLat(double x, double y) {
        double ret=-100.0+2.0*x+3.0*y+0.2*y*y+0.1*x*y+0.2*Math.sqrt(Math.abs(x));
        ret +=(20.0*Math.sin(6.0*x*pi)+20.0*Math.sin(2.0*x*pi))*2.0/3.0;
        ret +=(20.0*Math.sin(y*pi)+40.0*Math.sin(y/3.0*pi))*2.0/3.0;
        ret +=(160.0*Math.sin(y/12.0*pi)+320*Math.sin(y*pi/30.0))*2.0/3.0;

        return ret;
    }

    private static double calLon(double x,double y){
        double ret=300.0+x+2.0*y+0.1*x*x+0.1*x*y+0.1*Math.sqrt(Math.abs(x));
        ret +=(20.0*Math.sin(6.0*x*pi)+20.0*Math.sin(2.0*x*pi))*2.0/3.0;
        ret +=(20.0*Math.sin(x*pi)+40.0*Math.sin(x/3.0*pi))*2.0/3.0;
        ret +=(150.0*Math.sin(x/12.0*pi)+300.0*Math.sin(x/30.0*pi))*2.0/3.0;;

        return ret;

    }


    private static boolean isOutofChina(double lat, double lon) {
        if(lon<72.004 || lon>137.8347){
            return true;
        }
        if(lat<0.8293 || lat>55.8271){
            return true;
        }

        return false;
    }
}
