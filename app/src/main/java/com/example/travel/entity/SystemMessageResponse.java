package com.example.travel.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @@author:ljz
 * @@date:2021/5/14,17:14
 * @@version:1.0
 * @@annotation:
 **/
public class SystemMessageResponse {

    private int code;
    private String msg;
    private ArrayList<SystemMessageEntity> data;

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

    public ArrayList<SystemMessageEntity> getData() {
        return data;
    }

    public void setData(ArrayList<SystemMessageEntity> data) {
        this.data = data;
    }
}
