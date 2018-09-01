package com.inspur.hebeiline.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.inspur.hebeiline.R;
import com.inspur.hebeiline.core.Constance;
import com.inspur.hebeiline.core.MallRequest;
import com.inspur.hebeiline.entity.ACodeBean;
import com.inspur.hebeiline.entity.UserNamePwdBean;
import com.inspur.hebeiline.entity.eventbean.MyEvent;
import com.inspur.hebeiline.entity.post_bean.AcodePost;
import com.inspur.hebeiline.utils.base.BaseViewModel;
import com.inspur.hebeiline.utils.tools.SPUtil;
import com.inspur.hebeiline.utils.tools.StringUtils;
import com.inspur.hebeiline.utils.tools.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
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

public class RegiontransferModel extends BaseViewModel {


    public RegiontransferModel(Application application) {
        super(application);
    }

    private MutableLiveData<ACodeBean> roundSiteList;
    private Context mContext;

    public MutableLiveData<ACodeBean> getCurrentData(Context context) {
        this.mContext = context;
        if (roundSiteList == null) {
            roundSiteList = new MutableLiveData<>();
        }
        return roundSiteList;
    }

    /**
     * @param mRequest
     */
    public void regiontransfer(MallRequest mRequest,String acode) {
        if (roundSiteList == null) {
            roundSiteList = new MutableLiveData<>();
        }
        showProgressDialog("正在转换编码...");
        EventBus.getDefault().post(new MyEvent(MyEvent.OKK));

        SPUtil spUtil = new SPUtil(mContext,SPUtil.USER);
        String headInfo = "Bearer " + spUtil.getString(SPUtil.USER_TOKEN,"");
        Map<String,String> map = new HashMap<>();
        map.put("Content-Type","application/json;charset=UTF-8");
        map.put("Authorization",headInfo);
        AcodePost acodePost = new AcodePost();
        acodePost.setAdCode(acode);
        Log.d("qqqqqq",headInfo+"传过去的是:"+acode);
//        ToastUtil.showToast(mContext,"地理编码=="+acode,ToastUtil.TOAST_TYPE_WARNING);
        mRequest.regiontransfer(map,acodePost)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ACodeBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ACodeBean roundSiteEntity) {
                        Log.d("qqqqqq","登录返回=="+roundSiteEntity.toString());


                        if(roundSiteEntity != null){

                            if (roundSiteList == null) {
                                roundSiteList = new MutableLiveData<>();
                            }
                            Log.d("qqqqq","getCountyUUID"+roundSiteEntity.getCountyUUID());

                            roundSiteList.postValue(roundSiteEntity);
                        }else{
                            ToastUtil.showToast(mContext,"转换编码异常",ToastUtil.TOAST_TYPE_WARNING);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
                        Log.d("qqqqqq","转换Constance.getMsgByException(e)=="+Constance.getMsgByException(e));
                        ToastUtil.showToast(mContext, Constance.getMsgByException(e),ToastUtil.TOAST_TYPE_WARNING);
                        if (onCallBackListener != null) {
                            onCallBackListener.onErro();
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissProgressDialog();
                        EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
                    }
                });
    }


    private OnErroListener onCallBackListener;

    public void setOnErroCallback(OnErroListener onItemClickListener) {
        this.onCallBackListener = onItemClickListener;
    }

    public interface OnErroListener {
        void Success();
        void onErro();
    }


    private SweetAlertDialog pDialog;

    public void showProgressDialog( String title) {
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
