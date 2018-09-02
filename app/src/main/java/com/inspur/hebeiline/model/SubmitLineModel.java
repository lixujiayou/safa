package com.inspur.hebeiline.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inspur.hebeiline.R;
import com.inspur.hebeiline.core.AllUrl;
import com.inspur.hebeiline.core.Constance;
import com.inspur.hebeiline.core.MallRequest;
import com.inspur.hebeiline.entity.LXWaitListEntity;
import com.inspur.hebeiline.entity.LineResultEntity;
import com.inspur.hebeiline.entity.eventbean.MyEvent;
import com.inspur.hebeiline.utils.base.BaseViewModel;
import com.inspur.hebeiline.utils.tools.SPUtil;
import com.inspur.hebeiline.utils.tools.StringUtils;
import com.inspur.hebeiline.utils.tools.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
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

public class SubmitLineModel extends BaseViewModel {
    private boolean isRequesting = false;

    public SubmitLineModel(Application application) {
        super(application);
    }

    private MutableLiveData<Object> roundSiteList;
    private Context mContext;


    public MutableLiveData<Object> getCurrentData(Context context) {
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
    public void submit(MallRequest mRequest,List<LineResultEntity> listMy) {

        SPUtil spUtil = new SPUtil(mContext,SPUtil.USER);
        String headInfo = "Bearer " + spUtil.getString(SPUtil.USER_TOKEN,"");
//        EventBus.getDefault().post(new MyEvent(MyEvent.OKK));
        Log.d("qqqqq","传参==headInfo="+headInfo);

        Map<String,String> map = new HashMap<>();
        map.put("Content-Type","application/json;charset=UTF-8");
        map.put("Authorization",headInfo);
        Gson gson = new Gson();

        Log.d("qqqqqq","===="+gson.toJson(listMy));

        showProgressDialog(mContext,"正在提交...");
        mRequest.updateTaskRecord(map
                , listMy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if(isRequesting) {
                            d.dispose();
                        }
                        isRequesting = true;
                    }

                    @Override
                    public void onNext(Object roundSiteEntity) {
                        String jsonRequest = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss Z").create().toJson(roundSiteEntity);
                        Log.d("qqqqqq", "roundSiteEntity==" + jsonRequest);
                        if (roundSiteList == null) {
                            roundSiteList = new MutableLiveData<>();
                        }
                        if (roundSiteEntity != null) {

                            roundSiteList.postValue(roundSiteEntity);
                        } else {
                            ToastUtil.showToast(mContext, "上传失败", ToastUtil.TOAST_TYPE_WARNING);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                      ToastUtil.showToast(mContext,"本次提交的巡检成功UUID="+lineUUID,ToastUtil.TOAST_TYPE_SUCCESS);
                        dismissProgressDialog();
                        isRequesting = false;
//                        EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
                        if (roundSiteList == null) {
                            roundSiteList = new MutableLiveData<>();
                        }
                        if(e.getMessage().toString().contains("500") || e.toString().contains("End of input at line")){
                             roundSiteList.postValue("");
                            return;
                        }
                        Log.d("qqqqqq","上传失败=="+e.toString());


                        Log.d("qqqqqq", "Constance.getMsgByException(e)==" + Constance.getMsgByException(e));
                        ToastUtil.showToast(mContext, "上传失败:"+Constance.getMsgByException(e), ToastUtil.TOAST_TYPE_WARNING);
                        if (onCallBackListener != null) {
                            onCallBackListener.onErro();
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissProgressDialog();
                        isRequesting = false;
//                        EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
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
