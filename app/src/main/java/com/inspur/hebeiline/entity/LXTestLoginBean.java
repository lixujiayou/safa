package com.inspur.hebeiline.entity;

/**
 * Created by lixu on 2018/6/20.
 */

public class LXTestLoginBean {
    private String id_token;

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    @Override
    public String toString() {
        return "LXTestLoginBean{" +
                "id_token='" + id_token + '\'' +
                '}';
    }
}
