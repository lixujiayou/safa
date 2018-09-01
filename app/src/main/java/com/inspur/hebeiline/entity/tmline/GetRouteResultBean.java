package com.inspur.hebeiline.entity.tmline;

import java.util.List;

/**
 * Created by lixu on 2018/4/10.
 */

public class GetRouteResultBean {

    /**
     * info : 没有查询到资源信息或没有上传轨迹点
     * result : 1
     */

    private List<RouteInfoBean> info;
    private String hint;
    private int result;

    public List<RouteInfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<RouteInfoBean> info) {
        this.info = info;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
