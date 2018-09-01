package com.inspur.hebeiline.utils.tools;

import android.util.Log;

import com.amap.api.maps.Projection;
import com.amap.api.maps.model.LatLng;
import com.inspur.hebeiline.entity.LXResEntity;
import com.inspur.hebeiline.entity.LXResNewEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyakun on 2018/7/26.
 */

public class Unility {

    /**
     * 查询点击点附近的线资源
     * @param clickLatlng
     * @param resList
     * @param pixel
     * @return
     */
    public static List<LXResNewEntity.LinesNewBean> getNearbyLine(Projection mProjection, LatLng clickLatlng, List<LXResNewEntity> resList, int pixel){
        //点击附近的经纬度转坐标
        android.graphics.Point centerPoint = mProjection.toScreenLocation(clickLatlng);
        int xMin = centerPoint.x - pixel;
        int xMax = centerPoint.x + pixel;
        int yMin = centerPoint.y - pixel;
        int yMax = centerPoint.y + pixel;
        android.graphics.Point minPoint = new android.graphics.Point(xMin, yMin);
        android.graphics.Point maxPoint = new android.graphics.Point(xMax, yMax);
        List<LXResNewEntity.LinesNewBean> items = new ArrayList<>();
        //矩形四条线段
        android.graphics.Point leftTopPoint = new android.graphics.Point(xMin, yMax);
        android.graphics.Point leftBottomPoint = new android.graphics.Point(xMin, yMin);
        android.graphics.Point rightTopPoint = new android.graphics.Point(xMax, yMax);
        android.graphics.Point rightBottomPoint = new android.graphics.Point(xMax, yMin);
        android.graphics.Point[][] arrayLine = {
                {leftTopPoint, rightTopPoint},
                {leftTopPoint, leftBottomPoint},
                {rightTopPoint, rightBottomPoint},
                {leftBottomPoint, rightBottomPoint},
        };
        for(LXResNewEntity lxResEntity:resList){
            for(LXResNewEntity.LinesNewBean item:lxResEntity.getLines()){
                LXResNewEntity.LinesNewBean newitem = isInRetancle(mProjection,item,minPoint,maxPoint,arrayLine);
                if(null!=newitem){
                    items.add(newitem);
                    Log.v("click","点击位置附近有资源,资源类型"+newitem.getTaskUUID()+":"+newitem.getUuid());
                }
            }
        }

       return items;
    }

    /**
     * 判断线是否与矩形相交，如果相交则返回线对象，不相交则不返回
     * @param mProjection
     * @param lineItem
     * @param minPoint
     * @param maxPoint
     * @param arrayLine
     * @return
     */
    public static LXResNewEntity.LinesNewBean isInRetancle(Projection mProjection,LXResNewEntity.LinesNewBean lineItem,android.graphics.Point minPoint,
                                              android.graphics.Point maxPoint,
                                              android.graphics.Point[][] arrayLine){

        android.graphics.Point lastPoint =
                mProjection.toScreenLocation(new LatLng(Double.valueOf(lineItem.getAPointLat()), Double.valueOf(lineItem.getAPointLng())));
        android.graphics.Point nextPoint =
                mProjection.toScreenLocation(new LatLng(Double.valueOf(lineItem.getZPointLat()), Double.valueOf(lineItem.getZPointLng())));
        int pointXmin = Math.min(lastPoint.x, nextPoint.x);
        int pointXmax = Math.max(lastPoint.x, nextPoint.x);
        int pointYmin = Math.min(lastPoint.y, nextPoint.y);
        int pointYmax = Math.max(lastPoint.y, nextPoint.y);
//快速排斥实验，根据以线段的对角线形成的矩形 边界比较左右边界和上下边界
        if (pointXmax < minPoint.x
                || pointXmin > maxPoint.x
                || pointYmax < minPoint.y
                || pointYmin > maxPoint.y
                ) {
            //这种情况下，不会重合
        } else {
            //在判断其他情况，跨立试验
            //判断P1P2跨立Q1Q2的依据是：( P1 - Q1 ) × ( Q2 - Q1 ) * ( Q2 - Q1 ) × ( P2 - Q1 ) >= 0
            //(P1.x -Q1.x ,P1.y -Q1.y)×(Q2.x-Q1.x,Q2.y-Q1.y) *(Q2.x-Q1.x,Q2.y-Q1.y)×(P2.x-Q1.x,P2.y-Q1.y)>=0
            //判断P1P2跨立Q1Q2的依据是：( Q1 - P1 ) × ( P2 - P1 ) * ( P2 - P1 ) × ( Q2 - P1 ) >= 0
            //(Q1.x -P1.x ,Q1.y -P1.y)×(P2.x-P1.x,P2.y-P1.y) *(P2.x-P1.x,P2.y-P1.y)×(Q2.x-P1.x,Q2.y-P1.y)>=0

            android.graphics.Point P1 = lastPoint;
            android.graphics.Point P2 = nextPoint;

            for (int m = 0; m < arrayLine.length; m++) {
                android.graphics.Point[] points = arrayLine[m];
                android.graphics.Point Q1 = points[0];
                android.graphics.Point Q2 = points[1];

                //P × Q = x1*y2 - x2*y1

                boolean left = (((P1.x - Q1.x) * (Q2.y - Q1.y) - (Q2.x - Q1.x) * (P1.y - Q1.y))
                        * ((Q2.x - Q1.x) * (P2.y - Q1.y) - (P2.x - Q1.x) * (Q2.y - Q1.y))) >= 0;
                boolean right = (((Q1.x - P1.x) * (P2.y - P1.y) - (P2.x - P1.x) * (Q1.y - P1.y))
                        * ((P2.x - P1.x) * (Q2.y - P1.y) - (Q2.x - P1.x) * (P2.y - P1.y))) >= 0;

                if (left && right) {
                    //所以线段与矩形任意一条边相交
                    return lineItem;
                }
            }
        }
        return null;
    }


}
