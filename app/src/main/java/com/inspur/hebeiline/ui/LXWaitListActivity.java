package com.inspur.hebeiline.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.inspur.hebeiline.R;
import com.inspur.hebeiline.adapter.LXWaitListAdapter;
import com.inspur.hebeiline.entity.LXWaitListEntity;
import com.inspur.hebeiline.model.GetWaitListModel;
import com.inspur.hebeiline.utils.base.BaseActivity;
import com.inspur.hebeiline.utils.tools.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixu on 2018/6/21.
 */

public class LXWaitListActivity extends BaseActivity{
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<LXWaitListEntity> lxWaitListEntitieList = new ArrayList<>();

    private LXWaitListAdapter lxWaitListAdapter;

    private GetWaitListModel getWaitListModel;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wait_list_lx);
    }

    @Override
    public void initViews() {
        mToolbar.setTitle("待办任务");
        mToolbar.setNavigationIcon(R.drawable.icon_cancle);

        recyclerView = findViewById(R.id.recyler);
        linearLayoutManager = new LinearLayoutManager(LXWaitListActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        lxWaitListAdapter = new LXWaitListAdapter(LXWaitListActivity.this,R.layout.item_floor_d,lxWaitListEntitieList);
        recyclerView.setAdapter(lxWaitListAdapter);

        getWaitListModel = ViewModelProviders.of(this).get(GetWaitListModel.class);
        getWaitListModel.getCurrentData(this).observe(this, new Observer<List<LXWaitListEntity>>() {
            @Override
            public void onChanged(@Nullable List<LXWaitListEntity> lxWaitListEntities) {
                if(!ToolUtil.isEmpty(lxWaitListEntities)){
                    lxWaitListEntitieList.clear();
                    lxWaitListEntitieList.addAll(lxWaitListEntities);
                    lxWaitListAdapter.notifyDataSetChanged();
                }
            }
        });

        getWaitListModel.getNearRes(mRequestClient);

        lxWaitListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //开始巡检
                Intent gIntent = new Intent(LXWaitListActivity.this,LXWaitInfoActivity.class);
                gIntent.putExtra("mmType","list");
                gIntent.putExtra("taskid",lxWaitListEntitieList.get(position).getTaskUUID());
                startActivityForResult(gIntent,1);



            }
        });
    }

    @Override
    public void initData() {
    }
}
