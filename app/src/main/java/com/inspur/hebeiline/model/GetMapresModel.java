package com.inspur.hebeiline.model;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.inspur.hebeiline.R;
import com.inspur.hebeiline.core.AllUrl;
import com.inspur.hebeiline.core.Constance;
import com.inspur.hebeiline.core.MallRequest;
import com.inspur.hebeiline.core.SingleRoute;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.MyLatLng;
import com.inspur.hebeiline.entity.TMLineMainEntity;
import com.inspur.hebeiline.entity.eventbean.MyEvent;
import com.inspur.hebeiline.entity.post_bean.LXGetResPostBean;
import com.inspur.hebeiline.entity.tmline.MyWayBean;
import com.inspur.hebeiline.room_.AppDatabase;
import com.inspur.hebeiline.utils.base.BaseViewModel;
import com.inspur.hebeiline.utils.tools.SPUtil;
import com.inspur.hebeiline.utils.tools.StringUtils;
import com.inspur.hebeiline.utils.tools.TimeUtils;
import com.inspur.hebeiline.utils.tools.ToastUtil;
import com.inspur.hebeiline.utils.tools.ToolUtil;
import com.inspur.hebeiline.utils.tools.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixu on 2018/1/23.
 *
 * @author lixu
 */

public class GetMapresModel extends BaseViewModel {
    private boolean isRequesting = false;
    private AMap aMap;
    private LatLng latLngMarkerA;
    private LatLng latLngMarkerZ;
    private List<LXResNewEntity> mlxResEntities = new ArrayList<>();

    public GetMapresModel(Application application) {
        super(application);
    }

    private MutableLiveData<List<LXResNewEntity>> roundSiteList;
    private Context mContext;
    private Activity mActivity;
    private AppDatabase db;
    private String myRouteidStrs;

    public MutableLiveData<List<LXResNewEntity>> getCurrentData(Activity context, AMap map) {
        this.mContext = context;
        this.mActivity = context;
        this.aMap = map;
        if (roundSiteList == null) {
            roundSiteList = new MutableLiveData<>();
        }
        latLngMarkerA = new LatLng(0, 0);
        latLngMarkerZ = new LatLng(0, 0);
        return roundSiteList;
    }

    /**
     * @param mRequest 手机屏幕的左上，右上，右下，左下,四个点
     */
    public void getNearRes(MallRequest mRequest, String routeidStr, boolean isForNetWork,final boolean isHint) {
        dismissProgressDialog();
        if (routeidStr == null) {
            routeidStr = myRouteidStrs;
        } else {
            myRouteidStrs = routeidStr;
        }


//        Gson gs = new Gson();
//        List<LXResNewEntity> jsonListObject = gs.fromJson(test, new TypeToken<List<LXResNewEntity>>(){}.getType());//把JSON
//        if (roundSiteList == null) {
//            roundSiteList = new MutableLiveData<>();
//        }
//        Log.d("qqqqqq","临时数据=="+jsonListObject.size());
//        roundSiteList.postValue(jsonListObject);
//
//        if(true){
//            return;
//        }

        showProgressDialog(mContext, "正在查询资源...");
        SPUtil spUtil = new SPUtil(mContext, SPUtil.USER);
        String cId = spUtil.getString("county_uid","");
        String headInfo = "Bearer " + spUtil.getString(SPUtil.USER_TOKEN, "");
        EventBus.getDefault().post(new MyEvent(MyEvent.OKK));

        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json;charset=UTF-8");
        map.put("Authorization", headInfo);
        String url = AllUrl.mainUrl + AllUrl.getrouteInspectinfo + "?routeidStr=" + routeidStr + "&countyId="+cId+"&userId=T400004238";
        Log.d("qqqqq", "查询周边Res==" + url);

        if (isForNetWork) {
           /* mRequest.getrouteInspectinfo(map, url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .doOnNext(new Consumer<List<LXResNewEntity>>() {
                        @Override
                        public void accept(List<LXResNewEntity> lxResEntities) throws Exception {
                            List<LXResNewEntity> lxResTest = new ArrayList<>();
                            Log.d("qqqqqqq", "one");

                            //全部在子线程中进行
                            if (!ToolUtil.isEmpty(lxResEntities)) {
                                Log.d("qqqqqqq", "two" + lxResEntities.size());

                                //遍历出最近的点，找到最近点所在的路由，并将匹配到的路由存到Room
                                getAndSaveRoute(lxResEntities);

                                lxResTest.addAll(lxResEntities);
                                for (int i = 0; i < lxResTest.size(); i++) {
                                    LXResNewEntity tmList = lxResTest.get(i);
                                    for (int j = 0; j < tmList.getLines().size(); j++) {
                                        LXResNewEntity.LinesNewBean lineForOne = tmList.getLines().get(j);
                                        if (StringUtils.isDouble(lineForOne.getAPointLat()) && StringUtils.isDouble(lineForOne.getAPointLng())
                                                && StringUtils.isDouble(lineForOne.getZPointLat()) && StringUtils.isDouble(lineForOne.getZPointLng())) {
                                            latLngMarkerA = null;
                                            latLngMarkerA = new LatLng(StringUtils.toDouble(lineForOne.getAPointLat()), StringUtils.toDouble(lineForOne.getAPointLng()));
                                            //转为像素
                                            Point aPoint = aMap.getProjection().toScreenLocation(latLngMarkerA);

                                            latLngMarkerZ = null;
                                            latLngMarkerZ = new LatLng(StringUtils.toDouble(lineForOne.getZPointLat()), StringUtils.toDouble(lineForOne.getZPointLng()));
                                            //转为像素
                                            Point zPoint = aMap.getProjection().toScreenLocation(latLngMarkerZ);

                                            //判断是否在屏幕内，没有则标识为0，后续删除
                                            if (isContain(aPoint) && isContain(zPoint)) {
                                            } else {
                                                lxResEntities.get(i).getLines().get(j).setAPoint("0");//先临时用0作为无用标识，后面会删掉
                                            }
                                        } else {
                                            lxResEntities.get(i).getLines().get(j).setAPoint("0");//先临时用0作为无用标识，后面会删掉
                                        }
                                    }
                                }


                                //如果没有线在屏幕内，则把这条路由清除掉
                                for (int i = 0; i < lxResEntities.size(); i++) {
                                    LXResNewEntity lx1 = lxResEntities.get(i);
                                    Iterator<LXResNewEntity.LinesNewBean> iter = lx1.getLines().iterator();
                                    while (iter.hasNext()) {
                                        LXResNewEntity.LinesNewBean s = iter.next();
                                        if (s.getAPoint().equals("0")) {
                                            iter.remove();
                                        }
                                    }
                                }

                                Iterator<LXResNewEntity> iter = lxResEntities.iterator();
                                while (iter.hasNext()) {
                                    LXResNewEntity s = iter.next();
                                    if (s.getLines() == null || s.getLines().size() < 1) {
                                        iter.remove();
                                    }
                                }
                                Log.d("qqqqq", "共在屏幕内" + lxResEntities.size());
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<LXResNewEntity>>() {
                        @Override
                        public void accept(List<LXResNewEntity> lxResEntities) throws Exception {
                            dismissProgressDialog();
                            EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
                            String jsonRequest = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss Z").create().toJson(lxResEntities);
                            Log.d("qqqqqq", "地图资源返回==" + jsonRequest);
                            if (roundSiteList == null) {
                                roundSiteList = new MutableLiveData<>();
                            }
                            if(isHint) {
                                ToastUtil.showToast(mContext, "自动匹配到最近段==" + mCNearLine.getName() + ",请立即开始巡检", ToastUtil.TOAST_TYPE_SUCCESS);
                            }
                            roundSiteList.postValue(lxResEntities);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            dismissProgressDialog();
                            EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
                            Log.d("qqqqqq", "Constance.getMsgByException(e)==" + Constance.getMsgByException(throwable));
                        }
                    });*/
        } else {
            dismissProgressDialog();
            EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
            if (db == null) {
                db = Room.databaseBuilder(mContext.getApplicationContext(), AppDatabase.class, "database-name").build();
            }
           /* db.myDao().loadRouteNew()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<LXResNewEntity>>() {
                        @Override
                        public void accept(List<LXResNewEntity> lxResEntities) throws Exception {

                            if (!ToolUtil.isEmpty(lxResEntities)) {
                                mlxResEntities.addAll(lxResEntities);
                                db.myDao().loadAllLinesNew(lxResEntities.get(0).getRouteUUID())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<List<LXResNewEntity.LinesNewBean>>() {
                                            @Override
                                            public void accept(List<LXResNewEntity.LinesNewBean> linesBeans) throws Exception {
                                                Log.d("qqqqqq", "room查询route==" + linesBeans.size());
                                                if(!ToolUtil.isEmpty(linesBeans) && !ToolUtil.isEmpty(mlxResEntities)) {
                                                    mlxResEntities.get(0).setLines(linesBeans);
                                                    queryDataForPix(mlxResEntities);
                                                }
                                            }
                                        });
                            }
                        }
                    });*/
        }
    }


    private void queryDataForPix(List<LXResNewEntity> lxResEntities) {
        dismissProgressDialog();
        List<LXResNewEntity> lxResTest = new ArrayList<>();
        lxResTest.addAll(lxResEntities);

        LXResNewEntity tmList = lxResTest.get(0);
        for (int j = 0; j < tmList.getLines().size(); j++) {
            LXResNewEntity.LinesNewBean lineForOne = tmList.getLines().get(j);
            if (StringUtils.isDouble(lineForOne.getAPointLat()) && StringUtils.isDouble(lineForOne.getAPointLng())
                    && StringUtils.isDouble(lineForOne.getZPointLat()) && StringUtils.isDouble(lineForOne.getZPointLng())) {
                latLngMarkerA = null;
                latLngMarkerA = new LatLng(StringUtils.toDouble(lineForOne.getAPointLat()), StringUtils.toDouble(lineForOne.getAPointLng()));
                //转为像素
                Point aPoint = aMap.getProjection().toScreenLocation(latLngMarkerA);

                latLngMarkerZ = null;
                latLngMarkerZ = new LatLng(StringUtils.toDouble(lineForOne.getZPointLat()), StringUtils.toDouble(lineForOne.getZPointLng()));
                //转为像素
                Point zPoint = aMap.getProjection().toScreenLocation(latLngMarkerZ);

                //判断是否在屏幕内，没有则标识为0，后续删除
                if (isContain(aPoint) && isContain(zPoint)) {
                } else {
                    lxResEntities.get(0).getLines().get(j).setAPoint("0");//先临时用0作为无用标识，后面会删掉
                }
            } else {
                lxResEntities.get(0).getLines().get(j).setAPoint("0");//先临时用0作为无用标识，后面会删掉
            }
        }


        //如果没有线在屏幕内，则把这条路由清除掉
        for (int i = 0; i < lxResEntities.size(); i++) {
            LXResNewEntity lx1 = lxResEntities.get(i);
            if(!ToolUtil.isEmpty(lx1.getLines())) {
                Iterator<LXResNewEntity.LinesNewBean> iter = lx1.getLines().iterator();
                while (iter.hasNext()) {
                    LXResNewEntity.LinesNewBean s = iter.next();
                    if (s.getAPoint().equals("0")) {
                        iter.remove();
                    }
                }
            }
        }


        if(!ToolUtil.isEmpty(lxResEntities)) {
            Iterator<LXResNewEntity> iter = lxResEntities.iterator();
            while (iter.hasNext()) {
                LXResNewEntity s = iter.next();
                if (s.getLines() == null || s.getLines().size() < 1) {
                    iter.remove();
                }
            }
        }


        roundSiteList.postValue(lxResEntities);
        Log.d("qqqqq", "Room共在屏幕内" + lxResEntities.size());
    }


    /**
     * 遍历出最近的点，找到最近点所在的路由，并将匹配到的路由存到Room
     *
     * @param lxResEntities
     */
    private LatLng currentLatLng;
    private LatLng resLatLng;
    private double nearestNum = 0;
    private String AORZ = "A";
    private LXResNewEntity mCLXResNewEntity;//当前的路由
    private LXResNewEntity.LinesNewBean mCNearLine;//最近的点

    private void getAndSaveRoute(List<LXResNewEntity> lxResEntities) {
        if (db == null) {
            db = Room.databaseBuilder(mContext.getApplicationContext(), AppDatabase.class, "database-name").build();
        }

        if (!ToolUtil.isEmpty(lxResEntities)) {
            SPUtil spUtil = new SPUtil(mContext, SPUtil.LOCATION_ADRESS);
            String mlat = spUtil.getString(SPUtil.LOCATION_LAT, "0");
            String mlon = spUtil.getString(SPUtil.LOCATION_LON, "0");
            if (StringUtils.isDouble(mlat) && StringUtils.isDouble(mlon)) {
                currentLatLng = new LatLng(Double.parseDouble(mlat), Double.parseDouble(mlon));
            }

            for (int i = 0; i < lxResEntities.size(); i++) {
                LXResNewEntity oneRoute = lxResEntities.get(i);
                if (!ToolUtil.isEmpty(oneRoute.getLines())) {
                    for (int j = 0; j < oneRoute.getLines().size(); j++) {
                        LXResNewEntity.LinesNewBean oneLine = oneRoute.getLines().get(j);
                        resLatLng = null;
                        if (StringUtils.isDouble(oneLine.getAPointLat()) && StringUtils.isDouble(oneLine.getAPointLng())
                                && StringUtils.isDouble(oneLine.getZPointLat()) && StringUtils.isDouble(oneLine.getZPointLng())) {

                            resLatLng = new LatLng(Double.parseDouble(oneLine.getAPointLat()), Double.parseDouble(oneLine.getAPointLng()));
                            double sDistance = convert(AMapUtils.calculateLineDistance(currentLatLng, resLatLng));
                            if(mCLXResNewEntity == null){
                                mCLXResNewEntity = new LXResNewEntity();
                            }

                            if (nearestNum == 0) {
                                AORZ = "A";
                                nearestNum = sDistance;
                          //    mCLXResNewEntity = oneRoute;
                                mCLXResNewEntity.setLines(oneRoute.getLines());
                                mCLXResNewEntity.setAPointUUID(oneRoute.getAPointUUID());
                                mCLXResNewEntity.setPoints(oneRoute.getPoints());
                                mCLXResNewEntity.setRouteName(oneRoute.getRouteName());
                                mCLXResNewEntity.setRouteUUID(oneRoute.getRouteUUID());
                                mCLXResNewEntity.setZPointUUID(oneRoute.getZPointUUID());

                                mCNearLine = oneLine;
                            } else {
                                if (nearestNum > sDistance) {
                                    AORZ = "A";
                                    nearestNum = sDistance;
                                    //mCLXResNewEntity = oneRoute;
                                    mCLXResNewEntity.setLines(oneRoute.getLines());
                                    mCLXResNewEntity.setAPointUUID(oneRoute.getAPointUUID());
                                    mCLXResNewEntity.setPoints(oneRoute.getPoints());
                                    mCLXResNewEntity.setRouteName(oneRoute.getRouteName());
                                    mCLXResNewEntity.setRouteUUID(oneRoute.getRouteUUID());
                                    mCLXResNewEntity.setZPointUUID(oneRoute.getZPointUUID());
                                    mCNearLine = oneLine;
                                }
                            }

                            resLatLng = new LatLng(Double.parseDouble(oneLine.getZPointLat()), Double.parseDouble(oneLine.getZPointLng()));
                            sDistance = convert(AMapUtils.calculateLineDistance(currentLatLng, resLatLng));
                            if (nearestNum == 0) {
                                AORZ = "Z";
                                nearestNum = sDistance;
                                //      mCLXResNewEntity = oneRoute;
                                mCLXResNewEntity.setLines(oneRoute.getLines());
                                mCLXResNewEntity.setAPointUUID(oneRoute.getAPointUUID());
                                mCLXResNewEntity.setPoints(oneRoute.getPoints());
                                mCLXResNewEntity.setRouteName(oneRoute.getRouteName());
                                mCLXResNewEntity.setRouteUUID(oneRoute.getRouteUUID());
                                mCLXResNewEntity.setZPointUUID(oneRoute.getZPointUUID());
                                mCNearLine = oneLine;
                            } else {
                                if (nearestNum > sDistance) {
                                    AORZ = "Z";
                                    nearestNum = sDistance;
                                    //mCLXResNewEntity = oneRoute;
                                    mCLXResNewEntity.setLines(oneRoute.getLines());
                                    mCLXResNewEntity.setAPointUUID(oneRoute.getAPointUUID());
                                    mCLXResNewEntity.setPoints(oneRoute.getPoints());
                                    mCLXResNewEntity.setRouteName(oneRoute.getRouteName());
                                    mCLXResNewEntity.setRouteUUID(oneRoute.getRouteUUID());
                                    mCLXResNewEntity.setZPointUUID(oneRoute.getZPointUUID());
                                    mCNearLine = oneLine;
                                }
                            }
                        }
                    }
                } else {
                    ToastUtil.showToast(mContext, "匹配到的路由无段资源", ToastUtil.TOAST_TYPE_ERRO);
                    return;
                }
            }
        } else {
            return;
        }

        for (int i = 0; i < mCLXResNewEntity.getLines().size(); i++) {
            mCLXResNewEntity.getLines().get(i).setRouteUUID(mCLXResNewEntity.getRouteUUID());
        }
        mCNearLine.setRouteUUID(mCLXResNewEntity.getRouteUUID());

        Log.d("qqqqqqqq", "最近的路由id" + mCLXResNewEntity.getRouteUUID());
        Log.d("qqqqqqqq", "最近的Lines id" + mCNearLine.getTaskUUID());
        SingleRoute.getInstance().setmCLXResNewEntity(mCLXResNewEntity);
        SingleRoute.getInstance().setmCNearLine(mCNearLine);
        SingleRoute.getInstance().setAORZ(AORZ);

        //Log.d("qqqqqqqq","存入后=="+SingleRoute.getInstance().getmCLXResNewEntity().getLines().size());


        long a = db.myDao().insertRoute(mCLXResNewEntity);
        List<Long> b = db.myDao().insertLinesNew(mCLXResNewEntity.getLines());
        Log.d("qqqq", "插入新的路由==" + a);
        Log.d("qqqq", "插入新的段==" + b.toString());


        /*db.myDao().loadRoute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LXResNewEntity>>() {
                    @Override
                    public void accept(List<LXResNewEntity> lxResEntities) throws Exception {
                        Log.d("qqqqqq", "room查询route==" + lxResEntities.size());
                    }
                });
        db.myDao().loadAllLines(mCLXResNewEntity.getRouteUUID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LXResNewEntity.LinesNewBean>>() {
                    @Override
                    public void accept(List<LXResNewEntity.LinesNewBean> linesBeans) throws Exception {
                        Log.d("qqqqqq", "line==" + linesBeans.size());
                    }
                });*/


    }


    private double convert(float value) {
        long l1 = Math.round(value * 100); //四舍五入
        double ret = l1 / 100.0; //注意:使用 100.0 而不是 100
        return ret;
    }


    private boolean isContain(Point cPoint) {
        try {
            //手机的像素
            Point myPoint = Utils.getFourCornersOnly(mActivity);
            if (cPoint.x >= 0 && cPoint.x <= myPoint.x && cPoint.y >= 0 && cPoint.y <= myPoint.y) {
                return true;
            }
            return false;
        }catch (Exception e){

        }
        return false;
    }


    private OnErroListener onCallBackListener;

    public void setOnErroCallback(OnErroListener onItemClickListener) {
        this.onCallBackListener = onItemClickListener;
    }

    public interface OnErroListener {
        void onErro();
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
