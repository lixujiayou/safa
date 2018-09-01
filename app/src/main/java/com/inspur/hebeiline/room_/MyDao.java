package com.inspur.hebeiline.room_;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.Update;


import com.inspur.hebeiline.entity.LXResEntity;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.tmline.LocusPoint;
import com.inspur.hebeiline.entity.tmline.MyWayBean;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by lixu on 2017/12/1.
 */
@Dao
public interface MyDao {


    //User ======================================================================================================

    /**
     * Insert
     */
    //插入多条数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
//重复替换
    void insertAll(User... users);

    //插入固定条数数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBothUsers(User user1, User user2);

    //插入固定1条数数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneUser(User user1);

    // ?
    @Insert
    void insertUsersAndFriend(User user, List<User> friends);

    /**
     * update
     */
    @Update
    void updateUsers(User... users);

    /**
     * delete
     */
    /*@Delete()
    void deleteAll();*/
    @Delete
    void delete(User... user);

    @Delete
    void delete(List<User> users);

  /*  @Delete
    void deleteUsers(User... users);*/

    /**
     * query
     */

    //只需要几个字段的查询

    // @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM users")
    List<User> getAll();

    /*@Query("SELECT * FROM users WHERE uid IN (:ids)")
    List<User> loadAllByIds(int[] ids);

    @Query("SELECT * FROM users WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
    User fundByName(String first,String last);

    //获取所有年龄大于minAge的用户
    @Query("SELECT * FROM users WHERE age > :minAge")
    List<User> loadAllUsersOlderThan(int minAge);

    //获取所有年龄在minAge和maxAge之间的用户
    @Query("SELECT * FROM users WHERE age BETWEEN :minAge AND maxAge")
    List<User> loadAllUsersBetweenAges(int minAge,int maxAge);

    //查询某n个地区的所有用户查询某两个地区的所有用户
    @Query("SELECT * FROM users WHERE regin in :regions")
    List<User> loadUsersFromRegions(List<String> regions);

    //前面提到了LiveData，可以异步的获取数据，那么我们的Room也是支持异步查询的。
    @Query("SELECT * FROM users WHERE region in :regions")
    LiveData<List<User>> loadUsersFromRegionsSync(List<String> regions);*/


    //LocusPoint本地点=======================================================================================================================
    //插入固定1个当前记录点
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneLocation(LocusPoint recordLocationEntity);

    //删除指定记录点
    @Delete
    void deleteLocation(List<LocusPoint> recordLocationEntities);

    //查询所有的记录点
    @Query("SELECT * FROM locus_point")
    List<LocusPoint> getAllLocation();

    //查询指定id的点
//    @Query("SELECT * FROM locus_point WHERE idAuto IN (:ids)")
//    List<LocusPoint> loadAllByIds(String... ids);


    /**
     * 最近的路由
     */
    //保存最近的路由
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRoute(LXResEntity route);

    //保存路由
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertRouteList(List<LXResEntity> routes);

    //删除最近的路由
    @Delete
    int deleteRoutes(List<LXResEntity> route);

    //查出最近的路由
    @Query("SELECT * FROM LXResEntity")
    Flowable<List<LXResEntity>> loadRoute();

    //查出任务下的路由
    @Query("SELECT * FROM LXResEntity where taskUUID = :taskID")
    Flowable<List<LXResEntity>> loadRoute(String taskID);


    //插入路由所有的段
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertLines(List<LXResEntity.LinesBean> lines);


    //查出所有的段
    @Query("SELECT * from LinesBean where route_uuid = :routId")
    Flowable<List<LXResEntity.LinesBean>> loadAllLines(String routId);

    //查出所有的段
    @Query("SELECT * from LinesBean where taskUUID = :taskID")
    Flowable<List<LXResEntity.LinesBean>> loadAllLinesByTaskId(String taskID);

    //查出所有的段
    @Query("SELECT * from LinesBean")
    Flowable<List<LXResEntity.LinesBean>> loadAllLines();

    //查出所有的段
    @Query("SELECT * from LinesBean")
    List<LXResEntity.LinesBean> loadAllLines2();

    //更新段
    @Update
    int updateLines(LXResEntity.LinesBean lines);


    //查出所有的段
    @Query("SELECT * from LinesBean")
    Flowable<List<LXResEntity.LinesBean>> loadAllAllLines();


    //删除所有的段
    @Delete
    int deleteAllLines(List<LXResEntity.LinesBean> linesBeans);

    /**
     * 最近的路由New
     */
    //保存最近的路由
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRoute(LXResNewEntity route);

    //保存路由
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertRouteListNew(List<LXResNewEntity> routes);

    //删除最近的路由
    @Delete
    int deleteRoutesNew(List<LXResNewEntity> route);

    //查出最近的路由
    @Query("SELECT * FROM LXResNewEntity")
    Flowable<List<LXResNewEntity>> loadRouteNew();

    //查出任务下的路由
    @Query("SELECT * FROM LXResNewEntity where taskUUID = :taskID")
    Flowable<List<LXResNewEntity>> loadRouteNew(String taskID);


    //插入路由所有的段
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertLinesNew(List<LXResNewEntity.LinesNewBean> lines);


    //查出所有的段
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from LinesNewBean where routeUUID = :routId")
    Flowable<List<LXResNewEntity.LinesNewBean>> loadAllLinesNew(String routId);

    //查出所有的段
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from LinesNewBean where taskUUID = :taskID")
    Flowable<List<LXResNewEntity.LinesNewBean>> loadAllLinesByTaskIdNew(String taskID);

    //查出所有的段
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from LinesNewBean")
    Flowable<List<LXResNewEntity.LinesNewBean>> loadAllLinesNew();

    //查出所有的段
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from LinesNewBean")
    List<LXResNewEntity.LinesNewBean> loadAllLines2New();

    //更新段
    @Update
    int updateLines(LXResNewEntity.LinesNewBean lines);


    //查出所有的段
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * from LinesNewBean")
    Flowable<List<LXResNewEntity.LinesNewBean>> loadAllAllLinesNew();


    //删除所有的段
    @Delete
    int deleteAllLinesNew(List<LXResNewEntity.LinesNewBean> linesBeans);


    /**
     * 用户跑的段
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertWay(MyWayBean myWayBean);


    @Query("SELECT * from MyWayBean")
    Flowable<List<MyWayBean>> loadAllWays();

    //删除所有的
    @Delete
    int deleteAllWays(List<MyWayBean> myWayBeans);

    /**
     * ErroInfoBean隐患
     */
    //====================================================================================================================================

//    //新增一条隐患
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertOneTrouble(ErrorInfoBean recordLocationEntity);
//
//    //查询所有的隐患
//    @Query("SELECT * FROM erro_info")
//    List<ErrorInfoBean> getAllTrouble();
//
//   //根据id查询隐患
//    @Query("SELECT * FROM erro_info WHERE id IN (:id)")
//    ErrorInfoBean getOneTrouble(int id);
//
//    //删除指定隐患
//    @Delete
//    void deleteErro(List<ErrorInfoBean> recordLocationEntities);
//
//     // update
//     // 修改隐患
//
//    @Update
//    void updateErro(ErrorInfoBean users);

}
