package com.inspur.hebeiline.utils.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.inspur.hebeiline.R;
import com.inspur.hebeiline.core.MallRequest;
import com.inspur.hebeiline.entity.eventbean.NetWorkEvent;
import com.inspur.hebeiline.utils.converter.ServiceGenerator;
import com.inspur.hebeiline.utils.tools.NetWorkUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by lixu on 2016/11/29.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private BroadcastReceiver netStateReceiver;
   // public LinearLayout ll_netWork;
    public Toolbar mToolbar;
    public MallRequest mRequestClient;
    public boolean isSHOW;//记录本Activity是否可视区域

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(savedInstanceState);
        isSHOW = true;
        EventBus.getDefault().register(this);
        mRequestClient = ServiceGenerator.createService(MallRequest.class);
        initNetWork();
        mToolbar = findViewById(R.id.toolbar);
        if(mToolbar != null) {
            mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        }
        initViews();
        if(mToolbar != null) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isSHOW = true;
    }

    // 初始化UI，setContentView
    protected abstract void initContentView(Bundle savedInstanceState);
    public abstract void initViews();
    public abstract void initData();

    @Override
    public void onClick(View view) {
    }

    @Override
    protected void onStop() {
        super.onStop();
        isSHOW = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSHOW = false;
        EventBus.getDefault().unregister(this);
        overridePendingTransition(R.anim.anim_none, R.anim.trans_center_2_right);
        unregisterReceiver(netStateReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isSHOW = false;
    }


    public void initNetWork(){
        netStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(
                        ConnectivityManager.CONNECTIVITY_ACTION)) {
                    if (NetWorkUtil.isNetWorkConnected(BaseActivity.this)) {
                        EventBus.getDefault().post(new NetWorkEvent(NetWorkEvent.AVAILABLE));
                    } else {
                        EventBus.getDefault().post(new NetWorkEvent(NetWorkEvent.UNAVAILABLE));
                    }
                }
            }
        };

        registerReceiver(netStateReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Subscribe
    public void onEvent(NetWorkEvent event) {
        if (event.getType() == NetWorkEvent.UNAVAILABLE) {
           // ll_netWork.setVisibility(View.VISIBLE);
        }else{
           // ll_netWork.setVisibility(View.GONE);
        }
    }

}
