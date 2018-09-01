package com.inspur.hebeiline.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.inspur.hebeiline.R;
import com.inspur.hebeiline.adapter.LXWaitListAdapter;
import com.inspur.hebeiline.entity.LXResNewEntity;
import com.inspur.hebeiline.entity.LXWaitListEntity;
import com.inspur.hebeiline.model.GetTaskLineModel;
import com.inspur.hebeiline.model.GetWaitModel;
import com.inspur.hebeiline.utils.base.BaseActivity;
import com.inspur.hebeiline.utils.tools.StringUtils;
import com.inspur.hebeiline.utils.tools.ToolUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixu on 2018/6/22.
 */

public class LXWaitInfoActivity extends BaseActivity  implements Toolbar.OnMenuItemClickListener{
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<LXWaitListEntity> lxWaitListEntitieList = new ArrayList<>();
    private LXWaitListAdapter lxWaitListAdapter;
    private GetWaitModel getWaitModel;
    private GetTaskLineModel getTaskLineModel;
    private String mmType;

    private Button btStart;

    private SwipeRefreshLayout swipeRefreshLayout;
    private String mUUID;
    private String mRouteId;
    private String mTaskId;
    private LXResNewEntity.LinesNewBean linesBean;

    private int page = 1;
    private  View mHeadView;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wait_info_lx);
    }

    @Override
    public void initViews() {
        mToolbar.setTitle("线路巡检详情");
        mToolbar.setNavigationIcon(R.drawable.icon_cancle);

        recyclerView = findViewById(R.id.recyler);
        btStart = findViewById(R.id.bt_start);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        btStart.setOnClickListener(this);

        Intent gIntent = getIntent();
        mmType = gIntent.getExtras().getString("type");
        if(!StringUtils.isEmpty(mmType) && mmType.equals("map")){
            mUUID = gIntent.getExtras().getString("uuid");
            mRouteId = gIntent.getExtras().getString("routeid");
            linesBean = (LXResNewEntity.LinesNewBean) gIntent.getSerializableExtra("entity");

//            Log.d("qqqqqqq","mRouteId接收资源=="+mRouteId);
        }
        mTaskId =  gIntent.getExtras().getString("taskid");
//        Log.d("qqqqqqq","taskId=="+mTaskId);
        linearLayoutManager = new LinearLayoutManager(LXWaitInfoActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        lxWaitListAdapter = new LXWaitListAdapter(LXWaitInfoActivity.this,R.layout.item_floor_d,lxWaitListEntitieList);
        recyclerView.setAdapter(lxWaitListAdapter);

         mHeadView = getLayoutInflater().inflate(R.layout.layout_head,null);
        lxWaitListAdapter.addHeaderView(mHeadView);

        getWaitModel = ViewModelProviders.of(this).get(GetWaitModel.class);
        getWaitModel.getCurrentData(this).observe(this, new Observer<LXWaitListEntity>() {
            @Override
            public void onChanged(@Nullable LXWaitListEntity lxWaitListEntities) {
                String taskId = "6b768520-eb2f-40ff-a540-601d84cd480a";
                if(lxWaitListEntities != null){
                    lxWaitListEntitieList.clear();
                    taskId = lxWaitListEntities.getTaskUUID();
//                Log.d("qqqqqqq","mRouteId路由资源=="+lxWaitListEntities.get(0).getRouteUUID());
                LXWaitListEntity item = lxWaitListEntities;

                TextView tvNum = mHeadView.findViewById(R.id.item_noreceive_tv_bnum);
                TextView tvperson = mHeadView.findViewById(R.id.item_noreceive_tv_person);
                TextView tvtime = mHeadView.findViewById(R.id.item_noreceive_tv_time);
                TextView tvname = mHeadView.findViewById(R.id.item_noreceive_tv_name);
                TextView tvname2 = mHeadView.findViewById(R.id.item_noreceive_tv_name2);
                TextView tvmajor = mHeadView.findViewById(R.id.item_noreceive_tv_major);

                if (!StringUtils.isEmpty(item.getName())) {
                    tvNum.setText(item.getName());
                }
                if (!StringUtils.isEmpty(item.getDoneLenght())) {
                    tvperson.setText(item.getDoneLenght());
                }
                if (!StringUtils.isEmpty(item.getUndoLenght())) {
                    tvtime.setText(item.getUndoLenght());
                }
                if (!StringUtils.isEmpty(item.getRouteLenght())) {
                    tvname.setText("巡检路由长度:" + item.getRouteLenght());
                }
                if (!StringUtils.isEmpty(item.getDoneRate())) {
                    tvname2.setText("巡检完成率:" + item.getDoneRate());
                }
                if (!StringUtils.isEmpty(item.getTaskStatus())) {
                    if (item.getTaskStatus().equals("0")) {
                        tvmajor.setText( "未执行");
                    } else if (item.getTaskStatus().equals("1")) {
                        tvmajor.setText("执行中");
                    } else if (item.getTaskStatus().equals("2")) {
                        tvmajor.setText("已完成");
                    }
                }
                }
                page = 1;
                getTaskLineModel.getTaskLine(mRequestClient,taskId,page);
            }
        });


        getWaitModel.getNearRes(mRequestClient,mTaskId);
        getTaskLineModel = ViewModelProviders.of(this).get(GetTaskLineModel.class);
        getTaskLineModel.getCurrentData(this).observe(this, new Observer<List<LXWaitListEntity>>() {
            @Override
            public void onChanged(@Nullable List<LXWaitListEntity> lxWaitListEntities) {
                swipeRefreshLayout.setRefreshing(false);
                if(!ToolUtil.isEmpty(lxWaitListEntities)){
                    for(int i = 0;i< lxWaitListEntities.size();i++){
                        if(i == 0) {
                            lxWaitListEntities.get(i).setMyType("3");
                        }else{
                            lxWaitListEntities.get(i).setMyType("0");
                        }
//                        Log.d("qqqqq","status=="+lxWaitListEntities.get(i).getStatus());
                    }
                    mRouteId = lxWaitListEntities.get(0).getRouteUUID();
                    Log.d("qqqqqqq",page+"线资源=="+lxWaitListEntities.size());

                    if(lxWaitListEntities.size() >= 10 ){

                        lxWaitListAdapter.loadMoreComplete();

                    }else{
                        lxWaitListAdapter.loadMoreEnd();
                    }
                    if(page == 1) {
                        lxWaitListEntitieList.clear();
                    }
                    lxWaitListEntitieList.addAll(lxWaitListEntities);
                    lxWaitListAdapter.notifyDataSetChanged();
                }
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getTaskLineModel.getTaskLine(mRequestClient,mTaskId,page);
            }
        });
        lxWaitListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page += 1;
                getTaskLineModel.getTaskLine(mRequestClient,mTaskId,page);
            }
        },recyclerView);


//取消点击段进行巡检
//        lxWaitListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if(position != 0){
//
//                    String uuid =lxWaitListEntitieList.get(position).getUuid();
//                    String routeId = lxWaitListEntitieList.get(position).getRouteUUID();
//                    Log.d("qqqq","点击taskId="+taskUUID+"uuid="+uuid+"routeId="+routeId);
//
//                    Intent gIntent = new Intent(LXWaitInfoActivity.this,LXGoingActivity.class);
//                    gIntent.putExtra("taskUUID",taskUUID);
//                    gIntent.putExtra("uuid",uuid);
//                    gIntent.putExtra("routeId",routeId);
//                    gIntent.putExtra("entity",lxWaitListEntitieList.get(position));
//                    startActivity(gIntent);
//                }
//            }
//        });
    }

    @Override
    public void initData() {
        mToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.bt_start:
                Intent gIntent = new Intent(LXWaitInfoActivity.this,LXGoingActivity.class);
                gIntent.putExtra("taskid",mTaskId);
                startActivity(gIntent);
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int i = menuItem.getItemId();
        if (i == R.id.action_more) {//开始巡检
            LXWaitListEntity lxWaitListEntity = new LXWaitListEntity();
            lxWaitListEntity.setaPointLat(linesBean.getAPointLat());
            lxWaitListEntity.setaPointLat(linesBean.getZPointLat());
            lxWaitListEntity.setaPointLat(linesBean.getAPointLng());
            lxWaitListEntity.setaPointLat(linesBean.getZPointLng());
            lxWaitListEntity.setRouteName(linesBean.getName());


            String mmTaskid = "";
            if (linesBean != null) {
                mmTaskid = linesBean.getTaskUUID();
            }


            Intent gIntent = new Intent(LXWaitInfoActivity.this, LXGoingActivity.class);
            gIntent.putExtra("taskUUID", mmTaskid);
            gIntent.putExtra("uuid", mUUID);
            gIntent.putExtra("routeId", mRouteId);
            gIntent.putExtra("entity", lxWaitListEntity);
            startActivity(gIntent);

        }
        return true;
    }
}
