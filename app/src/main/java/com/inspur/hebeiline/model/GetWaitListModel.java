package com.inspur.hebeiline.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.gson.GsonBuilder;
import com.inspur.hebeiline.R;
import com.inspur.hebeiline.core.AllUrl;
import com.inspur.hebeiline.core.Constance;
import com.inspur.hebeiline.core.MallRequest;
import com.inspur.hebeiline.entity.LXWaitListEntity;
import com.inspur.hebeiline.entity.MyLatLng;
import com.inspur.hebeiline.entity.TMLineMainEntity;
import com.inspur.hebeiline.entity.eventbean.MyEvent;
import com.inspur.hebeiline.entity.post_bean.LXGetResPostBean;
import com.inspur.hebeiline.utils.base.BaseViewModel;
import com.inspur.hebeiline.utils.tools.SPUtil;
import com.inspur.hebeiline.utils.tools.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixu on 2018/1/23.
 *
 * @author lixu
 */

public class GetWaitListModel extends BaseViewModel {
    private boolean isRequesting = false;

    public GetWaitListModel(Application application) {
        super(application);
    }

    private MutableLiveData<List<LXWaitListEntity>> roundSiteList;
    private Context mContext;


    public MutableLiveData<List<LXWaitListEntity>> getCurrentData(Context context) {
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
    public void getNearRes(MallRequest mRequest) {

        SPUtil spUtil = new SPUtil(mContext,SPUtil.USER);
        String cId = spUtil.getString("county_uid","");

        String headInfo = "Bearer " + spUtil.getString(SPUtil.USER_TOKEN,"");
        EventBus.getDefault().post(new MyEvent(MyEvent.OKK));
        Log.d("qqqqq","传参==headInfo="+headInfo);

        Map<String,String> map = new HashMap<>();
        map.put("Content-Type","application/json;charset=UTF-8");
        map.put("Authorization",headInfo);




        String url = AllUrl.mainUrl + AllUrl.getTaskToDo+"?countyId="+cId+"&userId=T400004238";
        Log.d("qqqq","查询待办=="+url);
        showProgressDialog(mContext,"正在加载数据...");
        mRequest.getTaskToDo(map
                , url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LXWaitListEntity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if(isRequesting) {
                            d.dispose();
                        }
                        isRequesting = true;
                    }

                    @Override
                    public void onNext(List<LXWaitListEntity> roundSiteEntity) {

                        String jsonRequest = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss Z").create().toJson(roundSiteEntity);

                        Log.d("qqqqqq", "roundSiteEntity==" + jsonRequest);
                        if (roundSiteList == null) {
                            roundSiteList = new MutableLiveData<>();
                        }
                        if (roundSiteEntity != null) {
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
