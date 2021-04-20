package com.example.travel.entity;

import java.util.ArrayList;

/**
 * @@author:ljz
 * @@date:2021/4/19,18:42
 * @@version:1.0
 * @@annotation:
 **/
public class TravelRecordResponse {

    private int code;
    private String msg;
    private ArrayList<TravelRecordEntity> data;

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

    public ArrayList<TravelRecordEntity> getData() {
        return data;
    }

    public void setData(ArrayList<TravelRecordEntity> data) {
        this.data = data;
    }
}
