package com.inspur.hebeiline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.inspur.hebeiline.R;
import com.inspur.hebeiline.utils.tools.StringUtils;

import java.util.List;

/**
 */

public class MySpAdapter extends BaseAdapter {
    private MainViewHolder mainViewHolder = null;
    private List<String> list_str;
    private Context mContext;

    public MySpAdapter(Context context, List<String> list_str){
        this.list_str = list_str;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return list_str.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            mainViewHolder = new MainViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_spinner,null);
            mainViewHolder.tv_where = (TextView) view.findViewById(R.id.tv_major);
            view.setTag(mainViewHolder);
        }else{
            mainViewHolder = (MainViewHolder) view.getTag();
        }
        if(!StringUtils.isEmpty(list_str.get(i))) {
            mainViewHolder.tv_where.setText(list_str.get(i));
        }
        return view;
    }


    class MainViewHolder{
        TextView tv_where;
    }


    public void updateDate(List<String> mStr){
        this.list_str = mStr;
        notifyDataSetChanged();
    }
}
