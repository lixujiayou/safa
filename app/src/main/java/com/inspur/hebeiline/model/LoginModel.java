package com.inspur.hebeiline.model;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.inspur.hebeiline.R;
import com.inspur.hebeiline.core.AllUrl;
import com.inspur.hebeiline.core.Constance;
import com.inspur.hebeiline.core.MallRequest;
import com.inspur.hebeiline.entity.LXTestLoginBean;
import com.inspur.hebeiline.entity.UserNamePwdBean;
import com.inspur.hebeiline.entity.eventbean.MyEvent;
import com.inspur.hebeiline.utils.base.BaseViewModel;
import com.inspur.hebeiline.utils.tools.SPUtil;
import com.inspur.hebeiline.utils.tools.StringUtils;
import com.inspur.hebeiline.utils.tools.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixu on 2018/1/23.
 *
 * @author lixu
 */

public class LoginModel extends BaseViewModel {


    public LoginModel(Application application) {
        super(application);
    }

    private MutableLiveData<LXTestLoginBean> roundSiteList;
    private Context mContext;


    public MutableLiveData<LXTestLoginBean> getCurrentData(Context context) {
        this.mContext = context;
        if (roundSiteList == null) {
            roundSiteList = new MutableLiveData<>();
        }

        return roundSiteList;
    }

    /**
     * @param mRequest
     */
    public void login(MallRequest mRequest, String useraccount, String password) {
        if (roundSiteList == null) {
            roundSiteList = new MutableLiveData<>();
        }
        showProgressDialog("正在登录...");
        UserNamePwdBean userNamePwdBean = new UserNamePwdBean();
        userNamePwdBean.setUsername("admin");
        userNamePwdBean.setPassword("admin");
        EventBus.getDefault().post(new MyEvent(MyEvent.OKK));
        Log.d("qqqqqq","登录地址"+userNamePwdBean);
        mRequest.login(userNamePwdBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LXTestLoginBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(LXTestLoginBean roundSiteEntity) {
                        Log.d("qqqqqq","登录返回=="+roundSiteEntity.toString());

                        Log.d("qqqqq","第一步");

                        if(!StringUtils.isEmpty(roundSiteEntity.getId_token())){
                          //  ToastUtil.showToast(mContext,"登录成功",ToastUtil.TOAST_TYPE_SUCCESS);
                            SPUtil spUtil = new SPUtil(mContext,SPUtil.USER);
                            spUtil.putString(SPUtil.USER_TOKEN,roundSiteEntity.getId_token());
                            if (roundSiteList == null) {
                                roundSiteList = new MutableLiveData<>();
                            }
                            Log.d("qqqqq","第2步"+roundSiteEntity.getId_token());

                            roundSiteList.postValue(roundSiteEntity);
                        }else{
                            ToastUtil.showToast(mContext,"登录异常",ToastUtil.TOAST_TYPE_WARNING);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        EventBus.getDefault().post(new MyEvent(MyEvent.NOO));
                        Log.d("qqqqqq","登录Constance.getMsgByException(e)=="+Constance.getMsgByException(e));
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
