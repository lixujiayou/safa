package com.inspur.hebeiline.room_;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.inspur.hebeiline.entity.LXResEntity;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.tmline.ErrorInfoBean;
import com.inspur.hebeiline.entity.tmline.LocusPoint;
import com.inspur.hebeiline.entity.tmline.MyWayBean;
import com.inspur.hebeiline.entity.tmline.PhotoInfoBean;


/**
 * Created by lixu on 2017/12/1.
 */
@Database(entities = {User.class, LocusPoint.class, ErrorInfoBean.class, PhotoInfoBean.class, LXResEntity.class
        , LXResEntity.LinesBean.class, MyWayBean.class, LXResNewEntity.class,LXResNewEntity.LinesNewBean.class},version = 1)
@TypeConverters({Converters.class})   //类型转换
public abstract class AppDatabase extends RoomDatabase {
    public abstract MyDao myDao();
}
