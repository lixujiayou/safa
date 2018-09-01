package com.inspur.hebeiline.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.location.Location;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.inspur.hebeiline.entity.eventbean.MyEvent;
import com.inspur.hebeiline.entity.tmline.LocusPoint;
import com.inspur.hebeiline.utils.base.BaseViewModel;
import com.inspur.hebeiline.utils.tools.SPUtil;
import com.inspur.hebeiline.utils.tools.ToolUtil;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixu on 2018/1/23.
 */

public class CurrentLocationMapModel extends BaseViewModel {
    public CurrentLocationMapModel(Application application) {
        super(application);
    }

    private MutableLiveData<Location> locationMutableLiveData;


    private MutableLiveData<List<LocusPoint>> locationList;
    private List<LocusPoint> mLocations = new ArrayList<>();

    private AMap mAmap;
    private Context mContext;
    private MyLocationStyle myLocationStyle;
    private LatLng lastLocation;
    private LatLng currentLocation;
    private int DIS = 40;
    private int TOTAL_DISTANCE = 0;//总距离
    private int MAX_DISTANCE = 1000;//最大距离



    public MutableLiveData<Location> getlocationMutableLiveData(){
        if(locationMutableLiveData == null){
            locationMutableLiveData = new MutableLiveData<>();
        }
        if(locationList == null){
            locationList = new MutableLiveData<>();
        }
        return locationMutableLiveData;
    }
    public MutableLiveData<List<LocusPoint>> getlocationListMutableLiveData(){
        if(locationMutableLiveData == null){
            locationMutableLiveData = new MutableLiveData<>();
        }
        if(locationList == null){
            locationList = new MutableLiveData<>();
        }
        return locationList;
    }




    public void getLiveData(){
        if(locationMutableLiveData == null){
            locationMutableLiveData = new MutableLiveData<>();
        }
        locationMutableLiveData.postValue(locationMutableLiveData.getValue());
    }


    public void getLocationListLiveData(){
        if(locationList == null){
            locationList = new MutableLiveData<>();
        }
        if(!ToolUtil.isEmpty(mLocations)){
            locationList.postValue(mLocations);
        }

    }

    //是否只定位一次
    public void getCurrentLocation(final Context context, AMap aMap, final boolean isFirst){
        EventBus.getDefault().post(new MyEvent(MyEvent.OKK));
        this.mContext = context;
        this.mAmap = aMap;
        //DialogUtil.getInstance().showProgressDialog(mContext, "正在定位当前位置...");
        if(myLocationStyle == null) {
            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        }
        myLocationStyle.interval(10000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//只定位一次。
        //mAmap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //mAmap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
        mAmap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAmap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(locationMutableLiveData == null){
                    locationMutableLiveData = new MutableLiveData<>();
                }

                //location.setLatitude(36.65447439197);
                //location.setLongitude(116.98875715748);
                //location.setLatitude(36.51465);
                //location.setLongitude(117.01165);
                if(isFirst){
                }else {
                }

                EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
            }
        });
    }



    private void getAdress(final Context context, final Location location){

        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                SPUtil ss = new SPUtil(mContext,SPUtil.USER);
                RegeocodeAddress aAdress = regeocodeResult.getRegeocodeAddress();
                String adressStr = aAdress.getCountry()
                        + aAdress.getProvince()
                        + aAdress.getCity()
                        + aAdress.getDistrict()
                        + aAdress.getStreetNumber().getStreet()
                        + aAdress.getStreetNumber().getNumber();
                ss.putString(SPUtil.LOCATION_ADRESS,adressStr);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        LatLonPoint latLonPoint = new LatLonPoint(location.getLatitude(),location.getLongitude());
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    private double convert(float value){
        long l1 = Math.round(value*100); //四舍五入
        double ret = l1/100.0; //注意:使用 100.0 而不是 100
        return ret;
    }


     /*Log.d("qqq", "initMapData定位结束" + location.getLatitude() + "==" + location.getLongitude());
                    locationMutableLiveData.setValue(location);

                    if (lastLocation == null) {
        //修改为多次定位
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        mAmap.setMyLocationStyle(myLocationStyle);
        //第一次不计算距离，直接add到轨迹点
        lastLocation = new LatLng(location.getLatitude(), location.getLongitude());
        RecordLocationEntity recordLocationEntity = new RecordLocationEntity();
        recordLocationEntity.setLatLng(lastLocation);
        mLocations.add(recordLocationEntity);
        Log.d("qqqq", "第一次直接add");
    } else {
        if (currentLocation != null) {
            currentLocation = null;
        }

        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        double sDistance = convert(AMapUtils.calculateLineDistance(lastLocation, currentLocation));


        Log.d("qqqq", "计算距离==" + sDistance);

        //和上一个点距离大于DIS才会记录，一直站着不动不会记录
        if (sDistance > DIS) {

            //交割距离
            TOTAL_DISTANCE += DIS;
            Log.d("qqqq", "记录总距离==" + TOTAL_DISTANCE);
            //到达最大距离
            if (TOTAL_DISTANCE >= MAX_DISTANCE) {
                return;
            }
            Log.d("qqqq", "addList");
            //记录轨迹点加的list中
            RecordLocationEntity recordLocationEntity = new RecordLocationEntity();
            recordLocationEntity.setLatLng(lastLocation);
            mLocations.add(recordLocationEntity);

            //记录上一个点
            lastLocation = null;
            lastLocation = currentLocation;
        }
    }*/
}
