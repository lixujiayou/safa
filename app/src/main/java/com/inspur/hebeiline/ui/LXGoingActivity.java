package com.inspur.hebeiline.ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.inspur.hebeiline.R;
import com.inspur.hebeiline.core.SingleRoute;
import com.inspur.hebeiline.entity.LXResEntity;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.LXTestLoginBean;
import com.inspur.hebeiline.entity.LXWaitListEntity;
import com.inspur.hebeiline.entity.LineResultEntity;
import com.inspur.hebeiline.entity.TMLineMainEntity;
import com.inspur.hebeiline.entity.eventbean.MyEvent;
import com.inspur.hebeiline.entity.tmline.LocusPoint;
import com.inspur.hebeiline.entity.tmline.MyWayBean;
import com.inspur.hebeiline.entity.tmline.RouteInfoBean;
import com.inspur.hebeiline.model.GetRegionDataForNewModel;
import com.inspur.hebeiline.model.GetRegionDataModel;
import com.inspur.hebeiline.model.GetNearResModel;
import com.inspur.hebeiline.model.LocationModel;
import com.inspur.hebeiline.model.LoginModel;
import com.inspur.hebeiline.model.SubmitLineModel;
import com.inspur.hebeiline.room_.AppDatabase;
import com.inspur.hebeiline.utils.base.BaseActivity;
import com.inspur.hebeiline.utils.tools.SPUtil;
import com.inspur.hebeiline.utils.tools.StringUtils;
import com.inspur.hebeiline.utils.tools.ToastUtil;
import com.inspur.hebeiline.utils.tools.ToolUtil;
import com.inspur.hebeiline.utils.tools.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixu on 2018/3/14.
 * 传输线路
 */

public class LXGoingActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    private int TROUBLE_REQUESTCODE = 16;
    private int SUBMIT_REQUESTCODE = 15;

    private SubmitLineModel submitLineModel;

    private MapView mMapView = null;
    private AMap aMap;
    //private CurrentLocationMapModel cLocationModel;
    private LocationModel cLocationModel;
    private Context mContext;
    private ImageView ivLocation;
    private Button zsl_go, zsl_error_report;
    private ImageView ivWaitList;
    private ImageView iv_mine;


    //第一次加载则放弃三次加载规则
    private boolean isMyFirstLoad = true;

    //海量点
    private MultiPointOverlayOptions overlayOptions;
    private MultiPointOverlay multiPointOverlay;
    private List<MultiPointItem> list;

    //查询地图区域资源
    private GetRegionDataForNewModel getRegionDataModel;
    private GetRegionDataModel getDataModel;


    private List<Polyline> polylineList; //所有的线
    private List<Marker> markerList = new ArrayList<>(); //所有的点
    private List<LXResNewEntity.LinesNewBean> pointList = new ArrayList<>();

    //toolbar
    private TextView tvTitle;
    private ImageView ivCancle;
    private ImageView mToolbarCicle;
    private ObjectAnimator rotation;
    private double mCircumference = 500;//方圆多少米
    private LatLng currentLatLng;//当前位置  最新的经纬度
    private Marker currentMarker;//当前Marker
    private Circle currentCircle;//当前Marker  方圆的圈


    private MarkerOptions markerOption = new MarkerOptions();
    private CircleOptions circleOptions = new CircleOptions();


    private TextView tvMaxHint;
    private Button btRest;

    private AppDatabase mDb;

    private int manyPos = 0;//几次查询一次数据

    /**
     * 开始/结束状态
     */
    private boolean isStart = false;


    private int autoLocation = 0;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private AppDatabase db;


    //路由ID
    private String taskUUID;//任务ID
    private boolean isFirstLocation = true;
    private int miss = 0;
    private Chronometer chronometer = null;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tmline_main);

        mContext = LXGoingActivity.this;
        //获取地图控件引用
        mMapView = findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);


        if (db == null) {
            db = Room.databaseBuilder(mContext.getApplicationContext(), AppDatabase.class, "database-name").build();
        }

        //删掉之前跑的段
        db.myDao().loadAllWays()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<MyWayBean>>() {
                    @Override
                    public void accept(List<MyWayBean> myWayBeans) throws Exception {
                        if (!ToolUtil.isEmpty(myWayBeans)) {
                            int a = db.myDao().deleteAllWays(myWayBeans);
                        }
                    }
                });

        //获取Chronometer对象
        chronometer = (Chronometer) super.findViewById(R.id.chronometer);
        chronometer.setFormat("计时：%s");

        chronometer.start();
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override
            public void onChronometerTick(Chronometer ch) {
                miss++;
                ch.setText(FormatMiss());
            }
        });


    }


    @Override
    public void initViews() {
        Intent gIntent = getIntent();
        taskUUID = gIntent.getExtras().getString("taskid");


        btRest = findViewById(R.id.bt_rest);
        tvTitle = findViewById(R.id.toolbar_title);
        ivLocation = findViewById(R.id.iv_location);
        ivCancle = findViewById(R.id.iv_cancle);
        mToolbarCicle = findViewById(R.id.toolbar_cicle);
        iv_mine = findViewById(R.id.iv_mine);
        iv_mine.setVisibility(View.GONE);
        zsl_go = findViewById(R.id.zsl_go);
        zsl_go.setVisibility(View.VISIBLE);
        zsl_go.setText("开始");
        zsl_error_report = findViewById(R.id.zsl_error_report);
        tvMaxHint = findViewById(R.id.tv_max_hint);
        ivWaitList = findViewById(R.id.iv_wait_list);
        ivWaitList.setVisibility(View.GONE);
        tvTitle.setText("线路巡检");
        mToolbar.setTitle("");
        ivCancle.setVisibility(View.VISIBLE);


        ivCancle.setOnClickListener(this);
        iv_mine.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
        zsl_go.setOnClickListener(this);
        ivWaitList.setOnClickListener(this);
        btRest.setOnClickListener(this);
        zsl_error_report.setOnClickListener(this);
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
            mUiSettings.setScaleControlsEnabled(true);
        }


        getDataModel = ViewModelProviders.of(this).get(GetRegionDataModel.class);
        getDataModel.getCurrentData(this, aMap, mMapView).observe(this, new Observer<List<LXResEntity>>() {
            @Override
            public void onChanged(@Nullable List<LXResEntity> lxResEntities) {
                if (!ToolUtil.isEmpty(lxResEntities)) {
                    initLineation(lxResEntities);
                } else {
                    Log.d("qqqqqq", "没查到数据");
                    //ToastUtil.showToast(mContext,"没有查询到数据",ToastUtil.TOAST_TYPE_WARNING);
                }
            }
        });

        SPUtil spUtil = new SPUtil(mContext, SPUtil.USER);
        String lid = spUtil.getString("county_uid","");
        getDataModel.getNearRes(mRequestClient, lid, "2", null);

    }


    @Override
    public void initData() {
        cicleAnime();


        //提交数据回调
        submitLineModel = ViewModelProviders.of(this).get(SubmitLineModel.class);
        submitLineModel.getCurrentData(this).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ToastUtil.showToast(LXGoingActivity.this, "提交成功", ToastUtil.TOAST_TYPE_WARNING);
                finish();
            }
        });


        /**
         * 轨迹点返回
         */
        cLocationModel = ViewModelProviders.of(this).get(LocationModel.class);
//        cLocationModel.getlocationListMutableLiveData().observe(this, new Observer<List<LocusPoint>>() {
//            @Override
//            public void onChanged(@Nullable List<LocusPoint> recordLocationEntities) {
//                //得到记录在本地的点资源
//                //删掉之前跑的段
//                db.myDao().loadAllWays()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(Schedulers.io())
//                        .subscribe(new Consumer<List<MyWayBean>>() {
//                            @Override
//                            public void accept(List<MyWayBean> myWayBeans) throws Exception {
//                                if (!ToolUtil.isEmpty(myWayBeans)) {
//                                    //查出来了，开始提交
//                                    String uuids = "";
//                                    for(int i = 0;i < myWayBeans.size();i++){
//                                        if(i == 0){
//                                            uuids = myWayBeans.get(i).getLineId();
//                                        }else{
//                                            uuids += ","+myWayBeans.get(i).getLineId();
//                                        }
//                                    }
//                                    LXResNewEntity aa = SingleRoute.getInstance().getmCLXResNewEntity();
////                                    submitLineModel.submit(mRequestClient,SingleRoute.getInstance().getLength(),uuids,aa.getRouteUUID(),aa.getRouteName(),taskUUID);
//                                }
//                            }
//                        });
//
//            }
//        });

        /**
         * 手动点击当前经纬度
         */
        cLocationModel.getlocationMutableLiveData().observe(this, new Observer<LatLng>() {
            @Override
            public void onChanged(@Nullable LatLng latLng) {
                if (latLng != null) {
                    if (currentMarker == null) {
                        markerOption.position(latLng);
                        markerOption.draggable(false);//设置Marker不可拖动
                        markerOption.title("当前位置");//.snippet("西安市：34.341568, 108.940174");
                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(mContext.getResources(), R.drawable.icon_current)));
                        currentMarker = aMap.addMarker(markerOption);
                        getRegionDataModel.getNearRes(mRequestClient, null, "2", taskUUID,0,0);

                    } else {
                        markerOption.position(currentLatLng);
                        currentMarker.setMarkerOptions(markerOption);
                        if (autoLocation > 4) {
//                            getNearResModel.getNearRes(mRequestClient
//                                    , ""
//                                    , Utils.getFourCorners(LXGoingActivity.this, aMap));
                            autoLocation = 0;
                        } else {
                            autoLocation += 1;
                        }
                    }

                    if (currentCircle == null) {
                        circleOptions.center(latLng);
                        circleOptions.radius(mCircumference);
                        circleOptions.strokeWidth(1);
                        circleOptions.strokeColor(Color.argb(50, 1, 1, 1));
                        circleOptions.fillColor(Color.argb(50, 1, 1, 1));
                        currentCircle = aMap.addCircle(circleOptions);
                    } else {
                        currentCircle.setCenter(latLng);
                    }


                    if (currentLatLng == null) {
                        ////参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)

                        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 15, 0, 0));
                        aMap.animateCamera(mCameraUpdate, 1000, new AMap.CancelableCallback() {
                            @Override
                            public void onFinish() {

                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                    //记录当前最新的点
                    currentLatLng = latLng;
                }
            }
        });


        /**
         * 每5秒更新页面
         */
        cLocationModel.setOnPointCallback(new LocationModel.OnPointBackListener() {
            @Override
            public void onCallBack(LatLng latLng) {
                if (currentMarker == null) {
                    markerOption.position(latLng);
                    markerOption.draggable(false);//设置Marker不可拖动
                    markerOption.title("当前位置");//.snippet("西安市：34.341568, 108.940174");
                    markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(mContext.getResources(), R.drawable.icon_current)));
                    currentMarker = aMap.addMarker(markerOption);
                } else {
                    markerOption.position(latLng);
                    currentMarker.setMarkerOptions(markerOption);

                }

                if (currentCircle == null) {
                    circleOptions.center(latLng);
                    circleOptions.radius(mCircumference);
                    circleOptions.strokeWidth(1);
                    circleOptions.strokeColor(Color.argb(50, 1, 1, 1));
                    circleOptions.fillColor(Color.argb(50, 1, 1, 1));
                    currentCircle = aMap.addCircle(circleOptions);
                } else {
                    currentCircle.setCenter(latLng);
                }
                //记录当前最新的点
                currentLatLng = latLng;
//                CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(currentLatLng, 20, 30, 0));
//                aMap.animateCamera(mCameraUpdate);
                    getRegionDataModel.getNearRes(mRequestClient, null, "2", taskUUID,0,0);


            }

            @Override
            public void onMAXDISTANCECallBack(String hint) {
                tvMaxHint.setText(hint);
                maxDistanceHint();
            }

            @Override
            public void onDeleteRouteCallBack(int routeID) {
                String json = "{\"routeId\":" + routeID + "}";
                // deleteRouteModel.deleteRouteTask(mRequestClient,json);
            }

            @Override
            public void onUpdateLine(String lineUUID) {
                //在线程里面  重新请求下数据就好
                getRegionDataModel.getNearRes(mRequestClient, null, "2", taskUUID,0,0);
            }
        });


        /**
         * 得到地图资源
         */

        getRegionDataModel = ViewModelProviders.of(this).get(GetRegionDataForNewModel.class);
        getRegionDataModel.getCurrentData(this, aMap,mMapView).observe(this, new Observer<List<LXResNewEntity>>() {
            @Override
            public void onChanged(@Nullable List<LXResNewEntity> lxResEntities) {
                if (!ToolUtil.isEmpty(lxResEntities)) {

                    if(!isMyFirstLoad) {
                        manyPos -= 1;
                        if (manyPos <= 0) {
                            manyPos = 3;
                            initLineation2(lxResEntities);
                        }
                    }else{
                        isMyFirstLoad = false;
                        initLineation2(lxResEntities);
                    }



                    if (isFirstLocation) {
                        isFirstLocation = false;
                        if (ToolUtil.isEmpty(SingleRoute.getInstance().getmCBothLines())) {
//                            calculateBothPoint();
                        }
//                        cLocationModel.getCurrentLocation(mContext, false, aMap, null, taskUUID);
                    }

                } else {
                    //ToastUtil.showToast(mContext,"没有查询到数据",ToastUtil.TOAST_TYPE_WARNING);
                }
            }
        });

        mToolbar.setOnMenuItemClickListener(this);

        /**
         * 地图滑动监听
         */
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {


                    if (cameraPosition.zoom > 14) {
//                        if (!isStart) {
                            getRegionDataModel.getNearRes(mRequestClient, null, "2", taskUUID,0,0);
//                        }
                    }

            }
        });


        //开始
        setDeliveryStatus("end");
        //交割结束，获取所有数据并前去提交

        //获取所有的数据
        //cLocationModel.getLocationListLiveData();

        cLocationModel.getCurrentLocation(mContext, true, null, null, "");
    }


    /**
     * 计算两端点
     */
    private LXResNewEntity calculateres;
    private LXResNewEntity.LinesNewBean calculatelines;
    private List<LXResNewEntity.LinesNewBean> bothList;

    private void calculateBothPoint() {
        calculateres = SingleRoute.getInstance().getmCLXResNewEntity();
        calculatelines = SingleRoute.getInstance().getmCNearLine();
        taskUUID = calculatelines.getTaskUUID();
        bothList = new ArrayList<>();

        if (mDb == null) {
            mDb = Room.databaseBuilder(mContext.getApplicationContext(), AppDatabase.class, "database-name").build();
        }

        mDb.myDao().loadAllLinesNew(calculateres.getRouteUUID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LXResNewEntity.LinesNewBean>>() {
                    @Override
                    public void accept(List<LXResNewEntity.LinesNewBean> linesBeans) throws Exception {
                        calculateres.setLines(linesBeans);
                        if (calculateres != null && calculatelines != null) {
                            if (!ToolUtil.isEmpty(calculateres.getLines())) {
                                for (int i = 0; i < calculateres.getLines().size(); i++) {
                                    LXResNewEntity.LinesNewBean cuRes = calculateres.getLines().get(i);
                                    if (!StringUtils.isEmpty(cuRes.getStatus()) && cuRes.getStatus().equals("0")) {

                                        String alat, alng;
                                        String zlat, zlng;
                                        String mlat, mlng;

                                        alat = cuRes.getAPointLat();
                                        alng = cuRes.getAPointLng();
                                        zlat = cuRes.getZPointLat();
                                        zlng = cuRes.getZPointLng();

                                        if (!StringUtils.isEmpty(SingleRoute.getInstance().getAORZ()) && SingleRoute.getInstance().getAORZ().equals("A")) {
                                            mlat = calculatelines.getAPointLat();
                                            mlng = calculatelines.getAPointLng();
                                        } else {
                                            mlat = calculatelines.getZPointLat();
                                            mlng = calculatelines.getZPointLng();
                                        }

                                        if (mlat.equals(alat) && mlng.equals(alng)) {
                                            bothList.add(cuRes);
                                        } else if (mlat.equals(zlat) && mlng.equals(zlng)) {
                                            bothList.add(cuRes);
                                        } else {
                                        }
                                    }
                                }
                            } else {
                            }
                        } else {
                        }
                        SingleRoute.getInstance().setmCBothLines(bothList);

                    }
                });
    }

    private double convert(float value) {
        long l1 = Math.round(value * 100); //四舍五入
        double ret = l1 / 100.0; //注意:使用 100.0 而不是 100
        return ret;
    }

    /**
     * 计算匹配率
     */
    private int matchingRatio = 0;

    private void calculateMatchingRatio(List<LocusPoint> recordLocationEntities, String length) {

      /*double aLat;
        double aLng;
        double zLat;
        double zLng;
        if(StringUtils.isDouble(lxWaitListEntity.getaPointLat()) && StringUtils.isDouble(lxWaitListEntity.getaPointLng()) && StringUtils.isDouble(lxWaitListEntity.getzPointLat()) && StringUtils.isDouble(lxWaitListEntity.getzPointLng()) ){
            aLat = Double.parseDouble(lxWaitListEntity.getaPointLat());
            aLng = Double.parseDouble(lxWaitListEntity.getaPointLng());
            zLat = Double.parseDouble(lxWaitListEntity.getzPointLat());
            zLng = Double.parseDouble(lxWaitListEntity.getzPointLng());
        }else{
            return;
        }
        LatLng currentLocation;
        LatLng lastLocation;
        LatLng lastLocation2;
        boolean isOne = false;
        boolean isTwo = false;

        for(int i = 0;i < recordLocationEntities.size();i++){
            lastLocation = null;
            currentLocation = null;
            lastLocation2 = null;

            currentLocation = new LatLng(recordLocationEntities.get(i).getLatitude(), recordLocationEntities.get(i).getLongitude());
            lastLocation = new LatLng(aLat, aLng);
            lastLocation2 = new LatLng(zLat, zLng);

            double sDistance = AMapUtils.calculateLineDistance(lastLocation, currentLocation);
            double sDistance2 = AMapUtils.calculateLineDistance(lastLocation2, currentLocation);

            if(sDistance < 80){
                isOne = true;
            }else if(sDistance2 < 80){
                isTwo = true;
            }
        }

        if(isOne && isTwo){
            matchingRatio = 100;
            submitLineModel.submit(mRequestClient,length,uuid,routeID,lxWaitListEntity.getRouteName(),taskUUID);
            ToastUtil.showToast(LXGoingActivity.this,"巡检成功，正在提交",ToastUtil.TOAST_TYPE_WARNING);

        }else if(isOne || isTwo){
            matchingRatio = 50;
            ToastUtil.showToast(LXGoingActivity.this,"匹配率为50,巡检失败",ToastUtil.TOAST_TYPE_WARNING);
        }else{
            matchingRatio = 0;
            ToastUtil.showToast(LXGoingActivity.this,"匹配率为0,巡检失败",ToastUtil.TOAST_TYPE_WARNING);
        }
       */
    }

    private Vibrator vibrator;

    private void maxDistanceHint() {

        tvMaxHint.setVisibility(View.VISIBLE);
        //震动
        vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
        // 等待3秒，震动3秒，从第0个索引开始，一直循环  
        vibrator.vibrate(new long[]{3000, 3000}, 0);
        //响铃
        playSound(LXGoingActivity.this);
    }

    /**
     * 响铃提示
     */
    private MediaPlayer mMediaPlayer;

    public void playSound(final Context context) {

        // 使用来电铃声的铃声路径
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        // 如果为空，才构造，不为空，说明之前有构造过

        if (mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.setLooping(true); //循环播放
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    private void closePlayAndVibrator() {
        tvMaxHint.setVisibility(View.GONE);
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                // 要释放资源，不然会打开很多个MediaPlayer
                mMediaPlayer.release();
            }
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }


    /**
     * 初始化线点
     *
     * @param tmLineMainEntities
     */
   /* private void initLineation(List<LXResNewEntity> tmLineMainEntities) {
        if (ToolUtil.isEmpty(tmLineMainEntities)) {
            return;
        }

        pointList.clear();
        //删掉地图的旧数据
        if (!ToolUtil.isEmpty(polylineList)) {
            for (int i = 0; i < polylineList.size(); i++) {
                if (polylineList.get(i) != null) {
                    polylineList.get(i).remove();
                }
            }
            polylineList.clear();
        }

        if (!ToolUtil.isEmpty(markerList)) {
            for (Marker marker1 : markerList) {
                marker1.remove();
            }
            markerList.clear();
        }

        List<LatLng> latLngs = new ArrayList<>();
        for (int i = 0; i < tmLineMainEntities.size(); i++) {
            LXResNewEntity oneTmLine = tmLineMainEntities.get(i);
            if (oneTmLine != null && !ToolUtil.isEmpty(oneTmLine.getLines())) {
                for (int j = 0; j < oneTmLine.getLines().size(); j++) {
                    LXResNewEntity.LinesNewBean twoLine = oneTmLine.getLines().get(j);
                    if (twoLine == null) {
                        continue;
                    }
                    if (!StringUtils.isDouble(twoLine.getAPointLat())) {
                        continue;
                    }
                    if (!StringUtils.isDouble(twoLine.getAPointLng())) {
                        continue;
                    }
                    if (!StringUtils.isDouble(twoLine.getZPointLat())) {
                        continue;
                    }
                    if (!StringUtils.isDouble(twoLine.getZPointLng())) {
                        continue;
                    }

                    *//**
                     * 画start的点
                     *//*
                    LatLng lalnStart = null;
                    MarkerOptions startMarker = null;
                    if (twoLine != null) {
                        lalnStart = new LatLng(StringUtils.toDouble(twoLine.getAPointLat()), StringUtils.toDouble(twoLine.getAPointLng()));

//                if (oneTmLine.getStart().getIsPass().equals("1")) {
//                    startMarker = new MarkerOptions().position(lalnStart).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                            .decodeResource(getResources(), R.drawable.icon_marker_alreay))).title(oneTmLine.getStart().getResourceName());
//                } else {
//                    startMarker = new MarkerOptions().position(lalnStart).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                            .decodeResource(getResources(), R.drawable.icon_marker_default))).title(oneTmLine.getStart().getResourceName());
//                }

                        startMarker = new MarkerOptions().position(lalnStart).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(), R.drawable.icon_marker_default))).title(twoLine.getName());

                        markerList.add(aMap.addMarker(startMarker));
                    }


                    pointList.add(twoLine);

                    *//**
                     * 画end的点
                     *//*
                    MarkerOptions endMarker;
                    if (twoLine == null) {
                        continue;
                    }
                    LatLng lalnEnd = new LatLng(StringUtils.toDouble(twoLine.getZPointLat()), StringUtils.toDouble(twoLine.getZPointLng()));

                    endMarker = new MarkerOptions().position(lalnEnd).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.drawable.icon_marker_default))).title(twoLine.getName());

                    markerList.add(aMap.addMarker(endMarker));


                    if (ToolUtil.isEmpty(polylineList)) {
                        polylineList = new ArrayList<>();
                    }


                    latLngs.add(lalnStart);
                    latLngs.add(lalnEnd);
                    int colorMy;
                    //未巡检
                    if (!StringUtils.isEmpty(twoLine.getStatus()) && twoLine.getStatus().equals("0")) {
                        colorMy = Color.argb(255, 225, 0, 30);
                        //已巡检
                    } else {
                        colorMy = Color.argb(255, 28, 137, 30);
                    }

                    Polyline polylinem = aMap.addPolyline(new PolylineOptions().
                            addAll(latLngs).width(8).color(colorMy));

                    polylineList.add(polylinem);
                    latLngs.clear();
                }
            }
        }
    }*/
    private void initLineation(List<LXResEntity> tmLineMainEntities) {
        if (ToolUtil.isEmpty(tmLineMainEntities) || ToolUtil.isEmpty(tmLineMainEntities.get(0).getLines())) {
            return;
        }

        //删掉地图的旧数据
        if (!ToolUtil.isEmpty(polylineList)) {
            for (int i = 0; i < polylineList.size(); i++) {
                if (polylineList.get(i) != null) {
                    polylineList.get(i).remove();
                }
            }
            polylineList.clear();
        }

        if (!ToolUtil.isEmpty(markerList)) {
            for (Marker marker1 : markerList) {
                marker1.remove();
            }
            markerList.clear();
        }
        if (multiPointOverlay != null) {
            multiPointOverlay.remove();
        }
        if (!ToolUtil.isEmpty(list)) {
            list.clear();
        }
        showProgressDialog(mContext, "正在初始化地图...");
        LXResEntity.LinesBean[] ls = new LXResEntity.LinesBean[tmLineMainEntities.get(0).getLines().size()];
        for (int i = 0; i < tmLineMainEntities.get(0).getLines().size(); i++) {
            ls[i] = tmLineMainEntities.get(0).getLines().get(i);
        }
        /*Observable.fromArray(ls)
                .concatMap(new Function<LXResEntity.LinesBean, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(LXResEntity.LinesBean linesBean) throws Exception {
                        if (linesBean == null || !StringUtils.isDouble(linesBean.getAPointLat()) || !StringUtils.isDouble(linesBean.getAPointLng()) || !StringUtils.isDouble(linesBean.getZPointLat()) || !StringUtils.isDouble(linesBean.getZPointLng())) {
                            return Observable.just("no");
                        }

                        *//*
         * 添加start的点
         *//*
                        LatLng lalnStart = null;
                        if (linesBean != null) {
                            lalnStart = new LatLng(StringUtils.toDouble(linesBean.getAPointLat()), StringUtils.toDouble(linesBean.getAPointLng()));
                        }

                        *//*
         *
         * 添加end的点
         *//*
                        LatLng lalnEnd = new LatLng(StringUtils.toDouble(linesBean.getZPointLat()), StringUtils.toDouble(linesBean.getZPointLng()));

                        if (ToolUtil.isEmpty(polylineList)) {
                            polylineList = new ArrayList<>();
                        }

                        latLngs.add(lalnStart);
                        latLngs.add(lalnEnd);


                        //未巡检
                        if (!StringUtils.isEmpty(linesBean.getStatus()) && linesBean.getStatus().equals("0")) {
                            colorMy = Color.argb(255, 225, 0, 30);
                            //已巡检
                        } else {
                            colorMy = Color.argb(255, 28, 137, 30);
                        }

                        //.zIndex(20)
                        Polyline polylinem = aMap.addPolyline(new PolylineOptions().
                                addAll(latLngs).width(2).color(colorMy));
                        polylineList.add(polylinem);

                        //latLngs.clear();

                        //  Log.d("qqqqqqq","花了多少线"+);

                        return Observable.just("ok");

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String linesBean) throws Exception {

                    }
                });*/


        Observable.fromArray(tmLineMainEntities.get(0).getLines())
                .concatMap(new Function<List<LXResEntity.LinesBean>, ObservableSource<List<MultiPointItem>>>() {
                    @Override
                    public ObservableSource<List<MultiPointItem>> apply(List<LXResEntity.LinesBean> linesBeans) throws Exception {
                        overlayOptions = new MultiPointOverlayOptions();
                        overlayOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_marker_default)));//设置图标
                        overlayOptions.anchor(0.5f, 0.5f); //设置锚点
                        multiPointOverlay = aMap.addMultiPointOverlay(overlayOptions);
                        list = new ArrayList<MultiPointItem>();

                        for (int i = 0; i < linesBeans.size(); i++) {

                            LXResEntity.LinesBean linesBean = linesBeans.get(i);

                            if (linesBean == null || !StringUtils.isDouble(linesBean.getAPointLat()) || !StringUtils.isDouble(linesBean.getAPointLng()) || !StringUtils.isDouble(linesBean.getZPointLat()) || !StringUtils.isDouble(linesBean.getZPointLng())) {
                                continue;
                            }

                            /**
                             * 添加start的点
                             **/
                            LatLng lalnStart = null;
                            if (linesBean != null) {
                                lalnStart = new LatLng(StringUtils.toDouble(linesBean.getAPointLat()), StringUtils.toDouble(linesBean.getAPointLng()));
                                MultiPointItem multiPointItem = new MultiPointItem(lalnStart);
                                list.add(multiPointItem);
                            }


                            /**
                             * 添加end的点
                             **/
                            LatLng lalnEnd = new LatLng(StringUtils.toDouble(linesBean.getZPointLat()), StringUtils.toDouble(linesBean.getZPointLng()));
                            MultiPointItem multiPointItem = new MultiPointItem(lalnEnd);
                            list.add(multiPointItem);

                            if (ToolUtil.isEmpty(polylineList)) {
                                polylineList = new ArrayList<>();
                            }
                        }

                        multiPointOverlay.setItems(list);

                        return Observable.just(list);

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MultiPointItem>>() {
                    @Override
                    public void accept(List<MultiPointItem> linesBean) throws Exception {
                        dismissProgressDialog();
                        Log.d("qqqqq", "点画完了");

                    }
                });
    }

    private int myAddNum;
    private boolean isPolLineOk = true;//线是否完成
    private List<LatLng> latLngs = new ArrayList<>();
    private int colorMy;
    private void initLineation2(List<LXResNewEntity> tmLineMainEntities) {
        if (ToolUtil.isEmpty(tmLineMainEntities) || ToolUtil.isEmpty(tmLineMainEntities.get(0).getLines())) {
            return;
        }

        if (!isPolLineOk) {
            Log.d("qqqqqq", "上次的线还没有画完，继续上次");
            return;
        }
        isPolLineOk = false;
        Log.d("qqqqqq", "开始划线");

        //删掉地图的旧数据
        if (!ToolUtil.isEmpty(polylineList)) {
            for (int i = 0; i < polylineList.size(); i++) {
                if (polylineList.get(i) != null) {
                    polylineList.get(i).remove();
                }
            }
            polylineList.clear();
        }

        LXResNewEntity.LinesNewBean[] ls = new LXResNewEntity.LinesNewBean[tmLineMainEntities.get(0).getLines().size()];
        for (int i = 0; i < tmLineMainEntities.get(0).getLines().size(); i++) {
            ls[i] = tmLineMainEntities.get(0).getLines().get(i);
        }
        myAddNum = ls.length;
        Observable.fromArray(ls)
                .concatMap(new Function<LXResNewEntity.LinesNewBean, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(LXResNewEntity.LinesNewBean linesBean) throws Exception {

                        if (linesBean == null || !StringUtils.isDouble(linesBean.getAPointLat()) || !StringUtils.isDouble(linesBean.getAPointLng()) || !StringUtils.isDouble(linesBean.getZPointLat()) || !StringUtils.isDouble(linesBean.getZPointLng())) {
                            return Observable.just("no");
                        }
                        LatLng lalnStart = null;
                        if (linesBean != null) {
                            lalnStart = new LatLng(StringUtils.toDouble(linesBean.getAPointLat()), StringUtils.toDouble(linesBean.getAPointLng()));
                        }

                        LatLng lalnEnd = new LatLng(StringUtils.toDouble(linesBean.getZPointLat()), StringUtils.toDouble(linesBean.getZPointLng()));

                        if (ToolUtil.isEmpty(polylineList)) {
                            polylineList = new ArrayList<>();
                        }

                        latLngs.add(lalnStart);
                        latLngs.add(lalnEnd);


                        //未巡检
                        if (!StringUtils.isEmpty(linesBean.getStatus()) && linesBean.getStatus().equals("0")) {
                            colorMy = Color.argb(255, 225, 0, 30);
                            //已巡检
                        } else {
                            colorMy = Color.argb(255, 28, 137, 30);
                        }

                        //.zIndex(20)
                        Polyline polylinem = aMap.addPolyline(new PolylineOptions().
                                addAll(latLngs).width(8).color(colorMy));
                        polylineList.add(polylinem);

                        latLngs.clear();

                        //  Log.d("qqqqqqq","花了多少线"+);
                        myAddNum -= 1;
                        return Observable.just("ok");

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String linesBean) throws Exception {
                        if (myAddNum <= 0) {
                            isPolLineOk = true;
                            Log.d("qqqqq", "线完成");
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i = view.getId();
        if (i == R.id.iv_cancle) {
            goBack();

        } else if (i == R.id.iv_mine) {
            Intent mineIntent = new Intent(LXGoingActivity.this, LXMineActivity.class);
            startActivity(mineIntent);

        } else if (i == R.id.iv_location) {
            cLocationModel.getCurrentLocation(mContext, true, null, null, "");


        } else if (i == R.id.zsl_go) {//                if (!isDeliveryStart) {
//                    setDeliveryStatus("start");
//                    showPointDialog("start");
//                } else {

            if (isStart) {
                isStart = false;
//                zsl_go.setText("开始");
                showPointDialog("end");
            } else {
                btRest.setVisibility(View.VISIBLE);
                isStart = true;
                zsl_go.setText("结束");
                cLocationModel.getCurrentLocation(mContext, false, aMap, null, taskUUID);
            }


//                }

        } else if (i == R.id.iv_wait_list) {
            Intent gIntent = new Intent(LXGoingActivity.this, LXWaitListActivity.class);
            startActivity(gIntent);

        } else if (i == R.id.bt_rest) {
            showRestDialog();


        }
    }

    /**
     * @param statusDelivery default:初始化状态  start已经开始
     */
    private void setDeliveryStatus(String statusDelivery) {
        if (StringUtils.isEmpty(statusDelivery)) {
            return;
        }
        if (statusDelivery.equals("default")) {
            zsl_go.setText("开始");
        } else if (statusDelivery.equals("start")) {
            zsl_go.setText("结束");
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime;

    private void goBack() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showToast(mContext, "再按一次退出传输线路交割", ToastUtil.TOAST_TYPE_NOMAL);
            exitTime = System.currentTimeMillis();
        } else {
            closePlayAndVibrator();
            //cLocationModel.stopLocation();
            finish();
            //System.exit(0);
        }
    }


    private void cicleAnime() {
        rotation = ObjectAnimator.ofFloat(mToolbarCicle, "rotation", 0f, 359f);//最好是0f到359f，0f和360f的位置是重复的
        rotation.setRepeatCount(ObjectAnimator.INFINITE);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setDuration(1000);
        rotation.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyEvent event) {
        if (this.isSHOW) {
            if (event.getType() == event.OKK) {
                mToolbarCicle.setVisibility(View.VISIBLE);
            } else {
                mToolbarCicle.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int i = menuItem.getItemId();
        if (i == R.id.action_more) {
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        cLocationModel.stopLocation();
        SingleRoute.getInstance().setCurrentLine(new ArrayList<LXResNewEntity.LinesNewBean>());
        SingleRoute.getInstance().setMateLatLng(null);
        mMapView.onDestroy();
        if (rotation != null && rotation.isStarted()) {
            rotation.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    /**
     * 开始交割弹窗
     * typeDialog : start 起点
     * typeDialog : end 终点
     */
    private void showPointDialog(final String typeDialog) {
        final SweetAlertDialog pDialogSuccess;
        try {
            pDialogSuccess = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
            if (!StringUtils.isEmpty(typeDialog) && typeDialog.equals("end")) {
                pDialogSuccess.setTitleText("确定结束巡检?");
            } else {
                pDialogSuccess.setTitleText("确定开始巡检?");
            }
            pDialogSuccess.setCancelable(true);
            pDialogSuccess.setConfirmText("确定");
            pDialogSuccess.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    pDialogSuccess.dismiss();
                    btRest.setVisibility(View.GONE);
                    db.myDao().loadAllLinesNew()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<List<LXResNewEntity.LinesNewBean>>() {
                                @Override
                                public void accept(List<LXResNewEntity.LinesNewBean> linesBeans) {
                                    String uuids = "";
                                    if(!ToolUtil.isEmpty(linesBeans)) {
                                        List<LineResultEntity> listMy = new ArrayList<>();

                                        for(int i = 0;i < linesBeans.size();i++){




                                            if(!StringUtils.isEmpty(linesBeans.get(i).getStatus()) && linesBeans.get(i).getStatus().equals("1")){

                                                LineResultEntity lineResultEntity = new LineResultEntity();
                                                lineResultEntity.setLineUUID(linesBeans.get(i).getUuid());
                                                lineResultEntity.setRouteUUID(linesBeans.get(i).getRouteUUID());
                                                lineResultEntity.setStatus("1");
                                                lineResultEntity.setUserId("admin");
                                                lineResultEntity.setTaskUUID(taskUUID);
                                                listMy.add(lineResultEntity);
//                                                if (i == 0) {
//                                                    uuids = linesBeans.get(i).getUuid();
//                                                } else {
//                                                    uuids += "," + linesBeans.get(i).getUuid();
//                                                }

                                            }
                                        }




                                        cLocationModel.stopLocation();
                                        isStart = false;
                                        zsl_go.setText("开始");
//                                        LXResNewEntity aa = SingleRoute.getInstance().getmCLXResNewEntity();
                                        submitLineModel.submit(mRequestClient,listMy);
                                    }
                                }
                            });
//                    cLocationModel.getlocationListMutableLiveData();

                }
            });
            pDialogSuccess.setCancelText("取消");
            pDialogSuccess.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                }
            });
            pDialogSuccess.show();
        } catch (Exception e) {
        }
    }


    /**
     * 开始交割弹窗
     * typeDialog : start 起点
     * typeDialog : end 终点
     */
    private void showRestDialog() {
        final SweetAlertDialog pDialogSuccess;
        pDialogSuccess = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
        pDialogSuccess.setTitleText("确定重新匹配线路巡检?");
        pDialogSuccess.setContentText("确定后系统会视为放弃当前段的巡检，重新匹配.");
        pDialogSuccess.setCancelable(true);
        pDialogSuccess.setConfirmText("确定");
        pDialogSuccess.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialogSuccess.dismiss();

               cLocationModel.changeRest();

            }
        });
        pDialogSuccess.setCancelText("取消");
        pDialogSuccess.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        pDialogSuccess.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 777) {
            setResult(777);
            finish();
            // cLocationModel.TOTAL_DISTANCE = 0;
            //closePlayAndVibrator();
            // cLocationModel.stopLocation();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 将秒转化成小时分钟秒
    public String FormatMiss() {
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }

    /**
     * 将String类型的时间转换成long,如：12:01:08
     *
     * @return long类型的时间
     */
    protected long convertStrTimeToLong() {
        chronometer.stop();//停止计时
        String strTime = chronometer.getText().toString();

        String[] timeArry = strTime.split(":");
        long longTime = 0;
        if (timeArry.length == 2) {//如果时间是MM:SS格式
            longTime = Integer.parseInt(timeArry[0]) * 1000 * 60 + Integer.parseInt(timeArry[1]) * 1000;
        } else if (timeArry.length == 3) {//如果时间是HH:MM:SS格式
            longTime = Integer.parseInt(timeArry[0]) * 1000 * 60 * 60 + Integer.parseInt(timeArry[1])
                    * 1000 * 60 + Integer.parseInt(timeArry[0]) * 1000;
        }
        return longTime / 60000;
    }

    //开始计时
    public void onStart1() {
        chronometer.start();
    }

    //停止计时
    public void onStop1() {
        chronometer.stop();
    }

    //重置
    public void onReset1() {
        chronometer.setBase(SystemClock.elapsedRealtime());
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
