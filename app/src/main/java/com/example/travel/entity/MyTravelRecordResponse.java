package com.example.travel.entity;

import java.util.ArrayList;

/**
 * @@author:ljz
 * @@date:2021/4/20,20:13
 * @@version:1.0
 * @@annotation:
 **/
public class MyTravelRecordResponse {

    private int code;
    private String msg;
    private ArrayList<MyTravelRecordEntity> data;

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

    public ArrayList<MyTravelRecordEntity> getData() {
        return data;
    }

    public void setData(ArrayList<MyTravelRecordEntity> data) {
        this.data = data;
    }
}
