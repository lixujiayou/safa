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
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.amap.api.maps.AMap;
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
import com.google.gson.Gson;
import com.inspur.hebeiline.R;
import com.inspur.hebeiline.core.SingleRoute;
import com.inspur.hebeiline.entity.ACodeBean;
import com.inspur.hebeiline.entity.LXResEntity;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.LXTestLoginBean;
import com.inspur.hebeiline.entity.TMLineMainEntity;
import com.inspur.hebeiline.entity.eventbean.MyEvent;
import com.inspur.hebeiline.entity.tmline.LocusPoint;
import com.inspur.hebeiline.entity.tmline.RouteInfoBean;
import com.inspur.hebeiline.model.GetRegionDataForNewModel;
import com.inspur.hebeiline.model.GetRegionDataModel;
import com.inspur.hebeiline.model.GetNearResModel;
import com.inspur.hebeiline.model.LocationModel;
import com.inspur.hebeiline.model.LoginModel;
import com.inspur.hebeiline.model.RegiontransferModel;
import com.inspur.hebeiline.room_.AppDatabase;
import com.inspur.hebeiline.utils.base.BaseActivity;
import com.inspur.hebeiline.utils.tools.SPUtil;
import com.inspur.hebeiline.utils.tools.StringUtils;
import com.inspur.hebeiline.utils.tools.ToastUtil;
import com.inspur.hebeiline.utils.tools.ToolUtil;
import com.inspur.hebeiline.utils.tools.Unility;
import com.inspur.hebeiline.utils.tools.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.apache.commons.codec.binary.Hex;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixu on 2018/3/14.
 * 传输线路
 */

public class LXMainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    private int TROUBLE_REQUESTCODE = 16;
    private int SUBMIT_REQUESTCODE = 15;

    private List<LXResEntity> mTmLineMainEntities = new ArrayList<>();
    private List<LXResNewEntity> mTmLineMainEntities2 = new ArrayList<>();

    private MapView mMapView = null;
    private AMap aMap;
    private LocationModel cLocationModel;
    private Context mContext;
    private ImageView ivLocation;
    private Button zsl_go, zsl_error_report;
    private ImageView ivWaitList;
    private ImageView iv_mine;


    //查询地图区域资源
    private GetRegionDataModel getRegionDataModel;

    //区域编码转换
    private RegiontransferModel regiontransferModel;


    //测试用账号
    private LoginModel loginModel;

    private List<Polyline> polylineList; //所有的线
    private List<Marker> markerList = new ArrayList<>(); //所有的点

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
    private int CURENT_Confirm_ID = 0;
    private String CURENT_RES_TYPE = "";

    private TextView tvMaxHint;
    private Button btUpdate;
    //private DeleteRouteModel deleteRouteModel;


    private GetRegionDataForNewModel getRegionDataForNewModel;


    //本地有没有数据
    private boolean isStartLoad = false;

    /**
     * 开始/结束状态
     */
    private boolean isDeliveryStart = false;
    private boolean isDeliveryEnd = false;
    private boolean isDeliveryStartGo = false;

    private int isMyFirst = 0;

    private String cCode;
    //路由ID
    private int routeID = 0;
    //点击的Marker
    private Marker currentMarkerForStart;
    private int currentResId;
    //当前交割的路由
    private RouteInfoBean routeInfoBean = null;

    private int autoLocation = 0;
    private UiSettings mUiSettings;//定义一个UiSettings对象

    private AppDatabase db;

    //声明Chronometer对象
    private Chronometer chronometer = null;

    private boolean isFirstLoad = true;

    //海量点
    private MultiPointOverlayOptions overlayOptions;
    private MultiPointOverlay multiPointOverlay;
    private List<MultiPointItem> list;


    private MultiPointOverlayOptions overlayOptions2;
    private MultiPointOverlay multiPointOverlay2;
    private List<MultiPointItem> list2;


    //用户再地图上点击的位置
    private LatLng clickLatLng;
    private CircleOptions clickCircleOptions = new CircleOptions();
    private MarkerOptions clickMarkerOption = new MarkerOptions();
    private Circle clickCircle;//点击Marker  方圆的圈
    private double clickCircumference = 2000;//选择方圆多少米
    private Marker clickMarker;//当前Marker


    private Switch btChange;
    private boolean isSelectForMap = true;
    private  SPUtil mSpUtil;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tmline_main);
        //添加权限
        RxPermissions rxPermissions = new RxPermissions(LXMainActivity.this);
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {

                } else {
                    // 用户拒绝了该权限，并且选中不再询问,引导用户跳转权限管理页面
                }
            }
        });
        mContext = LXMainActivity.this;
        //获取地图控件引用
        mMapView = findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        chronometer = (Chronometer) super.findViewById(R.id.chronometer);
        chronometer.setVisibility(View.GONE);

        mSpUtil = new SPUtil(mContext, SPUtil.USER);



    }



    @Override
    public void initViews() {
        isSHOW = true;
        tvTitle = findViewById(R.id.toolbar_title);
        ivLocation = findViewById(R.id.iv_location);
        ivCancle = findViewById(R.id.iv_cancle);
        mToolbarCicle = findViewById(R.id.toolbar_cicle);
        zsl_go = findViewById(R.id.zsl_go);
        iv_mine = findViewById(R.id.iv_mine);
        zsl_error_report = findViewById(R.id.zsl_error_report);
        tvMaxHint = findViewById(R.id.tv_max_hint);
        ivWaitList = findViewById(R.id.iv_wait_list);
        btUpdate = findViewById(R.id.bt_update);
        btChange = findViewById(R.id.bt_change);

        tvTitle.setText(R.string.tm_line);
        mToolbar.setTitle("");
        ivCancle.setVisibility(View.VISIBLE);
        ivLocation.setVisibility(View.VISIBLE);
        btUpdate.setVisibility(View.VISIBLE);
        btChange.setVisibility(View.VISIBLE);
        ivCancle.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
        zsl_go.setOnClickListener(this);
        ivWaitList.setOnClickListener(this);
        zsl_error_report.setOnClickListener(this);
        btUpdate.setOnClickListener(this);
        iv_mine.setOnClickListener(this);
        mToolbar.setOnMenuItemClickListener(this);
        setSupportActionBar(mToolbar);

        btChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    buttonView.setText("下载模式");
                    isSelectForMap = true;
                } else {
                    buttonView.setText("任务模式");
                    isSelectForMap = false;
                }
            }
        });
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
            mUiSettings.setScaleControlsEnabled(true);
        }


        if (db == null) {
            db = Room.databaseBuilder(mContext.getApplicationContext(), AppDatabase.class, "database-name").build();
        }


        getRegionDataForNewModel = ViewModelProviders.of(this).get(GetRegionDataForNewModel.class);
        getRegionDataForNewModel.getCurrentData(this, aMap, mMapView).observe(this, new Observer<List<LXResNewEntity>>() {
            @Override
            public void onChanged(@Nullable List<LXResNewEntity> lxResEntities) {
                mTmLineMainEntities2.clear();
                mTmLineMainEntities2.addAll(lxResEntities);
                //查询完成
                initLineation2(lxResEntities);
            }
        });
    }

    private void getCLines() {
        SPUtil spUtil = new SPUtil(mContext, SPUtil.USER);
        String cid = spUtil.getString("county_uid", "151");
        getRegionDataForNewModel.getNearRes(mRequestClient, cid, "2", null, 0, 0);

//        db.myDao().loadAllLinesNew()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<LXResNewEntity.LinesNewBean>>() {
//                    @Override
//                    public void accept(List<LXResNewEntity.LinesNewBean> linesBeans) {
//                        if (!ToolUtil.isEmpty(linesBeans)) {
//                            isStartLoad = true;
//                            try {
//                                LXResNewEntity lxResNewEntity = new LXResNewEntity();
//                                lxResNewEntity.setLines(linesBeans);
//                                List<LXResNewEntity> lxResNewEntities = new ArrayList<>();
//                                initLineation2(lxResNewEntities);
//
//                            } catch (Exception e) {
//                            }
//                        }
//                    }
//                });
    }


    @Override
    public void initData() {
        cicleAnime();
        regiontransferModel = ViewModelProviders.of(this).get(RegiontransferModel.class);
        regiontransferModel.getCurrentData(mContext).observe(this, new Observer<ACodeBean>() {
            @Override
            public void onChanged(@Nullable ACodeBean aCodeBean) {
                Log.d("qqqqqq", "转换后是===" + aCodeBean.getCountyUUID());

                SPUtil spUtil = new SPUtil(mContext, SPUtil.USER);
                String lid = spUtil.getString("county_uid","");
                if(StringUtils.isEmpty(lid) || !lid.equals(aCodeBean.getCountyUUID())){
                    spUtil.putString("county_uid",aCodeBean.getCountyUUID());
                    delectLast();
                    //说明和上次数据不一样，要重新下载
                    getRegionDataModel.getNearRes(mRequestClient, aCodeBean.getCountyUUID(), "0", null);

                }else{
                    //直接加载本地
                    getRegionDataModel.getNearRes(mRequestClient, aCodeBean.getCountyUUID(), "2", null);

                }
            }
        });
        cLocationModel = ViewModelProviders.of(this).get(LocationModel.class);

        cLocationModel.getlocationMutableLiveData().observe(this, new Observer<LatLng>() {
            @Override
            public void onChanged(@Nullable LatLng latLng) {
                //删掉测试数据
//                latLng = null;
//                latLng = new LatLng(38.042143, 114.523914);
                Log.d("qqqqqqqq", "手动获取当前经纬度===" + latLng.latitude);


                if (latLng != null) {
                    if (currentMarker == null) {
                        markerOption.position(latLng);
                        markerOption.draggable(false);//设置Marker不可拖动
                        markerOption.title("当前位置");//.snippet("西安市：34.341568, 108.940174");
                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(mContext.getResources(), R.drawable.icon_current)));
                        currentMarker = aMap.addMarker(markerOption);
//                        getRegionDataModel.getNearRes(mRequestClient,null,false);
                    } else {
                        markerOption.position(currentLatLng);
                        currentMarker.setMarkerOptions(markerOption);
                        if (autoLocation > 4) {
//                            getRegionDataModel.getNearRes(mRequestClient,null,false);
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
                        currentCircle.setCenter(currentLatLng);
                    }
                    //清空地图的点、线资源
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
                    //  getNearResModel.getNearRes(mRequestClient, latLng.latitude, latLng.longitude);
                } else {
                    if (isMyFirst == 1) {
                        //开始
                        // ToastUtil.showToast(mContext, "系统准备就绪，可以开始交割啦", ToastUtil.TOAST_TYPE_SUCCESS);
                        setDeliveryStatus("start");
                    }
                    isMyFirst = 0;

                    markerOption.position(currentLatLng);
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
            }
        });

        cLocationModel.setOnCallback(new LocationModel.OnCallBackListener() {
            @Override
            public void onCallBack(String province, String city, String district, String aCode) {
                cCode = aCode;
                SPUtil spUtil = new SPUtil(mContext, SPUtil.TAG);
                int aa = spUtil.getInt("isFirst", 1);
//                if(aa == 2){
//                    getRegionDataModel.getNearRes(mRequestClient, null, "2", null);
//
//                }else{
//                    spUtil.putInt("isFirst",2);
//                    getRegionDataModel.getNearRes(mRequestClient, aCode, "0", null);
//                }

                //定位成功 去转换区域编码
                regiontransferModel.regiontransfer(mRequestClient, aCode);
            }
        });


        getRegionDataModel = ViewModelProviders.of(this).get(GetRegionDataModel.class);
        getRegionDataModel.getCurrentData(this, aMap, mMapView).observe(this, new Observer<List<LXResEntity>>() {
            @Override
            public void onChanged(@Nullable List<LXResEntity> lxResEntities) {
                if (!ToolUtil.isEmpty(lxResEntities)) {
                    Log.d("qqqqq", "查询地图数据==" + lxResEntities.size());

                    mTmLineMainEntities.clear();
                    mTmLineMainEntities.addAll(lxResEntities);

                    Log.d("qqqqqq", "查到数据啦");

                    initLineation(lxResEntities);
                } else {
                    Log.d("qqqqqq", "没查到数据");
                    //ToastUtil.showToast(mContext,"没有查询到数据",ToastUtil.TOAST_TYPE_WARNING);
                }
            }
        });

        mToolbar.setOnMenuItemClickListener(this);


        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", "T400004238");
        loginMap.put("password", "Ysyhl9t!");
        loginMap.put("isVeriry", "0");
        String jsonMap = new Gson().toJson(loginMap);
        String aesStr = AESEncode(jsonMap);
        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("payload", aesStr);
        mRequestClient.login2(payloadMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Map<String, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Map<String, String> stringStringMap) {
                        Log.d("qqq", "登录====" + stringStringMap.toString());
                        SPUtil spUtil = new SPUtil(mContext, SPUtil.USER);
                        spUtil.putString(SPUtil.USER_TOKEN, stringStringMap.get("id_token"));
                        cLocationModel.getCurrentLocation(mContext, true, null, null, "");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("qqq", "登录失败====" + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


        // 定义 Marker 点击事件监听
        AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        };

        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(markerClickListener);

        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (isSelectForMap) {
                    clickLatLng = latLng;

                    mSpUtil.putString("line_lat", clickLatLng.latitude+"");
                    mSpUtil.putString("line_lon", clickLatLng.longitude+"");
                    if (clickMarker == null) {

                        clickMarkerOption.position(clickLatLng);
                        clickMarkerOption.draggable(true);//设置Marker不可拖动
                        clickMarkerOption.title("选中位置");//.snippet("西安市：34.341568, 108.940174");
                        clickMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(mContext.getResources(), R.drawable.icon_click_marker)));
                        clickMarker = aMap.addMarker(clickMarkerOption);
                    } else {
                        clickMarkerOption.position(clickLatLng);
                        clickMarker.setMarkerOptions(clickMarkerOption);
                    }

                    //画方圆
                    if (clickCircle == null) {
                        clickCircleOptions.center(clickLatLng);
                        clickCircleOptions.radius(clickCircumference);
                        clickCircleOptions.strokeWidth(1);
                        clickCircleOptions.strokeColor(Color.argb(50, 1, 1, 1));
                        clickCircleOptions.fillColor(Color.argb(50, 1, 1, 1));
                        clickCircle = aMap.addCircle(clickCircleOptions);
                    } else {
                        clickCircle.setCenter(clickLatLng);
                    }
                } else {
                    List<LXResNewEntity.LinesNewBean> items = Unility.getNearbyLine(aMap.getProjection(), latLng, mTmLineMainEntities2, 30);
                    if (items.size() != 0) {
                        LXResNewEntity.LinesNewBean item = items.get(0);
                        Intent lineIntent = new Intent(LXMainActivity.this, LXWaitInfoActivity.class);
                        lineIntent.putExtra("type", "map");
                        lineIntent.putExtra("uuid", item.getUuid());
                        lineIntent.putExtra("routeid", item.getRouteUUID());
                        lineIntent.putExtra("entity", item);
                        lineIntent.putExtra("taskid", item.getTaskUUID());
                        startActivityForResult(lineIntent, 1);
                    }


                }


            }
        });


        // 定义 Marker拖拽的监听
        AMap.OnMarkerDragListener markerDragListener = new AMap.OnMarkerDragListener() {

            // 当marker开始被拖动时回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub


                clickLatLng = arg0.getPosition();
                //画方圆
                if (clickCircle == null) {
                    clickCircleOptions.center(clickLatLng);
                    clickCircleOptions.radius(clickCircumference);
                    clickCircleOptions.strokeWidth(1);
                    clickCircleOptions.strokeColor(Color.argb(50, 1, 1, 1));
                    clickCircleOptions.fillColor(Color.argb(50, 1, 1, 1));
                    clickCircle = aMap.addCircle(clickCircleOptions);
                } else {
                    clickCircle.setCenter(clickLatLng);
                }
            }

            // 在marker拖动完成后回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                clickLatLng = arg0.getPosition();
                mSpUtil.putString("line_lat", clickLatLng.latitude+"");
                mSpUtil.putString("line_lon", clickLatLng.longitude+"");
                //画方圆
                if (clickCircle == null) {
                    clickCircleOptions.center(clickLatLng);
                    clickCircleOptions.radius(clickCircumference);
                    clickCircleOptions.strokeWidth(1);
                    clickCircleOptions.strokeColor(Color.argb(50, 1, 1, 1));
                    clickCircleOptions.fillColor(Color.argb(50, 1, 1, 1));
                    clickCircle = aMap.addCircle(clickCircleOptions);
                } else {
                    clickCircle.setCenter(clickLatLng);
                }


            }

            // 在marker拖动过程中回调此方法, 这个marker的位置可以通过getPosition()方法返回。
            // 这个位置可能与拖动的之前的marker位置不一样。
            // marker 被拖动的marker对象。
            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub

                clickLatLng = arg0.getPosition();
                //画方圆
                if (clickCircle == null) {
                    clickCircleOptions.center(arg0.getPosition());
                    clickCircleOptions.radius(clickCircumference);
                    clickCircleOptions.strokeWidth(1);
                    clickCircleOptions.strokeColor(Color.argb(50, 1, 1, 1));
                    clickCircleOptions.fillColor(Color.argb(50, 1, 1, 1));
                    clickCircle = aMap.addCircle(circleOptions);
                } else {
                    clickCircle.setCenter(arg0.getPosition());
                }

            }
        };
// 绑定marker拖拽事件
        aMap.setOnMarkerDragListener(markerDragListener);


        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (isFirstLoad) {
                    return;
                }

                Log.d("qqqqqq", cameraPosition.zoom + "地图==" + aMap.getScalePerPixel());
                if (cameraPosition.zoom > 14 && isStartLoad) {
                    SPUtil spUtil = new SPUtil(mContext, SPUtil.USER);
                    String cid = spUtil.getString("county_uid", "151");
                    getRegionDataForNewModel.getNearRes(mRequestClient, cid, "2", null, 0, 0);

                } else {
                    //ToastUtil.show(mContext,"比例尺过大,停止加载数据",ToastUtil.TOAST_TYPE_WARNING);
                }
            }
        });




    }


    /**
     * 初始化地图上次加载的数据
     */
    private void initLastMapData(){

        String lastLatStr = mSpUtil.getString("line_lat", "0");
        String lastLonStr =  mSpUtil.getString("line_lon", "0");
        boolean lastIsHaveData =  mSpUtil.getBoolean("line_have", false);
        Log.d("qqqqqq","数据lastIsHaveData=="+lastIsHaveData);
        try {
            clickLatLng = new LatLng(Double.parseDouble(lastLatStr),Double.parseDouble(lastLonStr));
            if (clickMarker == null) {

                clickMarkerOption.position(clickLatLng);
                clickMarkerOption.draggable(true);//设置Marker不可拖动
                clickMarkerOption.title("选中位置");//.snippet("西安市：34.341568, 108.940174");
                clickMarkerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(mContext.getResources(), R.drawable.icon_click_marker)));
                clickMarker = aMap.addMarker(clickMarkerOption);
            } else {
                clickMarkerOption.position(clickLatLng);
                clickMarker.setMarkerOptions(clickMarkerOption);
            }

            //画方圆
            if (clickCircle == null) {
                clickCircleOptions.center(clickLatLng);
                clickCircleOptions.radius(clickCircumference);
                clickCircleOptions.strokeWidth(1);
                clickCircleOptions.strokeColor(Color.argb(50, 1, 1, 1));
                clickCircleOptions.fillColor(Color.argb(50, 1, 1, 1));
                clickCircle = aMap.addCircle(clickCircleOptions);
            } else {
                clickCircle.setCenter(clickLatLng);
            }


            //如果上次没有数据，则本次将会加载当前位置的数据
            if(!lastIsHaveData){
                SPUtil spUtil = new SPUtil(mContext, SPUtil.USER);
                String cid = spUtil.getString("county_uid", "151");
                getRegionDataForNewModel.getNearRes(mRequestClient, cid, "0", null, currentLatLng.longitude, currentLatLng.latitude);
            }else{
                getCLines();
            }
        }catch (Exception e){}

    }

    public String AESEncode(String content) {
        String base64EnCode = "ZGIyMTM5NTYxYzlmZTA2OA==";
        byte[] key = decodeBase64(base64EnCode);
        byte[] iv = decodeBase64(base64EnCode);
        SecretKeySpec secretKeySpec = new SecretKeySpec(getKey(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(getIV()));
            byte[] byteContent = content.getBytes("utf-8");
            byte[] byteResult = cipher.doFinal(byteContent);
            return new String(Hex.encodeHex(byteResult));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "";
    }

    private byte[] decodeBase64(String dataToDecode) {
        byte[] dataDecoded = Base64.decode(dataToDecode, Base64.DEFAULT);
        return dataDecoded;
    }

    public static byte[] getKey() {
        //ZGIyMTM5NTYxYzlmZTA2OA==
        return "db2139561c9fe068".getBytes();
    }

    public static byte[] getIV() {
        //ZGIyMTM5NTYxYzlmZTA2OA==
        return "db2139561c9fe068".getBytes();
    }


    private Vibrator vibrator;

    private void maxDistanceHint() {

        tvMaxHint.setVisibility(View.VISIBLE);
        //震动
        vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
        // 等待3秒，震动3秒，从第0个索引开始，一直循环  
        vibrator.vibrate(new long[]{3000, 3000}, 0);
        //响铃
        playSound(LXMainActivity.this);
    }

    /**
     * 响铃提示
     */
    private MediaPlayer mMediaPlayer;

    public void playSound(final Context context) {

        Log.e("ee", "正在响铃");
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
    private int colorMy;
    private List<LatLng> latLngs = new ArrayList<>();

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
        }if (!ToolUtil.isEmpty(list2)) {
            list2.clear();
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

        //需要改动
        if (isFirstLoad) {
            isFirstLoad = false;
        }

        Observable.fromArray(tmLineMainEntities.get(0).getLines())
                .concatMap(new Function<List<LXResEntity.LinesBean>, ObservableSource<List<MultiPointItem>>>() {
                    @Override
                    public ObservableSource<List<MultiPointItem>> apply(List<LXResEntity.LinesBean> linesBeans) throws Exception {
                        overlayOptions = new MultiPointOverlayOptions();
                        overlayOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_marker_default)));//设置图标
                        overlayOptions.anchor(0.5f, 0.5f); //设置锚点


                        overlayOptions2 = new MultiPointOverlayOptions();
                        overlayOptions2.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_marker_ok)));//设置图标
                        overlayOptions2.anchor(0.5f, 0.5f); //设置锚点



                        multiPointOverlay = aMap.addMultiPointOverlay(overlayOptions);


                        multiPointOverlay2 = aMap.addMultiPointOverlay(overlayOptions2);


                        list = new ArrayList<MultiPointItem>();
                        list2 = new ArrayList<MultiPointItem>();

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
                                if(!StringUtils.isEmpty(linesBean.getStatus()) && linesBean.getStatus().equals("0")){
                                    list.add(multiPointItem);
                                }else{
                                    list2.add(multiPointItem);
                                }
                            }


                            /**
                             * 添加end的点
                             **/
                            LatLng lalnEnd = new LatLng(StringUtils.toDouble(linesBean.getZPointLat()), StringUtils.toDouble(linesBean.getZPointLng()));
                            MultiPointItem multiPointItem = new MultiPointItem(lalnEnd);
                            if(!StringUtils.isEmpty(linesBean.getStatus()) && linesBean.getStatus().equals("0")){
                                list.add(multiPointItem);
                            }else{
                                list2.add(multiPointItem);
                            }
                            if (ToolUtil.isEmpty(polylineList)) {
                                polylineList = new ArrayList<>();
                            }
                        }

                        multiPointOverlay.setItems(list);
                        multiPointOverlay2.setItems(list2);

                        return Observable.just(list);

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MultiPointItem>>() {
                    @Override
                    public void accept(List<MultiPointItem> linesBean) throws Exception {
                        dismissProgressDialog();
                        initLastMapData();
                        Log.d("qqqqq", "点画完了");

                    }
                });
    }

    private int myAddNum;
    private boolean isPolLineOk = true;//线是否完成

    private void initLineation2(List<LXResNewEntity> tmLineMainEntities) {
        if (ToolUtil.isEmpty(tmLineMainEntities) || ToolUtil.isEmpty(tmLineMainEntities.get(0).getLines())) {
            return;
        }

        mSpUtil.putBoolean("line_have", true);//记录上次有没有请求到数据
        if (!isPolLineOk) {
            Log.d("qqqqqq", "上次的线还没有画完，继续上次");
            return;
        }
        isStartLoad = true;
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
            Intent mineIntent = new Intent(LXMainActivity.this, LXMineActivity.class);
            startActivity(mineIntent);

        } else if (i == R.id.iv_location) {

            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(currentLatLng, 15, 0, 0));
            aMap.animateCamera(mCameraUpdate, 1000, new AMap.CancelableCallback() {
                @Override
                public void onFinish() {

                }

                @Override
                public void onCancel() {

                }
            });
        } else if (i == R.id.zsl_go) {
            if (!isDeliveryStart) {
                showStartDialog();
            } else {
                showEndDialog();
                //ToastUtil.showToast(mContext,"正在交割中",ToastUtil.TOAST_TYPE_WARNING);
            }

        } else if (i == R.id.zsl_error_report) {
            if (isDeliveryStart) {
//                    Intent gIntent = new Intent(mContext, HiddenTroubleActivity.class);
//                    gIntent.putExtra(HiddenTroubleActivity.ROUTEID_KEY, routeID);
//                    startActivityForResult(gIntent, 1);
            } else {
                ToastUtil.showToast(mContext, "只有在交割路由时才能上报隐患哦", ToastUtil.TOAST_TYPE_WARNING);
            }

        } else if (i == R.id.iv_wait_list) {
            Intent gIntent = new Intent(LXMainActivity.this, LXWaitListActivity.class);
            startActivity(gIntent);

        } else if (i == R.id.bt_update) {
            showUpdateDialog();
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
            isDeliveryStart = false;
            isDeliveryEnd = true;
            isDeliveryStartGo = true;
        } else if (statusDelivery.equals("start")) {
            zsl_go.setText("结束");
            isDeliveryStart = true;
            isDeliveryEnd = false;
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
            ToastUtil.showToast(mContext, "再按一次退出传输线路巡检", ToastUtil.TOAST_TYPE_NOMAL);
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
//        if (this.isSHOW) {
//            if (event.getType() == event.OKK) {
//                mToolbarCicle.setVisibility(View.VISIBLE);
//            } else {
//                mToolbarCicle.setVisibility(View.GONE);
//            }
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int i = menuItem.getItemId();
        if (i == R.id.action_update) {
            //刷新存量数据
            delectLast();
            //说明和上次数据不一样，要重新下载
            getRegionDataModel.getNearRes(mRequestClient, mSpUtil.getString("county_uid",""), "0", null);

        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        cLocationModel.stopLocation();
        mMapView.onDestroy();
        if (rotation != null && rotation.isStarted()) {
            rotation.cancel();
        }

//        closePlayAndVibrator();

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

    // private SweetAlertDialog pDialogSuccess;

    /**
     * 开始交割弹窗
     */
    private void showStartDialog() {
        SweetAlertDialog pDialogSuccess;
        try {
            pDialogSuccess = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
            pDialogSuccess.setTitleText("准备好开始交割了吗？");
            pDialogSuccess.setContentText("点击开始后不要忘记选择交割起点哦.");
            pDialogSuccess.setCancelable(true);
            pDialogSuccess.setConfirmText("开始");
            pDialogSuccess.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    isMyFirst = 1;
                    sweetAlertDialog.dismiss();
                    ToastUtil.showToast(mContext, "请选择交割起始点", ToastUtil.TOAST_TYPE_WARNING);
                    isDeliveryEnd = false;

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
     * 结束交割弹窗
     */
    private void showEndDialog() {
        SweetAlertDialog pDialogSuccess;
        try {
            pDialogSuccess = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
            pDialogSuccess.setTitleText("准备好结束交割了吗？");
            pDialogSuccess.setContentText("点击确定后不要忘记选择结束点哦.");
            pDialogSuccess.setCancelable(true);
            pDialogSuccess.setConfirmText("确定");
            pDialogSuccess.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    isMyFirst = 1;
                    sweetAlertDialog.dismiss();
                    ToastUtil.showToast(mContext, "请选择交割结束点", ToastUtil.TOAST_TYPE_WARNING);
                    setDeliveryStatus("default");

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
    private void delectLast(){
        //删掉之前所有的段
        db.myDao().loadAllAllLines()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<LXResEntity.LinesBean>>() {
                    @Override
                    public void accept(List<LXResEntity.LinesBean> linesBeans) throws Exception {
                        if (!ToolUtil.isEmpty(linesBeans)) {
                            int a = db.myDao().deleteAllLines(linesBeans);
                            Log.d("qqqqqq", "删除结果线=" + a);
                        }
                    }
                });

        //删掉之前的路由
        db.myDao().loadRoute()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<List<LXResEntity>>() {
                    @Override
                    public void accept(List<LXResEntity> lxResEntities) throws Exception {
                        if (!ToolUtil.isEmpty(lxResEntities)) {
                            int a = db.myDao().deleteRoutes(lxResEntities);
                            Log.d("qqqqqq", "删除结路由=" + a);
                        }
                    }
                });
    }

    /**
     * 重新请求数据
     */
    private void showUpdateDialog() {
        SweetAlertDialog pDialogSuccess;
        try {
            pDialogSuccess = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
            pDialogSuccess.setTitleText("下载选中区域数据？");
            pDialogSuccess.setContentText("若有已巡检但未提交的数据，请提交后再更新.");
            pDialogSuccess.setCancelable(true);
            pDialogSuccess.setConfirmText("继续");
            pDialogSuccess.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();

                    //为避免地图的比例尺过大，下载完数据后地图太卡，先将地图定过来
                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(clickLatLng, 14, 0, 0));
                    aMap.animateCamera(mCameraUpdate, 1000, new AMap.CancelableCallback() {
                        @Override
                        public void onFinish() {

                        }

                        @Override
                        public void onCancel() {

                        }
                    });




                    //删掉之前所有的段
                    db.myDao().loadAllAllLinesNew()
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(new Consumer<List<LXResNewEntity.LinesNewBean>>() {
                                @Override
                                public void accept(List<LXResNewEntity.LinesNewBean> linesBeans) throws Exception {
                                    if (!ToolUtil.isEmpty(linesBeans)) {
                                        int a = db.myDao().deleteAllLinesNew(linesBeans);
                                        Log.d("qqqqqq", "删除之前的选中=" + a);
                                    }
                                }
                            });

                    //删掉之前的路由
                    db.myDao().loadRouteNew()
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(new Consumer<List<LXResNewEntity>>() {
                                @Override
                                public void accept(List<LXResNewEntity> lxResEntities) throws Exception {
                                    if (!ToolUtil.isEmpty(lxResEntities)) {
                                        int a = db.myDao().deleteRoutesNew(lxResEntities);
                                        Log.d("qqqqqq", "删除之前的选中=" + a);
                                    }
                                }
                            });

                    //定位成功 去转换区域编码
//                    regiontransferModel.regiontransfer(mRequestClient, cCode);


                    SPUtil spUtil = new SPUtil(mContext, SPUtil.USER);
                    String cid = spUtil.getString("county_uid", "151");
                    getRegionDataForNewModel.getNearRes(mRequestClient, cid, "0", null, clickLatLng.longitude, clickLatLng.latitude);

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
    private void showPointDialog(final String typeDialog, final Marker marker,
                                 final int resId) {
        String titleText = "选择'" + marker.getTitle() + "'为起点？";
        if (!StringUtils.isEmpty(typeDialog) && typeDialog.equals("end")) {
            titleText = "选择'" + marker.getTitle() + "'为终点？";
        }
        final SweetAlertDialog pDialogSuccess;
        try {
            pDialogSuccess = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);

            pDialogSuccess.setTitleText(titleText);

            //pDialogSuccess.setContentText("点击确定后不要忘记选择交割起点哦.");
            pDialogSuccess.setCancelable(true);
            pDialogSuccess.setConfirmText("确定");
            pDialogSuccess.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    pDialogSuccess.dismiss();
                    //ToastUtil.showToast(mContext,"请选择交割起始点",ToastUtil.TOAST_TYPE_WARNING);
                    //isDeliveryStart = true;

                    //选择终点结束交割处理
                    if (!StringUtils.isEmpty(typeDialog) && typeDialog.equals("end")) {
                        //isMyFirst = 1;
                        //开始
                        //setDeliveryStatus("default");
                        setDeliveryStatus("end");
                        if (routeInfoBean != null) {
                            //设置路由的终点
//                            PointlikeResourceInfoBean pointlikeResourceInfoBean = new PointlikeResourceInfoBean();
//                            pointlikeResourceInfoBean.setLatitude(marker.getPosition().latitude);
//                            pointlikeResourceInfoBean.setLongitude(marker.getPosition().longitude);
//                            pointlikeResourceInfoBean.setResourceName(marker.getTitle());
//                            pointlikeResourceInfoBean.setResourceID(resId);
//                            pointlikeResourceInfoBean.setRouteID(routeID);
//                            pointlikeResourceInfoBean.setResourceType(CURENT_RES_TYPE);
//                            routeInfoBean.setEndPosition(pointlikeResourceInfoBean);
                        }

                        //交割结束，获取所有数据并前去提交
                        cLocationModel.getLocationListLiveData();
                    } else {
                        //生成临时路由ID
//                        String cTime = String.valueOf(System.currentTimeMillis());
//                        String cTime_6 =cTime.substring(cTime.length()-6,cTime.length());
//                        routeID = Integer.parseInt(cTime_6);
                        currentMarkerForStart = marker;
                        currentResId = resId;
                        //去获取路由id
//                        getRouteIdModel.getRouteId(mRequestClient);
                    }
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
