package com.example.travel.entity;

/**
 * @@author:ljz
 * @@date:2021/5/11,22:21
 * @@version:1.0
 * @@annotation:
 **/
public class ModifyTravelRecordResponse {

    private int code;
    private String msg;
    private ModifyTravelRecordEntity data;

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

    public ModifyTravelRecordEntity getData() {
        return data;
    }

    public void setData(ModifyTravelRecordEntity data) {
        this.data = data;
    }
}
