package com.inspur.hebeiline.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.inspur.hebeiline.R;
import com.inspur.hebeiline.adapter.MySpAdapter;
import com.inspur.hebeiline.entity.LXMineEntity;
import com.inspur.hebeiline.model.GetMineModel;
import com.inspur.hebeiline.utils.base.BaseActivity;
import com.inspur.hebeiline.utils.base.DoubleDatePickerDialog;
import com.inspur.hebeiline.utils.base.MyDatePickerDialog;
import com.inspur.hebeiline.utils.tools.StringUtils;
import com.inspur.hebeiline.utils.tools.TimeUtils;
import com.inspur.hebeiline.utils.tools.ToastUtil;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lixu on 2018/6/22.
 */

public class LXMineActivity extends BaseActivity {
    private GetMineModel getMineModel;
    private TextView tvTime;
    private TextView tv_length1,tv_length2,tv_length_y,tv_length_no;
    private Spinner spLevel;
    private MySpAdapter mySpAdapter;
    private List<String> spList = new ArrayList<>();
    private int cLevel = 0;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mine_lx);
    }

    @Override
    public void initViews() {
        mToolbar.setTitle("我的巡检详情");
        mToolbar.setNavigationIcon(R.drawable.icon_cancle);

        tvTime = findViewById(R.id.tv_time);

        spLevel = findViewById(R.id.sp_level);

        tv_length1 = findViewById(R.id.tv_length1);
        tv_length2 = findViewById(R.id.tv_length2);
        tv_length_y = findViewById(R.id.tv_length_y);
        tv_length_no = findViewById(R.id.tv_length_no);

        tvTime.setOnClickListener(this);

        initSpData();

        getMineModel = ViewModelProviders.of(this).get(GetMineModel.class);
        getMineModel.getCurrentData(this).observe(this, new Observer<LXMineEntity>() {
            @Override
            public void onChanged(@Nullable LXMineEntity lxMineEntity) {
                if(lxMineEntity != null){
                    if(!StringUtils.isEmpty(lxMineEntity.getPersonalMonthLength())){
                        tv_length1.setText(lxMineEntity.getPersonalMonthLength());
                    }
                    if(!StringUtils.isEmpty(lxMineEntity.getPersonalTotalLength())){
                        tv_length2.setText(lxMineEntity.getPersonalTotalLength());
                    }
                    if(!StringUtils.isEmpty(lxMineEntity.getTotalDoneLenght())){
                        tv_length_y.setText(lxMineEntity.getTotalDoneLenght());
                    }
                    if(!StringUtils.isEmpty(lxMineEntity.getTotalUndoLenght())){
                        tv_length_no.setText(lxMineEntity.getTotalUndoLenght());
                    }
                }
            }
        });

        tvTime.setText(TimeUtils.getNowTimeToMounth());


        mySpAdapter = new MySpAdapter(LXMineActivity.this,spList);
        spLevel.setAdapter(mySpAdapter);
        spLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cLevel = i;
                getMineModel.getMine(mRequestClient, tvTime.getText().toString(),i+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void initData() {

    }

    private void initSpData(){
        spList.add("全部");
        spList.add("一干");
        spList.add("二干");
        spList.add("城域网主干");
        spList.add("城域网汇聚");
        spList.add("城域网接入");
        spList.add("集客专线接入");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i = view.getId();
        if (i == R.id.tv_time) {
            showTimeDialog();

        }
    }

    private void showTimeDialog() {
        MyDatePickerDialog datePickerDialog = new MyDatePickerDialog(LXMineActivity.this);
        datePickerDialog.setOnSureListener(new MyDatePickerDialog.OnSureListener() {
            @Override
            public void back(String name) {
                Log.d("qqqq","name=="+name);
                tvTime.setText(name);
                getMineModel.getMine(mRequestClient, name,cLevel+"");
            }
        });
        datePickerDialog.show();

    }
}
