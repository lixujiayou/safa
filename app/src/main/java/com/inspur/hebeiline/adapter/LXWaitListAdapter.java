package com.inspur.hebeiline.adapter;

import android.content.ContentProvider;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.inspur.hebeiline.R;
import com.inspur.hebeiline.entity.LXWaitListEntity;
import com.inspur.hebeiline.utils.tools.StringUtils;

import java.util.List;

/**
 * Created by lixu on 2018/3/15.
 * 传输线路 我的列表
 * getDeliveryState
 * 0：未交割
 * 1：已交割待确认
 * 2：已确认待审批
 * 3：审批驳回
 * 4：确认驳回
 * 5：归档
 */

public class LXWaitListAdapter extends BaseItemDraggableAdapter<LXWaitListEntity, BaseViewHolder> {
    private Context mContext;

    public LXWaitListAdapter(Context context, int layoutResId, @Nullable List<LXWaitListEntity> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LXWaitListEntity item) {
        if(StringUtils.isEmpty(item.getMyType()) || item.getMyType().equals("1")) {
            helper.setGone(R.id.ll_his,false);
            if (!StringUtils.isEmpty(item.getName())) {
                helper.setText(R.id.item_noreceive_tv_bnum, item.getName());
            }
            if (!StringUtils.isEmpty(item.getDoneLenght())) {
                helper.setText(R.id.item_noreceive_tv_person, item.getDoneLenght());
            }
            if (!StringUtils.isEmpty(item.getUndoLenght())) {
                helper.setText(R.id.item_noreceive_tv_time, item.getUndoLenght());
            }
            if (!StringUtils.isEmpty(item.getRouteLenght())) {
                helper.setText(R.id.item_noreceive_tv_name, "巡检路由长度:" + item.getRouteLenght());
            }
            if (!StringUtils.isEmpty(item.getDoneRate())) {
                helper.setText(R.id.item_noreceive_tv_name2, "巡检完成率:" + item.getDoneRate());
            }
            if (!StringUtils.isEmpty(item.getTaskStatus())) {
                if (item.getTaskStatus().equals("0")) {
                    helper.setText(R.id.item_noreceive_tv_major, "未执行");
                } else if (item.getTaskStatus().equals("1")) {
                    helper.setText(R.id.item_noreceive_tv_major, "执行中");
                } else if (item.getTaskStatus().equals("2")) {
                    helper.setText(R.id.item_noreceive_tv_major, "已完成");
                }
            }
        }else{

            helper.setGone(R.id.ll_his,true);
            helper.setGone(R.id.ll_main,false);

            if (!StringUtils.isEmpty(item.getName())) {
                helper.setText(R.id.item_his_name, item.getName());
            }
            if (!StringUtils.isEmpty(item.getRouteName())) {
                helper.setText(R.id.item_his_length, item.getRouteName());
            }
            if(item.getMyType().equals("3")){
                helper.setGone(R.id.item_title,true);
            }else{
                helper.setGone(R.id.item_title,false);
            }

            if(!StringUtils.isEmpty(item.getStatus()) && item.getStatus().equals("1")){
                helper.setImageDrawable(R.id.iv_xunjian, ContextCompat.getDrawable(mContext,R.drawable.icon_y_xunjian));
            }else{
                helper.setImageDrawable(R.id.iv_xunjian, ContextCompat.getDrawable(mContext,R.drawable.icon_no_xunjian));
            }


        }


    }
}
