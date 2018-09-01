package com.inspur.hebeiline.utils.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.inspur.hebeiline.entity.MyLatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouwei on 17/2/16.
 */

public class Utils {
    /**
     *  dp 转换 px
     * @param context
     * @param dip
     * @return
     */
    public static int dpToPx(Context context, float dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float valueDips = dip;
        int valuePixels = (int) (valueDips * SCALE + 0.5f);
        return valuePixels;
    }

    /**
     * 获取手机屏幕的左上，右上，右下，左下,四个点坐标
     * @param context
     * @return
     */
    public static List<MyLatLng> getFourCorners(Activity context,AMap aMap){
        List<MyLatLng> myLatLngList = new ArrayList<>();
        WindowManager manager = context.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        Log.d("qqqqq",width+"width=height="+height);
        //左上0，0
        Point pointLeftTop = new Point();
        pointLeftTop.set(0,0);
        LatLng pLeftTop = aMap.getProjection().fromScreenLocation(pointLeftTop);
        myLatLngList.add(new MyLatLng(pLeftTop.latitude,pLeftTop.longitude));
        Log.d("qqqqq","左上经纬度=="+pLeftTop.latitude+","+pLeftTop.longitude);
        //右上width,0
        Point pointRightTop = new Point();
        pointRightTop.set(width,0);
        LatLng pRightTop = aMap.getProjection().fromScreenLocation(pointRightTop);
        myLatLngList.add(new MyLatLng(pRightTop.latitude,pRightTop.longitude));

        //右下width，height
        Point pointRightBottom = new Point();
        pointRightBottom.set(width,height);
        LatLng pRightBottom = aMap.getProjection().fromScreenLocation(pointRightBottom);
        myLatLngList.add(new MyLatLng(pRightBottom.latitude,pRightBottom.longitude));
        Log.d("qqqqq","右下经纬度=="+pRightBottom.latitude+","+pRightBottom.longitude);

        //左下0,height
        Point pointLeftBottom = new Point();
        pointLeftBottom.set(0,height);
        LatLng pLeftBottom = aMap.getProjection().fromScreenLocation(pointLeftBottom);
        myLatLngList.add(new MyLatLng(pLeftBottom.latitude,pLeftBottom.longitude));

        //测试
//        myLatLngList.clear();
//        myLatLngList.add(new MyLatLng(36.683324,114.297025));
//        myLatLngList.add(new MyLatLng(36.684198,114.765521));
//        myLatLngList.add(new MyLatLng(36.509818,114.765884));
//        myLatLngList.add(new MyLatLng(36.510046,114.297044));
        Log.d("qqqqq","四个角的经纬度="+myLatLngList.toString());
        return myLatLngList;
    }

    /**
     * 获得屏幕右下的像素
     * @param context
     * @return
     */
    public static Point getFourCornersOnly(Activity context){
        WindowManager manager = context.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        Point point = new Point();
        point.set(width,height);

        return point;
    }
}
