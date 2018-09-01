package com.inspur.hebeiline.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceStatusListener;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.inspur.hebeiline.core.SingleRoute;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.RecordTestBean;
import com.inspur.hebeiline.entity.eventbean.MyEvent;
import com.inspur.hebeiline.entity.tmline.LocusPoint;
import com.inspur.hebeiline.entity.tmline.MyWayBean;
import com.inspur.hebeiline.room_.AppDatabase;
import com.inspur.hebeiline.utils.base.BaseViewModel;
import com.inspur.hebeiline.utils.tools.SPUtil;
import com.inspur.hebeiline.utils.tools.StringUtils;
import com.inspur.hebeiline.utils.tools.ToastUtil;
import com.inspur.hebeiline.utils.tools.ToolUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixu on 2018/1/23.
 */

public class LocationModel extends BaseViewModel {
    public LocationModel(Application application) {
        super(application);
    }

    private MutableLiveData<LatLng> locationMutableLiveData;
    private AMap mAmap;
    private Context mContext;
    private MyLocationStyle myLocationStyle;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private AppDatabase db;

    private LatLng lastLocation;
    private LatLng currentLocation;

    private int heightRange = 80;//高度在多少米之内
    private int MAXheightRange = 150;//最大范围
    private final int surpassHeightMAX = 5;//超过最大高度多少次后换线


    private List<MyWayBean> myWayBeanList = new ArrayList<>();

    private List<Double> lineHeights = new ArrayList<>();

    private int singleBiggest = 80;//单次定位距离不得超过80米，否则视为经纬度跳动

    public static int TOTAL_DISTANCE = 0;//总距离

    //private int MAX_DISTANCE = 1000;//最大距离1000
    private int DIS = -1;//每次划线最小距离10

    private List<LocusPoint> mLocations = new ArrayList<>();
    private MutableLiveData<List<LocusPoint>> locationList;
    private List<LatLng> latLngs = new ArrayList<>(); //所有的点
    private List<Polyline> polylines = new ArrayList<>(); //所有的线
    private List<LXResNewEntity> mlxResEntities = new ArrayList<>();//所有的路由及线资源
    private Map<String, List<RecordTestBean>> recordMap = new HashMap<>();//本段记录的所有线

    private LatLng latLngC;

    private double START_DISTANCE_MAX = 200;//起点最大距离

    public MutableLiveData<LatLng> getlocationMutableLiveData() {
        if (locationMutableLiveData == null) {
            locationMutableLiveData = new MutableLiveData<>();
        }
        if (locationMutableLiveData == null) {
            locationMutableLiveData = new MutableLiveData<>();
        }
        return locationMutableLiveData;
    }

    public MutableLiveData<List<LocusPoint>> getlocationListMutableLiveData() {
        if (locationList == null) {
            locationList = new MutableLiveData<>();
        }
        if (!ToolUtil.isEmpty(mLocations)) {
            locationList.postValue(mLocations);
        }
        return locationList;
    }


//    public void getLiveData() {
//        if (locationMutableLiveData == null) {
//            locationMutableLiveData = new MutableLiveData<>();
//        }
//        locationMutableLiveData.postValue(locationMutableLiveData.getValue());
//    }


    public void getLocationListLiveData() {
        if (locationList == null) {
            locationList = new MutableLiveData<>();
        }
        //if(!ToolUtil.isEmpty(mLocations)){
        locationList.postValue(mLocations);
        // }

    }

    /**
     * @param context
     * @param isOnce     是否只定位一次
     * @param aMap
     * @param startPoint 交割起点
     */
    public void getCurrentLocation(final Context context, final boolean isOnce, final AMap aMap, final LatLng startPoint, final String taskID) {


        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database-name").build();
        }
        if (aMap != null) {
            this.mAmap = aMap;
        }
        this.mContext = context;
        if (myLocationStyle == null) {
            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        }
        //清空记录的点
        if (!isOnce) {
            if (!ToolUtil.isEmpty(mLocations)) {
                mLocations.clear();
            }
            latLngs.clear();
            polylines.clear();
        }
        EventBus.getDefault().post(new MyEvent(MyEvent.OKK));
        if (mLocationOption == null) {
            mLocationOption = new AMapLocationClientOption();
        }
        //初始化定位
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(final AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        latLngC = null;
                        latLngC = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());

                        SPUtil spUtil = new SPUtil(context, SPUtil.LOCATION_ADRESS);
                        spUtil.putString(SPUtil.LOCATION_LAT, amapLocation.getLatitude() + "");
                        spUtil.putString(SPUtil.LOCATION_LON, amapLocation.getLongitude() + "");

                        if (!isOnce) {
                            if (lastLocation == null) {
                                currentLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                                //修改为多次定位
//                                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
                                //mAmap.setMyLocationStyle(myLocationStyle);
                                //第一次计算距离，直接add到轨迹点
                                lastLocation = new LatLng(latLngC.latitude, latLngC.longitude);
                                LocusPoint recordLocationEntity = new LocusPoint();
                                recordLocationEntity.setLatLng(lastLocation);
                                recordLocationEntity.setTaskId(taskID);
                                mLocations.add(recordLocationEntity);
                                latLngs.add(lastLocation);

                                //查询出所有的点，并去匹配80米内的资源
                                queryAllData();

                            } else {
                                if (currentLocation != null) {
                                    currentLocation = null;
                                }
                                currentLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                                //将屏幕移动到当前位置
                                CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLng(currentLocation);
                                aMap.animateCamera(mCameraUpdate);

                                //根据当前位置划线
                                lineation();

                                //判断有没匹配到线  匹配到线则记录并计算高度  否则不记录只划线并继续去计算有无匹配到
                                if (ToolUtil.isEmpty(SingleRoute.getInstance().getCurrentLine())) {
                                    //去匹配80米内的资源
                                    queryAllData();
                                } else {
                                    //第一次
                                    //计算线的两点是否与当前位置80米之内，计算高度并记录
                                    kernelGo();

                                }


                            }
                            if (onPointBackListener != null) {
                                onPointBackListener.onCallBack(currentLocation);
                            }

                            //locationMutableLiveData.postValue(latLngC);
                        } else {
                            String adCode = amapLocation.getAdCode();//地区编码
                            if (onCallBackListener != null) {
                                onCallBackListener.onCallBack(amapLocation.getProvince(), amapLocation.getCity(), amapLocation.getDistrict(), adCode);
                            }

                            locationMutableLiveData.postValue(latLngC);
                        }

                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    }
                }
                EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
            }
        });
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        if (isOnce) {
            //获取一次定位结果：
            //该方法默认为false。
            mLocationOption.setOnceLocation(true);
            //获取最近3s内精度最高的一次定位结果：
            mLocationOption.setOnceLocationLatest(true);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
        } else {
            mLocationOption.setOnceLocation(false);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(false);
        }
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(5000);

        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //初始化AMapLocationClientOption对象
        //   mLocationOption = new AMapLocationClientOption();
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.stopLocation();
        mLocationClient.startLocation();

    }

    public void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stopLocation();
        }
    }


    /**
     * 计算并记录
     */

    private void kernelGo() {
        List<LXResNewEntity.LinesNewBean> mCuList = SingleRoute.getInstance().getCurrentLine();

        try {
            for (int i = 0; i < mCuList.size(); i++) {
                LXResNewEntity.LinesNewBean mCu = mCuList.get(i);
                LatLng kernelLatLngMarkerA;
                LatLng kernelLatLngMarkerZ;
                kernelLatLngMarkerA = new LatLng(Double.parseDouble(mCu.getAPointLat()), Double.parseDouble(mCu.getAPointLng()));

                kernelLatLngMarkerZ = new LatLng(Double.parseDouble(mCu.getZPointLat()), Double.parseDouble(mCu.getZPointLng()));

                double MA = convert(AMapUtils.calculateLineDistance(kernelLatLngMarkerA, currentLocation));
                double MZ = convert(AMapUtils.calculateLineDistance(kernelLatLngMarkerZ, currentLocation));
                double AZ = convert(AMapUtils.calculateLineDistance(kernelLatLngMarkerZ, kernelLatLngMarkerA));

                double p = (MA + MZ + AZ) / 2;
                double s = Math.sqrt(p * (p - MA) * (p - MZ) * (p - AZ));
                //将所有正在巡检或可能会巡检的线的 垂直高度、当前位置记录
                double dis = 2 * s / AZ;
                RecordTestBean recordTestBean = new RecordTestBean();
                recordTestBean.setResHeight(dis);
                recordTestBean.setCurrentLocation(currentLocation);

                if (recordMap != null) {
                    if (!recordMap.containsKey(mCu.getUuid())) {
                        List<RecordTestBean> recordTestBeans = new ArrayList<>();
                        recordTestBeans.add(recordTestBean);
                        recordMap.put(mCu.getUuid(), recordTestBeans);
                    } else {
                        List<RecordTestBean> recordTestBeans = recordMap.get(mCu.getUuid());
                        recordTestBeans.add(recordTestBean);
                        recordMap.put(mCu.getUuid(), recordTestBeans);
                    }
                }
                //换线计算
                if(dis > MAXheightRange){
                    int num = SingleRoute.getInstance().getCurrentLine().get(i).getContinuousNum();
                    num += 1;
                    SingleRoute.getInstance().getCurrentLine().get(i).setContinuousNum(num);
                    ToastUtil.showToast(mContext,"高度连续超过"+num+"次"+MAXheightRange+"米",ToastUtil.TOAST_TYPE_WARNING);
                }else{
                    SingleRoute.getInstance().getCurrentLine().get(i).setContinuousNum(0);
                }

                //判断有没有到端点，如果到则结束计算本段并去巡检下一段
                LatLng lan = SingleRoute.getInstance().getMateLatLng();


                if (lan != null) {
                    if (String.valueOf(lan.longitude).equals(mCu.getAPointLng()) && String.valueOf(lan.latitude).equals(mCu.getAPointLat())) {
                        if (MZ <= heightRange) {
                            endAndChange(mCu.getUuid(), mCu, kernelLatLngMarkerZ);
                            break;
                        }

                    } else if (String.valueOf(lan.longitude).equals(mCu.getZPointLng()) && String.valueOf(lan.latitude).equals(mCu.getZPointLat())) {
                        if (MA <= heightRange) {
                            endAndChange(mCu.getUuid(), mCu, kernelLatLngMarkerA);
                            break;
                        }
                    }
                    //没有则匹配到哪一个点都算结束  //现在没有这种可能了 所以删掉
                } else {
//                    if (MA <= heightRange) {
//                        endAndChange(mCu.getUuid(), mCu, kernelLatLngMarkerA);
//                        break;
//                    } else if (MZ <= heightRange) {
//                        endAndChange(mCu.getUuid(), mCu, kernelLatLngMarkerZ);
//                    }
                }
            }
            //如果正在巡的所有线全部连续5次超过150米，则重新去计算
            int cN = 0;
            List<LXResNewEntity.LinesNewBean> ss = SingleRoute.getInstance().getCurrentLine();
            for(int i = 0;i < ss.size();i++ ){
                if(ss.get(i).getContinuousNum() >= surpassHeightMAX){
                    cN += 1;
                }
            }
            if(cN >= ss.size()){
                SingleRoute.getInstance().setCurrentLine(new ArrayList<LXResNewEntity.LinesNewBean>());
                isRangeHave(mlxResEntities);
            }

        } catch (Exception e) {
        }
    }

    /**
     * 重新匹配最近的点
     * @return
     * isRangeHave()
     */
//    private boolean isHaveNearPointOther(){
//        double minHeight = 0;
//        LatLng latlngCC = SingleRoute.getInstance().getMateLatLng();//正在跑的最近点，不能和这个点重复
//        if(latlngCC != null && !ToolUtil.isEmpty(mlxResEntities)) {
//            for (int i = 0; i < mlxResEntities.size(); i++) {
//                LXResNewEntity tmList = mlxResEntities.get(i);
//                for (int j = 0; j < tmList.getLines().size(); j++) {
//                    LXResNewEntity.LinesNewBean lineForOne = tmList.getLines().get(j);
//                    if (StringUtils.isDouble(lineForOne.getAPointLat()) && StringUtils.isDouble(lineForOne.getAPointLng())
//                            && StringUtils.isDouble(lineForOne.getZPointLat()) && StringUtils.isDouble(lineForOne.getZPointLng())) {
//
//                        latLngMarkerA = null;
//                        latLngMarkerA = new LatLng(StringUtils.toDouble(lineForOne.getAPointLat()), StringUtils.toDouble(lineForOne.getAPointLng()));
//
//                        latLngMarkerZ = null;
//                        latLngMarkerZ = new LatLng(StringUtils.toDouble(lineForOne.getZPointLat()), StringUtils.toDouble(lineForOne.getZPointLng()));
//
//
//
//                        double AM = convert(AMapUtils.calculateLineDistance(currentLocation, kernelLatLngMarkerA));
//                        double ZM = convert(AMapUtils.calculateLineDistance(currentLocation, kernelLatLngMarkerZ));
//
//
//
//                        if(latlngCC.latitude == latLngMarkerA.latitude && latlngCC.longitude == latLngMarkerA.longitude){
////                            continue;
//                        }else{
//                            if(minHeight == 0 && AM < heightRange){
//                                minHeight = AM;
//                            }else if(heightRange < minHeight){
//                                minHeight = AM;
//                            }
//                        }
//                        if(latlngCC.latitude == latLngMarkerZ.latitude && latlngCC.longitude == latLngMarkerZ.longitude){
////                            continue;
//                        }else{
//                            if(minHeight == 0 && ZM < heightRange){
//                                minHeight = ZM;
//                            }else if(heightRange < minHeight){
//                                minHeight = ZM;
//                            }
//                        }
//                        //找到最近的点
//                        if(minHeight != 0){
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//    }

    /**
     * 第一次完成并去计算匹配率
     */
    private LXResNewEntity.LinesNewBean cLine;

    private void endAndChange(final String lineUUID, LXResNewEntity.LinesNewBean mCu, LatLng latLng) {
        cLine = mCu;
        int offNum = 0;
        if (recordMap.containsKey(lineUUID)) {
            //先计算匹配率
            List<RecordTestBean> cRes = recordMap.get(lineUUID);
            if (!ToolUtil.isEmpty(cRes)) {
                for (int i = 0; i < cRes.size(); i++) {
                    if (cRes.get(i).getResHeight() < heightRange) {
                        offNum += 1;
                    }
                }

                int ratio = offNum / cRes.size() * 100;
                //匹配率若大于80则修改状态和颜色
                if (ratio >= 80) {
                    cLine.setStatus("1");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < mlxResEntities.size(); i++) {
                                LXResNewEntity tmList = mlxResEntities.get(i);
                                for (int j = 0; j < tmList.getLines().size(); j++) {
                                    if (tmList.getLines().get(j).getUuid().equals(lineUUID)) {
                                        tmList.getLines().get(j).setStatus("1");
                                    }
                                }
                            }
                            int updateCode = db.myDao().updateLines(cLine);
                            //回调,重新请求数据
                            if (onPointBackListener != null) {
                                onPointBackListener.onUpdateLine(lineUUID);
                            }

                        }
                    }).start();
                }
            }
        }

        //清除之前存的所有点与高度
        recordMap.clear();
        SingleRoute.getInstance().setCurrentLine(new ArrayList<LXResNewEntity.LinesNewBean>());
        List<LXResNewEntity.LinesNewBean> linesBeans = new ArrayList<>();

        //处理完本次后再去找下次需要匹配的线
        if (!ToolUtil.isEmpty(mlxResEntities)) {
            SingleRoute.getInstance().setMateLatLng(latLng);//将起点设置为上一个线的终点
            for (int i = 0; i < mlxResEntities.size(); i++) {
                LXResNewEntity tmList = mlxResEntities.get(i);
                for (int j = 0; j < tmList.getLines().size(); j++) {

                    LXResNewEntity.LinesNewBean lineForOne = tmList.getLines().get(j);
//                    if (!lineForOne.getStatus().equals("1")) { //已巡检成功的不会再次匹配上
                    if (StringUtils.isDouble(lineForOne.getAPointLat()) && StringUtils.isDouble(lineForOne.getAPointLng())
                            && StringUtils.isDouble(lineForOne.getZPointLat()) && StringUtils.isDouble(lineForOne.getZPointLng())) {
                        if (String.valueOf(latLng.longitude).equals(lineForOne.getAPointLng()) && String.valueOf(latLng.latitude).equals(lineForOne.getAPointLat())) {
                            //记下这条线
                            linesBeans.add(lineForOne);

                        } else if (String.valueOf(latLng.longitude).equals(lineForOne.getZPointLng()) && String.valueOf(latLng.latitude).equals(lineForOne.getZPointLat())) {
                            //记下这条线
                            linesBeans.add(lineForOne);
                        }
                    }
//                    }
                }
            }
            //记录所有接下来可能会询的线
            SingleRoute.getInstance().setCurrentLine(linesBeans);

        }
    }


    /**
     * 根据当前位置划线
     */
    private void lineation() {
        double sDistance = convert(AMapUtils.calculateLineDistance(lastLocation, currentLocation));
        if (sDistance >= DIS && sDistance < 180) {

            //交割总距离
            TOTAL_DISTANCE += sDistance;
            SingleRoute.getInstance().setLength(TOTAL_DISTANCE + "");

            latLngs.add(currentLocation);

            //latLngs.add(lastLocation);
            Polyline polyline = mAmap.addPolyline(new PolylineOptions().
                    addAll(latLngs).width(10).color(Color.argb(255, 21, 144, 226)));
            polylines.add(polyline);

            latLngs.remove(0);
            //记录上一个点
            lastLocation = null;
            lastLocation = latLngC;
        }
    }


    /**
     * 查询出所有的路由数据
     */

//    private int myNum = -1;
    private int myI = 0;

    private void queryAllData() {
        if (ToolUtil.isEmpty(mlxResEntities)) {

            db.myDao().loadAllLinesNew()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<LXResNewEntity.LinesNewBean>>() {
                        @Override
                        public void accept(List<LXResNewEntity.LinesNewBean> linesBeans) {
                            if(ToolUtil.isEmpty(mlxResEntities)) {
                                LXResNewEntity lxResEntitie = new LXResNewEntity();
                                mlxResEntities = new ArrayList<>();
                                mlxResEntities.add(lxResEntitie);
                                try {
                                    mlxResEntities.get(0).setLines(linesBeans);
                                } catch (Exception e) {
                                }
                                //最后去匹配80米内的点资源
                                if (myI == mlxResEntities.size() - 1) {
                                    isRangeHave(mlxResEntities);
                                }
                            }



                        }
                    });


        } else {
            isRangeHave(mlxResEntities);
        }


    }


    public void changeRest(){
        db.myDao().loadAllLinesNew()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LXResNewEntity.LinesNewBean>>() {
                    @Override
                    public void accept(List<LXResNewEntity.LinesNewBean> linesBeans) {
                        if(ToolUtil.isEmpty(mlxResEntities)) {
                            LXResNewEntity lxResEntitie = new LXResNewEntity();
                            mlxResEntities = new ArrayList<>();
                            mlxResEntities.add(lxResEntitie);
                            try {
                                mlxResEntities.get(0).setLines(linesBeans);
                            } catch (Exception e) {
                            }
                            if (myI == mlxResEntities.size() - 1) {
                                ToastUtil.showToast(mContext,"已重新匹配",ToastUtil.TOAST_TYPE_WARNING);
                                isRangeHave(mlxResEntities);
                            }
                        }
                    }
                });
    }


    /**
     * 查询80米内有无点资源
     */


    private List<LXResNewEntity.LinesNewBean> isRangeHave(List<LXResNewEntity> lxResEntities) {

        List<LXResNewEntity.LinesNewBean> linesBeans = new ArrayList<>();
        double a, b, c;
        double nearDis = 0;
        LatLng nearLatLng = null;
        for (int i = 0; i < lxResEntities.size(); i++) {
            LXResNewEntity tmList = lxResEntities.get(i);
            for (int j = 0; j < tmList.getLines().size(); j++) {
                LXResNewEntity.LinesNewBean lineForOne = tmList.getLines().get(j);
                if (StringUtils.isDouble(lineForOne.getAPointLat()) && StringUtils.isDouble(lineForOne.getAPointLng())
                        && StringUtils.isDouble(lineForOne.getZPointLat()) && StringUtils.isDouble(lineForOne.getZPointLng())) {

                    LatLng latLngMarkerA;
                    LatLng latLngMarkerZ;
                    latLngMarkerA = new LatLng(StringUtils.toDouble(lineForOne.getAPointLat()), StringUtils.toDouble(lineForOne.getAPointLng()));

                    latLngMarkerZ = new LatLng(StringUtils.toDouble(lineForOne.getZPointLat()), StringUtils.toDouble(lineForOne.getZPointLng()));

                    //距离a点
                    a = convert(AMapUtils.calculateLineDistance(latLngMarkerA, currentLocation));
                    //距离z点
                    b = convert(AMapUtils.calculateLineDistance(latLngMarkerZ, currentLocation));

                    if (nearDis == 0) {
                        if (a <= heightRange) {
                            nearDis = a;
                            nearLatLng = latLngMarkerA;
                        } else if (b <= heightRange) {
                            nearLatLng = latLngMarkerZ;
                            nearDis = b;
                        }
                    } else if (nearDis > a) {
                        nearLatLng = latLngMarkerA;
                        nearDis = a;
                    } else if (nearDis > b) {
                        nearLatLng = latLngMarkerZ;
                        nearDis = b;
                    }
                }
            }
        }


        LatLng lastL = SingleRoute.getInstance().getMateLatLng();

        if(lastL != null && lastL.latitude == nearLatLng.latitude && lastL.longitude == nearLatLng.longitude){
            ToastUtil.showToast(mContext,"再次匹配到上一个资源点",ToastUtil.TOAST_TYPE_WARNING);
            return null;
        }

        SingleRoute.getInstance().setMateLatLng(nearLatLng);

        if (nearLatLng != null) {
            //上面算出最近的点，这里算出最近的线
            for (int i = 0; i < lxResEntities.size(); i++) {
                LXResNewEntity tmList = lxResEntities.get(i);
                for (int j = 0; j < tmList.getLines().size(); j++) {
                    LXResNewEntity.LinesNewBean lineForOne = tmList.getLines().get(j);
//                    if(!StringUtils.isEmpty(lineForOne.getStatus()) && !lineForOne.getStatus().equals("1")) {
                    if (StringUtils.isDouble(lineForOne.getAPointLat()) && StringUtils.isDouble(lineForOne.getAPointLng())
                            && StringUtils.isDouble(lineForOne.getZPointLat()) && StringUtils.isDouble(lineForOne.getZPointLng())) {
                        LatLng latLngMarkerA;
                        LatLng latLngMarkerZ;
                        latLngMarkerA = new LatLng(StringUtils.toDouble(lineForOne.getAPointLat()), StringUtils.toDouble(lineForOne.getAPointLng()));

                        latLngMarkerZ = new LatLng(StringUtils.toDouble(lineForOne.getZPointLat()), StringUtils.toDouble(lineForOne.getZPointLng()));

                        if (nearLatLng.latitude == latLngMarkerA.latitude && nearLatLng.longitude == latLngMarkerA.longitude) {
                            linesBeans.add(lineForOne);
                        } else if (nearLatLng.latitude == latLngMarkerZ.latitude && nearLatLng.longitude == latLngMarkerZ.longitude) {
                            linesBeans.add(lineForOne);
                        }
                    }
//                    }
                }
            }
        }


        if (!ToolUtil.isEmpty(linesBeans)) {
            SingleRoute.getInstance().setCurrentLine(linesBeans);
//            nearRes(linesBeans);
        } else {
            ToastUtil.showToast(mContext, "80米内没有资源,请移步到有靠近任务点的位置", ToastUtil.TOAST_TYPE_WARNING);
        }
        return linesBeans;
    }


    /**
     * 在算出第一条匹配的线时，同时算出两端点有没有80米之内的,有则记录
     */
//    private LatLng latLngA, latLngZ;
//    private void nearRes(LXResNewEntity.LinesNewBean tmList) {
//
//        double a, b;
//        latLngA = null;
//        latLngA = new LatLng(StringUtils.toDouble(tmList.getAPointLat()), StringUtils.toDouble(tmList.getAPointLng()));
//
//        latLngZ = null;
//        latLngZ = new LatLng(StringUtils.toDouble(tmList.getZPointLat()), StringUtils.toDouble(tmList.getZPointLng()));
//
//        //距离a点
//        a = convert(AMapUtils.calculateLineDistance(latLngA, currentLocation));
//        //距离z点
//        b = convert(AMapUtils.calculateLineDistance(latLngZ, currentLocation));
//        if (a <= heightRange) {
//            SingleRoute.getInstance().setMateLatLng(latLngA);
//        } else if (b <= heightRange) {
//            SingleRoute.getInstance().setMateLatLng(latLngZ);
//        }
//    }

    /* *//**
     * 计算高度
     *
     * @return
     *//*
    private MyWayBean myWayBean;
    private LatLng startLocation;
    private LXResNewEntity.LinesNewBean endLine;
    private LatLng myEndLocation;
    private double chDis = 0;
    private String mAZ = "A";

    private double calculateHeight(LatLng latLng) {
        double a, b, c;
        LatLng endLocation;
        List<LXResNewEntity.LinesNewBean> mcBoths = SingleRoute.getInstance().getmCBothLines();

        LXResNewEntity.LinesNewBean mcline = SingleRoute.getInstance().getmCNearLine();

        final String az = SingleRoute.getInstance().getAORZ();

//        String cId = SingleRoute.getInstance().getStrUUID();
        if (mcline != null) {
        } else {
            mcline = SingleRoute.getInstance().getEndLine();
        }


        //确定起点的经纬度
        if (!StringUtils.isEmpty(az)) {
            if (az.equals("A")) {
                if (StringUtils.isDouble(mcline.getAPointLng()) && StringUtils.isDouble(mcline.getAPointLat())) {
                    startLocation = new LatLng(Double.parseDouble(mcline.getAPointLat()), Double.parseDouble(mcline.getAPointLng()));
                } else {
                }
            } else {
                if (StringUtils.isDouble(mcline.getZPointLng()) && StringUtils.isDouble(mcline.getZPointLat())) {
                    startLocation = new LatLng(Double.parseDouble(mcline.getZPointLat()), Double.parseDouble(mcline.getZPointLng()));
                }
            }
        }
        chDis = 0;
        //确定多个端点的经纬度
        for (int i = 0; i < mcBoths.size(); i++) {
            if (mcBoths.get(i).getAPointLat().equals(startLocation.latitude + "") && mcBoths.get(i).getAPointLng().equals(startLocation.longitude + "")) {
            } else {
                if (StringUtils.isDouble(mcBoths.get(i).getAPointLat()) && StringUtils.isDouble(mcBoths.get(i).getAPointLat())) {
                    //对端为a
                    endLocation = new LatLng(Double.parseDouble(mcBoths.get(i).getAPointLat()), Double.parseDouble(mcBoths.get(i).getAPointLng()));
                    double sDistance = convert(AMapUtils.calculateLineDistance(latLng, endLocation));
                    if (chDis == 0) {
                        mAZ = "Z";
                        endLine = mcBoths.get(i);
//                        SingleRoute.getInstance().setAORZ("A");
                        chDis = sDistance;
                        myEndLocation = endLocation;

                    } else {
                        if (chDis > sDistance) {
                            mAZ = "Z";
                            endLine = mcBoths.get(i);
//                            SingleRoute.getInstance().setAORZ("A");
                            chDis = sDistance;
                            myEndLocation = endLocation;
                        }
                    }
                }
            }

            if (mcBoths.get(i).getZPointLat().equals(startLocation.latitude + "") && mcBoths.get(i).getZPointLng().equals(startLocation.longitude + "")) {
            } else {
                if (StringUtils.isDouble(mcBoths.get(i).getZPointLat()) && StringUtils.isDouble(mcBoths.get(i).getZPointLat())) {
                    //对端为z
                    endLocation = new LatLng(Double.parseDouble(mcBoths.get(i).getZPointLat()), Double.parseDouble(mcBoths.get(i).getZPointLng()));
                    double sDistance = convert(AMapUtils.calculateLineDistance(latLng, endLocation));

                    if (chDis == 0) {
                        mAZ = "A";
                        endLine = mcBoths.get(i);
//                        SingleRoute.getInstance().setAORZ("Z");
                        chDis = sDistance;
                        myEndLocation = endLocation;
                    } else {
                        if (chDis > sDistance) {
                            mAZ = "A";
                            endLine = mcBoths.get(i);
//                            SingleRoute.getInstance().setAORZ("Z");
                            chDis = sDistance;
                            myEndLocation = endLocation;
                        }
                    }
                }
            }
        }

        a = convert(AMapUtils.calculateLineDistance(startLocation, currentLocation));
        //b 判断是否已距离终点80米之内
        b = convert(AMapUtils.calculateLineDistance(myEndLocation, currentLocation));
        c = convert(AMapUtils.calculateLineDistance(myEndLocation, startLocation));
        double p = (a + b + c) / 2;
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));

        ToastUtil.showToast(mContext, b + "==与对端点距离/计算对端点:" + endLine.getName(), ToastUtil.TOAST_TYPE_WARNING);

        //将轨迹点存起来
        myWayBean = null;
        myWayBean = new MyWayBean();
        myWayBean.setcHeight(2 * s / c);
        myWayBean.setLat(currentLocation.latitude);
        myWayBean.setLon(currentLocation.longitude);
        myWayBean.setLineId(mcline.getUuid());
        myWayBean.setRouteId(SingleRoute.getInstance().getmCLXResNewEntity().getRouteUUID());
        myWayBeanList.add(myWayBean);
        int tNum = 0;
        if (b < 30) {//距离端点多远  80
            if (!ToolUtil.isEmpty(myWayBeanList)) {
                for (int i = 0; i < myWayBeanList.size(); i++) {
                    if (myWayBeanList.get(i).getcHeight() < 100) { //高度不能超过100
                        tNum += 1;
                    }
                }

                int ratio = tNum / myWayBeanList.size() * 100;
                myWayBean.setMatchingRatio(ratio);
                if (ratio >= 80) {
                    //如果通过率为80以上，则修改线的颜色
                    //修改数据库
                    endLine.setStatus("1");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int aaa = db.myDao().updateLines(endLine);
                        }
                    }).start();
                }

                SingleRoute.getInstance().setEndLine(endLine);
                SingleRoute.getInstance().setmCNearLine(endLine);
                SingleRoute.getInstance().setStrUUID(endLine.getUuid());

                if ((myEndLocation.latitude + "").equals(endLine.getAPointLat()) && (myEndLocation.longitude + "").equals(endLine.getAPointLng())) {
                    SingleRoute.getInstance().setAORZ("A");
                } else {
                    SingleRoute.getInstance().setAORZ("Z");
                }
                //SingleRoute.getInstance().setAORZ(mAZ);

                //回调,重新请求数据
                if (onPointBackListener != null) {
                    onPointBackListener.onUpdateLine(endLine.getUuid());
                }
                //初始化起点

                //初始化对端点
            }

            //存入Room
            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.myDao().insertWay(myWayBean);
                    myWayBeanList.clear();
                }
            }).start();


//            db.myDao().loadAllLines(SingleRoute.getInstance().getmCLXResNewEntity().getRouteUUID())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(Schedulers.io())
//                    .subscribe(new Consumer<List<LXResNewEntity.LinesNewBean>>() {
//                        @Override
//                        public void accept(List<LXResNewEntity.LinesNewBean> linesBeans) throws Exception {
//                            //再去查两端点
//                            getAndSaveRoute(myEndLocation,linesBeans);
//                        }
//                    });
        }
        return 2 * s / c;
    }


    */

    /**
     * 遍历出最近的点，找到最近点所在的路由，并将匹配到的路由存到Room
     *//*
    private LatLng currentLatLng;
    private LatLng resLatLng;
    private double nearestNum = 0;
    private String AORZ = "A";
    private LXResNewEntity mCLXResNewEntity;//当前的路由
    private LXResNewEntity.LinesNewBean mCNearLine;//最近的点

    private void getAndSaveRoute(LatLng latLng, List<LXResNewEntity.LinesNewBean> lxResEntities) {
        if (db == null) {
            db = Room.databaseBuilder(mContext.getApplicationContext(), AppDatabase.class, "database-name").build();
        }

        //这里有问题 需要改
//        if (!ToolUtil.isEmpty(lxResEntities)) {
//           for(int i = 0;i < lxResEntities.size();i++){
//               if(lxResEntities.get(i).getAPointLat().equals(latLng.latitude+"") && lxResEntities.get(i).getAPointLng().equals(latLng.longitude+"")){
//                   mCNearLine = lxResEntities.get(i);
//                   AORZ = "A";
//                   SingleRoute.getInstance().setmCNearLine(mCNearLine);
//               }
//               if(lxResEntities.get(i).getZPointLat().equals(latLng.latitude+"") && lxResEntities.get(i).getZPointLng().equals(latLng.longitude+"")){
//                   mCNearLine = lxResEntities.get(i);
//                   AORZ = "Z";
//                   SingleRoute.getInstance().setmCNearLine(mCNearLine);
//               }
//           }
//        } else {
//            return;
//        }
//
//        SingleRoute.getInstance().setAORZ(AORZ);


    }


    private void getAdress(final Context context, final Location location) {

        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                SPUtil ss = new SPUtil(mContext, SPUtil.USER);
                RegeocodeAddress aAdress = regeocodeResult.getRegeocodeAddress();
                String adressStr = aAdress.getCountry()
                        + aAdress.getProvince()
                        + aAdress.getCity()
                        + aAdress.getDistrict()
                        + aAdress.getStreetNumber().getStreet()
                        + aAdress.getStreetNumber().getNumber();
                ss.putString(SPUtil.LOCATION_ADRESS, adressStr);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        LatLonPoint latLonPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }*/
    private double convert(float value) {
        long l1 = Math.round(value * 100); //四舍五入
        double ret = l1 / 100.0; //注意:使用 100.0 而不是 100
        return ret;
    }


    //可在其中解析amapLocation获取相应内容。
                        /*amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        amapLocation.getLatitude();//获取纬度
                        amapLocation.getLongitude();//获取经度
                        amapLocation.getAccuracy();//获取精度信息
                        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        amapLocation.getCountry();//国家信息
                        amapLocation.getProvince();//省信息
                        amapLocation.getCity();//城市信息
                        amapLocation.getDistrict();//城区信息
                        amapLocation.getStreet();//街道信息
                        amapLocation.getStreetNum();//街道门牌号信息
                        amapLocation.getCityCode();//城市编码
                        amapLocation.getAdCode();//地区编码
                        amapLocation.getAoiName();//获取当前定位点的AOI信息
                        amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                        amapLocation.getFloor();//获取当前室内定位的楼层
                        amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
                        //获取定位时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(amapLocation.getTime());
                        df.format(date);*/

    /**
     * 添加一条当前位置点
     *
     * @param recordLocationEntity
     */
//    private void saveLocation(final LocusPoint recordLocationEntity){
//        Observable.create(new ObservableOnSubscribe<LocusPoint>() {
//
//            @Override
//            public void subscribe(ObservableEmitter<LocusPoint> emitter) throws Exception {
//                db.myDao().insertOneLocation(recordLocationEntity);
//                emitter.onNext(recordLocationEntity);
//                emitter.onComplete();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<LocusPoint>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(LocusPoint strings) {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//                });
//    }


    private OnCallBackListener onCallBackListener;

    public void setOnCallback(OnCallBackListener onItemClickListener) {
        this.onCallBackListener = onItemClickListener;
    }

    public interface OnCallBackListener {
        void onCallBack(String province, String city, String district, String aCode);

    }


    private OnPointBackListener onPointBackListener;

    public void setOnPointCallback(OnPointBackListener onItemClickListener) {
        this.onPointBackListener = onItemClickListener;

    }

    public interface OnPointBackListener {
        void onCallBack(LatLng latLng);

        void onMAXDISTANCECallBack(String hint);

        void onDeleteRouteCallBack(int routeId);

        void onUpdateLine(String lineUUID);
    }


    /**
     * 开始交割弹窗
     */
   /* private void selectDialog(String content) {

        SweetAlertDialog pDialogSuccess;
        try {
            pDialogSuccess = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
            pDialogSuccess.setTitleText("提示");
            pDialogSuccess.setContentText(content);
            pDialogSuccess.setCancelable(true);
            pDialogSuccess.setConfirmText("选择");
            pDialogSuccess.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    ToastUtil.showToast(mContext, "请选择起点", ToastUtil.TOAST_TYPE_WARNING);
                    sweetAlertDialog.dismiss();

                }
            });
            pDialogSuccess.show();

        } catch (Exception e) {
        }
    }*/


    /* private void getLBSTraceClient() {

     *//**
     * 直接进行轨迹纠偏
     *//*
        LBSTraceClient lbsTraceClient = LBSTraceClient.getInstance(mContext);
        lbsTraceClient.startTrace(new TraceStatusListener() {
            @Override
            public void onTraceStatus(List<TraceLocation> list, List<LatLng> list1, String s) {
                EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
                if (!ToolUtil.isEmpty(list1)) {
                    ToastUtil.showToast(mContext, "返回:" + list1.size() + "个点" + s, ToastUtil.TOAST_TYPE_WARNING);
                }
                mAmap.addPolyline(new PolylineOptions().
                        addAll(list1).width(10).color(Color.argb(255, 21, 144, 226)));
            }
        });
*/


        /*if(ToolUtil.isEmpty(traceLocationList)) {
            if(amapLocation != null){
                traceLocation = new TraceLocation();
                traceLocation.setTime(amapLocation.getTime());
                traceLocation.setBearing(amapLocation.getBearing());
                traceLocation.setSpeed(amapLocation.getSpeed());
                traceLocation.setLatitude(amapLocation.getLatitude());
                traceLocation.setLongitude(amapLocation.getLongitude());
                traceLocationList.add(traceLocation);
            }

        }else {
            if(amapLocation != null){
                traceLocation = new TraceLocation();
                traceLocation.setTime(amapLocation.getTime());
                traceLocation.setBearing(amapLocation.getBearing());
                traceLocation.setSpeed(amapLocation.getSpeed());
                traceLocation.setLatitude(amapLocation.getLatitude());
                traceLocation.setLongitude(amapLocation.getLongitude());
                traceLocationList.add(traceLocation);
            }



            LBSTraceClient mTraceClient = new LBSTraceClient(mContext);
            mTraceClient.queryProcessedTrace(1, traceLocationList,
                    LBSTraceClient.TYPE_AMAP, new TraceListener() {
                        @Override
                        public void onRequestFailed(int i, String s) {
                            Toast.makeText(mContext, "纠偏失败"+s,
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onTraceProcessing(int i, int i1, List<LatLng> list) {

                        }

                        @Override
                        public void onFinished(int i, List<LatLng> list, int i1, int i2) {
                            if (ToolUtil.isEmpty(list)) {
                                return;
                            }
                            ToastUtil.showToast(mContext,"本次纠偏总长度"+i1,ToastUtil.TOAST_TYPE_WARNING);


                            mAmap.addPolyline(new PolylineOptions().
                                    addAll(list).width(10).color(Color.argb(255, 21, 144, 226)));
                        }
                    });
        }
    }*/


}

