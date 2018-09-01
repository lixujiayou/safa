package com.inspur.hebeiline.entity.tmline;

/**
 * Created by lixu on 2018/4/10.
 */

public class SaveResultBean {

    /**
     * info : 没有查询到资源信息或没有上传轨迹点
     * result : 1
     */

    //private RouteInfoBean info;
    private String info;
    private String hint;
    private int result;

    @Override
    public String toString() {
        return "SaveResultBean{" +
                "info='" + info + '\'' +
                ", hint='" + hint + '\'' +
                ", result=" + result +
                '}';
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
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
