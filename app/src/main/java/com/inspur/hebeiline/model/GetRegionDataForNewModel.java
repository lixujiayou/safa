package com.inspur.hebeiline.model;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.inspur.hebeiline.R;
import com.inspur.hebeiline.core.AllUrl;
import com.inspur.hebeiline.core.MallRequest;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.MyLatLng;
import com.inspur.hebeiline.entity.eventbean.MyEvent;
import com.inspur.hebeiline.room_.AppDatabase;
import com.inspur.hebeiline.utils.base.BaseViewModel;
import com.inspur.hebeiline.utils.tools.SPUtil;
import com.inspur.hebeiline.utils.tools.StringUtils;
import com.inspur.hebeiline.utils.tools.ToolUtil;
import com.inspur.hebeiline.utils.tools.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixu on 2018/1/23.
 *
 * @author lixu
 */

public class GetRegionDataForNewModel extends BaseViewModel {
    private boolean isRequesting = false;
    private AMap aMap;
    private MapView aMapView;
    private List<LXResNewEntity> mlxResEntities = new ArrayList<>();
    private List<LXResNewEntity> mAlllxResEntities = new ArrayList<>();


    public GetRegionDataForNewModel(Application application) {
        super(application);
    }

    private MutableLiveData<List<LXResNewEntity>> roundSiteList;
    private Context mContext;
    private Activity mActivity;
    private AppDatabase db;

    //本次加载是否完成
    private boolean isLoadEnd = true;

    private String myQueryTye;

    public MutableLiveData<List<LXResNewEntity>> getCurrentData(Activity context, AMap map,MapView mapView) {
        this.mContext = context;
        this.mActivity = context;
        this.aMap = map;
        this.aMapView = mapView;
        if (roundSiteList == null) {
            roundSiteList = new MutableLiveData<>();
        }

        return roundSiteList;
    }

    /**
     * isForNetWork 0:查询网络  1：查询本地或存量  2：查询本地
     */
    public void getNearRes(MallRequest mRequest, String countyId, String isForNetWork, final String taskId,double cLon,double cLat) {
        dismissProgressDialog();
        if (StringUtils.isEmpty(isForNetWork)) {
            isForNetWork = "0";
        } else {
          //  isLoadEnd = true;
        }
        myQueryTye = isForNetWork;

        if (!isLoadEnd) {
            Log.d("qqqqqq","正在加载中，取消本次加载");
            return;
        }


        isLoadEnd = false;


        showProgressDialog(mContext, "初始化选中数据...");
        SPUtil spUtil = new SPUtil(mContext, SPUtil.USER);
        String headInfo = "Bearer " + spUtil.getString(SPUtil.USER_TOKEN, "");
        EventBus.getDefault().post(new MyEvent(MyEvent.OKK));

        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json;charset=UTF-8");
        map.put("Authorization", headInfo);
        String url = AllUrl.mainUrl + AllUrl.getrouteInspectinfo2 + "?countyId=" + countyId + "&userId=T400004238"+"&lon="+cLon+"&lat="+cLat;

        if (isForNetWork.equals("0")) {
            mRequest.getrouteInspectinfoNew(map, url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<List<LXResNewEntity>>() {
                        @Override
                        public void accept(List<LXResNewEntity> lxResEntities) throws Exception {

                            //全部在子线程中进行
                            if (!ToolUtil.isEmpty(lxResEntities)) {
                                Log.d("qqqqqqq","区域查询数据"+lxResEntities.size());
                                //将匹配到的路由存到Room
                                mAlllxResEntities.clear();
                                mAlllxResEntities.addAll(lxResEntities);
                                getAndSaveRoute(lxResEntities, taskId);
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<LXResNewEntity>>() {
                        @Override
                        public void accept(List<LXResNewEntity> lxResEntities) throws Exception {
                            dismissProgressDialog();
//                            EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            isLoadEnd = true;
                            dismissProgressDialog();
//                            EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
                        }
                    });
        } else {
            dismissProgressDialog();
            EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
            if (db == null) {
                db = Room.databaseBuilder(mContext.getApplicationContext(), AppDatabase.class, "database-name").build();
            }
            getDataByThis(taskId);
        }
    }


    /**
     * 将匹配到的路由存到Room
     *
     * @param lxResEntities
     */
    private List<LXResNewEntity.LinesNewBean> noNullList = new ArrayList<>();

    private void getAndSaveRoute(List<LXResNewEntity> lxResEntities, String taskId) {
        if (db == null) {
            db = Room.databaseBuilder(mContext.getApplicationContext(), AppDatabase.class, "database-name").build();
        }
        noNullList.clear();

        if (!ToolUtil.isEmpty(lxResEntities)) {
            for (int i = 0; i < lxResEntities.size(); i++) {
                LXResNewEntity oneRoute = lxResEntities.get(i);
                if (!ToolUtil.isEmpty(oneRoute.getLines())) {
                    for (int j = 0; j < oneRoute.getLines().size(); j++) {
                        LXResNewEntity.LinesNewBean oneLine = oneRoute.getLines().get(j);
                        if (StringUtils.isDouble(oneLine.getAPointLat()) && StringUtils.isDouble(oneLine.getAPointLng())
                                && StringUtils.isDouble(oneLine.getZPointLat()) && StringUtils.isDouble(oneLine.getZPointLng())) {
                            long a = db.myDao().insertRoute(oneRoute);

                            oneLine.setRouteUUID(oneRoute.getRouteUUID());//设置一个关联查询value
                            noNullList.add(oneLine);
                        }
                    }
                } else {
                    continue;
                }
            }

            if (!ToolUtil.isEmpty(noNullList)) {
                List<Long> b = db.myDao().insertLinesNew(noNullList);
                Log.d("qqqqq","插入线"+b.size());
                mAlllxResEntities.clear();
            }


        } else {

            return;
        }


//        if(true){
//            Log.d("qqqqqqq","第0步到了");
//            return;
//        }

        getDataByThis(taskId);
    }

    /**
     * 通过本地获取数据
     */
    private void getDataByThis(final String taskId) {
        Log.d("qqqqqq","区域本地查询开始"+System.currentTimeMillis());
        try {
            //其它页面本地查询
            if (taskId == null) {

                db.myDao().loadAllLinesNew()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<LXResNewEntity.LinesNewBean>>() {
                            @Override
                            public void accept(List<LXResNewEntity.LinesNewBean> linesBeans) {
                                Log.d("qqqqqq",linesBeans.size()+"区域本地查询结束"+System.currentTimeMillis());
                                if (ToolUtil.isEmpty(mAlllxResEntities) || myQueryTye.equals("2")) {
                                    LXResNewEntity LXResNewEntity = new LXResNewEntity();
                                    try {
                                        if (ToolUtil.isEmpty(mlxResEntities)) {
                                            mlxResEntities = new ArrayList<>();
                                            mAlllxResEntities = new ArrayList<>();
                                        } else {
                                            mlxResEntities.clear();
                                            mAlllxResEntities.clear();
                                        }

                                        mlxResEntities.add(LXResNewEntity);
                                        mAlllxResEntities.add(LXResNewEntity);
                                        //将线资源依次设置到路由中
                                        mlxResEntities.get(0).setLines(linesBeans);
                                        mAlllxResEntities.get(0).setLines(linesBeans);

                                    } catch (Exception e) {
                                    }


                                    queryDataForPix(mlxResEntities);
//                                    queryDataForPixForNew(aMap, mlxResEntities);
//                                    queryDataForPixForCirc(aMap, mlxResEntities);

                                }

                            }
                        });
                //巡检页面根据任务ID查询
            } else {
                if (ToolUtil.isEmpty(mAlllxResEntities) || myQueryTye.equals("2")) {
                    db.myDao().loadAllLinesByTaskIdNew(taskId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<List<LXResNewEntity.LinesNewBean>>() {
                                @Override
                                public void accept(List<LXResNewEntity.LinesNewBean> linesBeans) {
                                    LXResNewEntity LXResNewEntity = new LXResNewEntity();
                                    try {
                                        //将线资源依次设置到路由中
                                        if (ToolUtil.isEmpty(mlxResEntities)) {
                                            mlxResEntities = new ArrayList<>();
                                        } else {
                                            mlxResEntities.clear();
                                        }

                                        mlxResEntities.add(LXResNewEntity);
                                        mAlllxResEntities.add(LXResNewEntity);

                                        //将线资源依次设置到路由中
                                        mlxResEntities.get(0).setLines(linesBeans);
                                        mAlllxResEntities.get(0).setLines(linesBeans);
                                    } catch (Exception e) {
                                    }
                                    queryDataForPix(mlxResEntities);

                                    //queryDataForPixForNew(aMap, mlxResEntities);
//                                    queryDataForPixForCirc(aMap, mlxResEntities);

                                }
                            });

                } else {
                    queryDataForPix(mAlllxResEntities);
//                    queryDataForPixForNew(aMap, mlxResEntities);
//                    queryDataForPixForCirc(aMap, mlxResEntities);
                }


            }


        } catch (Exception e) {
        }
    }

    /**
     * 过滤屏幕内的线
     *
     * @param lxResEntities
     */
    private List<LXResNewEntity> lxResEntitiesTest = new ArrayList<>();
    private boolean isParseData = false;
    private void queryDataForPix(List<LXResNewEntity> lxResEntities) {
        if(isParseData){
            return;
        }
        Log.d("qqqqqq",lxResEntities.size() + "区域开始过滤屏幕内的资源"+System.currentTimeMillis());

        isParseData = true;
        lxResEntitiesTest.clear();
        lxResEntitiesTest.addAll(lxResEntities);

        Observable.create(new ObservableOnSubscribe<List<LXResNewEntity>>() {

            @Override
            public void subscribe(ObservableEmitter<List<LXResNewEntity>> emitter) throws Exception {
                List<LXResNewEntity> lxResTest = new ArrayList<>();
                lxResTest.addAll(lxResEntitiesTest);


                for (int i = 0; i < lxResEntitiesTest.size(); i++) {
                    LXResNewEntity tmList = lxResTest.get(i);
                    if (ToolUtil.isEmpty(tmList.getLines())) {
                        continue;
                    }
                    for (int j = 0; j < tmList.getLines().size(); j++) {
                        LatLng latLngMarkerA;
                        LatLng latLngMarkerZ;
                        LXResNewEntity.LinesNewBean lineForOne = tmList.getLines().get(j);
                        if (StringUtils.isDouble(lineForOne.getAPointLat()) && StringUtils.isDouble(lineForOne.getAPointLng())
                                && StringUtils.isDouble(lineForOne.getZPointLat()) && StringUtils.isDouble(lineForOne.getZPointLng())) {
                            latLngMarkerA = new LatLng(StringUtils.toDouble(lineForOne.getAPointLat()), StringUtils.toDouble(lineForOne.getAPointLng()));
                            //转为像素
                            Point aPoint = aMap.getProjection().toScreenLocation(latLngMarkerA);

                            latLngMarkerZ = new LatLng(StringUtils.toDouble(lineForOne.getZPointLat()), StringUtils.toDouble(lineForOne.getZPointLng()));
                            //转为像素
                            Point zPoint = aMap.getProjection().toScreenLocation(latLngMarkerZ);

                            //判断是否在屏幕内，没有则标识为0，后续删除
                            if (isContain(aPoint) || isContain(zPoint)) {
                            } else {
                                lxResEntitiesTest.get(i).getLines().get(j).setAPoint("0");//先临时用0作为无用标识，后面会删掉
                            }
                        } else {
                            lxResEntitiesTest.get(i).getLines().get(j).setAPoint("0");//先临时用0作为无用标识，后面会删掉
                        }
                    }
                }


                //如果没有线在屏幕内，则把这条路由清除掉
                for (int i = 0; i < lxResEntitiesTest.size(); i++) {
                    LXResNewEntity lx1 = lxResEntitiesTest.get(i);
                    if (!ToolUtil.isEmpty(lx1.getLines())) {
                        Iterator<LXResNewEntity.LinesNewBean> iter = lx1.getLines().iterator();
                        while (iter.hasNext()) {
                            LXResNewEntity.LinesNewBean s = iter.next();
                            if (s.getAPoint().equals("0")) {
                                iter.remove();
                            }
                        }
                    }
                }

                if (!ToolUtil.isEmpty(lxResEntitiesTest)) {
                    Iterator<LXResNewEntity> iter = lxResEntitiesTest.iterator();
                    while (iter.hasNext()) {
                        LXResNewEntity s = iter.next();
                        if (s.getLines() == null || s.getLines().size() < 1) {
                            iter.remove();
                        }
                    }
                }


                emitter.onNext(lxResEntitiesTest);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<List<LXResNewEntity>>() {
                    @Override
                    public void accept(List<LXResNewEntity> integer) throws Exception {
                        dismissProgressDialog();
                        isLoadEnd = true;
                        isParseData = false;
                        Log.d("qqqqqq",integer.size()+"区域过滤完成，开始显示到地图"+System.currentTimeMillis());
                        roundSiteList.postValue(integer);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<LXResNewEntity>>() {
                    @Override
                    public void accept(List<LXResNewEntity> lxResEntities) throws Exception {
                    }


                });
    }


    private boolean isContain(Point cPoint) {
        try {
            //手机的像素
            Point myPoint = Utils.getFourCornersOnly(mActivity);
            if (cPoint.x >= 0 && cPoint.x <= myPoint.x && cPoint.y >= 0 && cPoint.y <= myPoint.y) {
                return true;
            }
            return false;
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 某个点是否在区域内
     * @param aMap 地图元素
     * @param lxResEntities 需要判断的点集合数据
     * @return
     */
    public void queryDataForPixForNew(final AMap aMap ,List<LXResNewEntity> lxResEntities) {
        if(isParseData){
            return;
        }
        Log.d("qqqqqq","开始过滤屏幕内的资源"+System.currentTimeMillis());

        isParseData = true;
        lxResEntitiesTest.clear();
        lxResEntitiesTest.addAll(lxResEntities);



        Observable.create(new ObservableOnSubscribe<List<LXResNewEntity>>() {

            @Override
            public void subscribe(ObservableEmitter<List<LXResNewEntity>> emitter) throws Exception {
                List<MyLatLng> latLngList = Utils.getFourCorners(mActivity,aMap);
                PolygonOptions options = new PolygonOptions();
                for (MyLatLng i : latLngList) {
                    LatLng latLng = new LatLng(i.getLat(),i.getLng());
                    options.add(latLng);
                }
                options.visible(false); //设置区域是否显示
                Polygon polygon = aMap.addPolygon(options);



                List<LXResNewEntity> lxResTest = new ArrayList<>();
                lxResTest.addAll(lxResEntitiesTest);


                for (int i = 0; i < lxResEntitiesTest.size(); i++) {
                    LXResNewEntity tmList = lxResTest.get(i);
                    if (ToolUtil.isEmpty(tmList.getLines())) {
                        continue;
                    }
                    for (int j = 0; j < tmList.getLines().size(); j++) {
                        LatLng latLngMarkerA;
                        LatLng latLngMarkerZ;
                        LXResNewEntity.LinesNewBean lineForOne = tmList.getLines().get(j);
                        if (StringUtils.isDouble(lineForOne.getAPointLat()) && StringUtils.isDouble(lineForOne.getAPointLng())
                                && StringUtils.isDouble(lineForOne.getZPointLat()) && StringUtils.isDouble(lineForOne.getZPointLng())) {
                            latLngMarkerA = new LatLng(StringUtils.toDouble(lineForOne.getAPointLat()), StringUtils.toDouble(lineForOne.getAPointLng()));

                            latLngMarkerZ = new LatLng(StringUtils.toDouble(lineForOne.getZPointLat()), StringUtils.toDouble(lineForOne.getZPointLng()));

                            //判断是否在屏幕内，没有则标识为0，后续删除

                            if (polygon.contains(latLngMarkerA) || polygon.contains(latLngMarkerZ)) {

                            } else {
                                lxResEntitiesTest.get(i).getLines().get(j).setAPoint("0");//先临时用0作为无用标识，后面会删掉
                            }
                        } else {
                            lxResEntitiesTest.get(i).getLines().get(j).setAPoint("0");//先临时用0作为无用标识，后面会删掉
                        }
                    }
                }

                //如果没有线在屏幕内，则把这条路由清除掉
                for (int i = 0; i < lxResEntitiesTest.size(); i++) {
                    LXResNewEntity lx1 = lxResEntitiesTest.get(i);
                    if (!ToolUtil.isEmpty(lx1.getLines())) {
                        Iterator<LXResNewEntity.LinesNewBean> iter = lx1.getLines().iterator();
                        while (iter.hasNext()) {
                            LXResNewEntity.LinesNewBean s = iter.next();
                            if (s.getAPoint().equals("0")) {
                                iter.remove();
                            }
                        }
                    }
                }

                if (!ToolUtil.isEmpty(lxResEntitiesTest)) {
                    Iterator<LXResNewEntity> iter = lxResEntitiesTest.iterator();
                    while (iter.hasNext()) {
                        LXResNewEntity s = iter.next();
                        if (s.getLines() == null || s.getLines().size() < 1) {
                            iter.remove();
                        }
                    }
                }

                emitter.onNext(lxResEntitiesTest);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<List<LXResNewEntity>>() {
                    @Override
                    public void accept(List<LXResNewEntity> integer) throws Exception {
                        dismissProgressDialog();
                        isLoadEnd = true;
                        isParseData = false;
                        Log.d("qqqqqq","过滤完成，开始显示到地图"+System.currentTimeMillis());
                        roundSiteList.postValue(integer);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<LXResNewEntity>>() {
                    @Override
                    public void accept(List<LXResNewEntity> lxResEntities) throws Exception {
                    }


                });


    }



    /**
     * 根据方圆判断
     */
    public void queryDataForPixForCirc(final AMap aMap ,List<LXResNewEntity> lxResEntities) {
        if(isParseData){
            return;
        }
        Log.d("qqqqqq","开始过滤屏幕内的资源"+System.currentTimeMillis());

        isParseData = true;
        lxResEntitiesTest.clear();
        lxResEntitiesTest.addAll(lxResEntities);



        Observable.create(new ObservableOnSubscribe<List<LXResNewEntity>>() {

            @Override
            public void subscribe(ObservableEmitter<List<LXResNewEntity>> emitter) throws Exception {

                LatLng latLngCenter = getMapCenterPoint();
                Double xMax = latLngCenter.longitude + (300.0 / 33 / 3600);
                Double xMin = latLngCenter.longitude - (300.0 / 33 / 3600);
                Double yMax = latLngCenter.latitude + (300.0 / 33 / 3600);
                Double yMin = latLngCenter.latitude - (300.0 / 33 / 3600);






                    LXResNewEntity tmList = lxResEntitiesTest.get(0);
                    if (ToolUtil.isEmpty(tmList.getLines())) {
                        return;
                    }
                    for (int j = 0; j < tmList.getLines().size(); j++) {
                        /*LatLng latLngMarkerA;
                        LatLng latLngMarkerZ;

                        LXResNewEntity.LinesNewBean lineForOne = tmList.getLines().get(j);
                        if (StringUtils.isDouble(lineForOne.getAPointLat()) && StringUtils.isDouble(lineForOne.getAPointLng())
                                && StringUtils.isDouble(lineForOne.getZPointLat()) && StringUtils.isDouble(lineForOne.getZPointLng())) {
                            latLngMarkerA = new LatLng(StringUtils.toDouble(lineForOne.getAPointLat()), StringUtils.toDouble(lineForOne.getAPointLng()));

                            latLngMarkerZ = new LatLng(StringUtils.toDouble(lineForOne.getZPointLat()), StringUtils.toDouble(lineForOne.getZPointLng()));

                            //判断是否在屏幕内，没有则标识为0，后续删除

                            if (latLngMarkerA.longitude > xMin && latLngMarkerA.longitude < xMax && latLngMarkerA.latitude > yMin && latLngMarkerA.latitude < yMax) {

                            } else if(latLngMarkerZ.longitude > xMin && latLngMarkerZ.longitude < xMax && latLngMarkerZ.latitude > yMin && latLngMarkerZ.latitude < yMax){

                            }else {
                                lxResEntitiesTest.get(0).getLines().get(j).setAPoint("0");//先临时用0作为无用标识，后面会删掉
                            }
                        } else {
                            lxResEntitiesTest.get(0).getLines().get(j).setAPoint("0");//先临时用0作为无用标识，后面会删掉
                        }*/
                    }


                //如果没有线在屏幕内，则把这条路由清除掉
                    LXResNewEntity lx1 = lxResEntitiesTest.get(0);
                    if (!ToolUtil.isEmpty(lx1.getLines())) {
                        Iterator<LXResNewEntity.LinesNewBean> iter = lx1.getLines().iterator();
                        while (iter.hasNext()) {
                            LXResNewEntity.LinesNewBean s = iter.next();
                            if (s.getAPoint().equals("0")) {
                                iter.remove();
                            }
                        }
                    }

                if (!ToolUtil.isEmpty(lxResEntitiesTest)) {
                    Iterator<LXResNewEntity> iter = lxResEntitiesTest.iterator();
                    while (iter.hasNext()) {
                        LXResNewEntity s = iter.next();
                        if (s.getLines() == null || s.getLines().size() < 1) {
                            iter.remove();
                        }
                    }
                }

                emitter.onNext(lxResEntitiesTest);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<List<LXResNewEntity>>() {
                    @Override
                    public void accept(List<LXResNewEntity> integer) throws Exception {
                        dismissProgressDialog();
                        isLoadEnd = true;
                        isParseData = false;
                        Log.d("qqqqqq","过滤完成，开始显示到地图"+System.currentTimeMillis());
                        roundSiteList.postValue(integer);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<LXResNewEntity>>() {
                    @Override
                    public void accept(List<LXResNewEntity> lxResEntities) throws Exception {
                    }


                });


    }

    /**
     * by moos on 2017/09/05
     * func:获取屏幕中心的经纬度坐标
     * @return
     */
    public LatLng getMapCenterPoint() {
        int left = aMapView.getLeft();
        int top = aMapView.getTop();
        int right = aMapView.getRight();
        int bottom = aMapView.getBottom();
        // 获得屏幕点击的位置
        int x = (int) (aMapView.getX() + (right - left) / 2);
        int y = (int) (aMapView.getY() + (bottom - top) / 2);
        Projection projection = aMap.getProjection();
        LatLng pt = projection.fromScreenLocation(new Point(x, y));

        return pt;
    }



    private SweetAlertDialog pDialog;

    public void showProgressDialog(Context mContext, String title) {
        try {
            pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(R.color.colorPrimary);
            pDialog.setTitleText(title);
            pDialog.setCancelable(false);
            pDialog.show();
        } catch (Exception e) {
        }
    }


    public void dismissProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }


}
