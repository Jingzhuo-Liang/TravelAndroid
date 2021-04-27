package com.example.travel.entity;

import java.util.List;

/**
 * @@author:ljz
 * @@date:2021/4/27,19:48
 * @@version:1.0
 * @@annotation:
 **/
public class MapPointResponse {

    private int code;
    private String msg;
    private List<MapPointEntity> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<MapPointEntity> getData() {
        return data;
    }

    public void setData(List<MapPointEntity> data) {
        this.data = data;
    }
}
