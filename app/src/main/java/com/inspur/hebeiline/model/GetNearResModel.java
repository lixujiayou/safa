package com.inspur.hebeiline.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.inspur.hebeiline.R;
import com.inspur.hebeiline.core.Constance;
import com.inspur.hebeiline.core.MallRequest;
import com.inspur.hebeiline.entity.MyLatLng;
import com.inspur.hebeiline.entity.TMLineMainEntity;
import com.inspur.hebeiline.entity.eventbean.MyEvent;
import com.inspur.hebeiline.entity.post_bean.LXGetResPostBean;
import com.inspur.hebeiline.utils.base.BaseViewModel;
import com.inspur.hebeiline.utils.tools.AMapToWGS;
import com.inspur.hebeiline.utils.tools.SPUtil;
import com.inspur.hebeiline.utils.tools.ToastUtil;
import com.inspur.hebeiline.utils.tools.ToolUtil;

import org.greenrobot.eventbus.EventBus;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by lixu on 2018/1/23.
 *
 * @author lixu
 */

public class GetNearResModel extends BaseViewModel {
    private boolean isRequesting = false;

    public GetNearResModel(Application application) {
        super(application);
    }

    private MutableLiveData<TMLineMainEntity> roundSiteList;
    private Context mContext;


    public MutableLiveData<TMLineMainEntity> getCurrentData(Context context) {
        this.mContext = context;
        if (roundSiteList == null) {
            roundSiteList = new MutableLiveData<>();
        }
        return roundSiteList;
    }

    /**
     * @param mRequest
     * 手机屏幕的左上，右上，右下，左下,四个点
     */
    public void getNearRes(MallRequest mRequest, String likeName,List<MyLatLng> paths) {
       // showProgressDialog(mContext,"初始化...");
//        LatLng cLoca = AMapToWGS.toWGS84Point(lat, lon);
//        lat = cLoca.latitude;
//        lon = cLoca.longitude;
//        Log.d("qqqqqq","传的经纬度"+lat+"，"+lon);

       // String test = "[{\\\"end\\\":{\\\"isPass\\\":\\\"\\\",\\\"latitude\\\":36.661954,\\\"longitude\\\":117.137213,\\\"resourceID\\\":1234545,\\\"resourceName\\\":\\\"2??\\\",\\\"resourceType\\\":\\\"???\\\"},\\\"relatedBranch\\\":0,\\\"start\\\":{\\\"isPass\\\":\\\"\\\",\\\"latitude\\\":36.661854,\\\"longitude\\\":117.130213,\\\"resourceID\\\":1234545,\\\"resourceName\\\":\\\"1??\\\",\\\"resourceType\\\":\\\"???\\\"}},{\\\"end\\\":{\\\"isPass\\\":\\\"\\\",\\\"latitude\\\":36.662054,\\\"longitude\\\":117.138213,\\\"resourceID\\\":1234545,\\\"resourceName\\\":\\\"3??\\\",\\\"resourceType\\\":\\\"???\\\"},\\\"relatedBranch\\\":0,\\\"start\\\":{\\\"isPass\\\":\\\"\\\",\\\"latitude\\\":36.661954,\\\"longitude\\\":117.137213,\\\"resourceID\\\":1234545,\\\"resourceName\\\":\\\"2??\\\",\\\"resourceType\\\":\\\"???\\\"}},{\\\"end\\\":{\\\"isPass\\\":\\\"\\\",\\\"latitude\\\":36.662154,\\\"longitude\\\":117.139213,\\\"resourceID\\\":1234545,\\\"resourceName\\\":\\\"4??\\\",\\\"resourceType\\\":\\\"???\\\"},\\\"relatedBranch\\\":0,\\\"start\\\":{\\\"isPass\\\":\\\"\\\",\\\"latitude\\\":36.662054,\\\"longitude\\\":117.138213,\\\"resourceID\\\":1234545,\\\"resourceName\\\":\\\"3??\\\",\\\"resourceType\\\":\\\"???\\\"}}]";
        //济南测试数据
//        String test = "[{\"end\":{\"isPass\":\"\",\"latitude\":36.661954,\"longitude\":117.137213,\"resourceID\":1234545,\"resourceName\":\"2??\",\"resourceType\":\"???\"},\"relatedBranch\":0,\"start\":{\"isPass\":\"\",\"latitude\":36.661854,\"longitude\":117.130213,\"resourceID\":1234545,\"resourceName\":\"1??\",\"resourceType\":\"???\"}},{\"end\":{\"isPass\":\"\",\"latitude\":36.662054,\"longitude\":117.138213,\"resourceID\":1234545,\"resourceName\":\"3??\",\"resourceType\":\"???\"},\"relatedBranch\":0,\"start\":{\"isPass\":\"\",\"latitude\":36.661954,\"longitude\":117.137213,\"resourceID\":1234545,\"resourceName\":\"2??\",\"resourceType\":\"???\"}},{\"end\":{\"isPass\":\"\",\"latitude\":36.662154,\"longitude\":117.139213,\"resourceID\":1234545,\"resourceName\":\"4??\",\"resourceType\":\"???\"},\"relatedBranch\":0,\"start\":{\"isPass\":\"\",\"latitude\":36.662054,\"longitude\":117.138213,\"resourceID\":1234545,\"resourceName\":\"3??\",\"resourceType\":\"???\"}}]";
//
//        Gson gs = new Gson();
//        List<TMLineMainEntity> jsonListObject = gs.fromJson(test, new TypeToken<List<TMLineMainEntity>>(){}.getType());//把JSON
//        if (roundSiteList == null) {
//            roundSiteList = new MutableLiveData<>();
//        }
//        roundSiteList.postValue(jsonListObject);
//        if(true){
//            return;
//        }
//        SPUtil spUtil = new SPUtil(mContext,SPUtil.USER);
//        String uId = spUtil.getString(SPUtil.USER_UID,"");

        //Bearer
        SPUtil spUtil = new SPUtil(mContext,SPUtil.USER);
        String headInfo = "Bearer " + spUtil.getString(SPUtil.USER_TOKEN,"");
        EventBus.getDefault().post(new MyEvent(MyEvent.OKK));
        Log.d("qqqqq","传参==headInfo="+headInfo);

        Map<String,String> map = new HashMap<>();
        map.put("Content-Type","application/json;charset=UTF-8");
        map.put("Authorization",headInfo);


        Log.d("qqqqq","传参==paths="+paths);

        LXGetResPostBean lxGetResPostBean = new LXGetResPostBean();
        lxGetResPostBean.setCoordtype("gcj02ll");
        lxGetResPostBean.setWhere("rownum <= 20");
        lxGetResPostBean.setIndentify_tpl("indentify_wx2_query");
        lxGetResPostBean.setLayerId("guanglanduan");
        lxGetResPostBean.setOrderByFields("length(name)");
        lxGetResPostBean.setPath(paths);

        mRequest.getMapResList(map
                , lxGetResPostBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TMLineMainEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if(isRequesting) {
                            dismissProgressDialog();
                            d.dispose();
                        }
                        showProgressDialog(mContext,"初始化...");
                        isRequesting = true;
                    }

                    @Override
                    public void onNext(TMLineMainEntity roundSiteEntity) {
                        String jsonRequest = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss Z").create().toJson(roundSiteEntity);

                        Log.d("qqqqqq", "roundSiteEntity==" + jsonRequest);
                        if (roundSiteList == null) {
                            roundSiteList = new MutableLiveData<>();
                        }
                        if (roundSiteEntity != null) {
//                            for(int i = 0;i < roundSiteEntity.size(); i++){
//                                if(roundSiteEntity.get(i).getStart()!= null
//                                        && roundSiteEntity.get(i).getStart().getLongitude() != 0
//                                        && roundSiteEntity.get(i).getStart().getLatitude() != 0){
//                                    LatLng cLoca = AMapToWGS.toGCJ02Piont(mContext,roundSiteEntity.get(i).getStart().getLatitude(), roundSiteEntity.get(i).getStart().getLongitude());
//                                    roundSiteEntity.get(i).getStart().setLongitude(cLoca.longitude);
//                                    roundSiteEntity.get(i).getStart().setLatitude(cLoca.latitude);
//                                }
//
//                                if(roundSiteEntity.get(i).getEnd()!= null
//                                        && roundSiteEntity.get(i).getEnd().getLongitude() != 0
//                                        && roundSiteEntity.get(i).getEnd().getLatitude() != 0){
//                                    LatLng cLoca = AMapToWGS.toGCJ02Piont(mContext,roundSiteEntity.get(i).getEnd().getLatitude(), roundSiteEntity.get(i).getEnd().getLongitude());
//                                    roundSiteEntity.get(i).getEnd().setLongitude(cLoca.longitude);
//                                    roundSiteEntity.get(i).getEnd().setLatitude(cLoca.latitude);
//                                }
//                            }
                            roundSiteList.postValue(roundSiteEntity);
                        } else {
                            ToastUtil.showToast(mContext, "没有查询到数据", ToastUtil.TOAST_TYPE_WARNING);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        isRequesting = false;
                        EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
                        Log.d("qqqqqq", "Constance.getMsgByException(e)==" + Constance.getMsgByException(e));
                        ToastUtil.showToast(mContext, Constance.getMsgByException(e), ToastUtil.TOAST_TYPE_WARNING);
                        if (onCallBackListener != null) {
                            onCallBackListener.onErro();
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissProgressDialog();
                        isRequesting = false;
                        EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
                    }
                });
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
