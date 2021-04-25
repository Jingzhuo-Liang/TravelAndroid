package com.example.travel.entity;

/**
 * @@author:ljz
 * @@date:2021/4/25,9:21
 * @@version:1.0
 * @@annotation:
 **/
public class RecordDetailResponse {

    private int code;
    private String msg;
    private RecordDetailEntity data;

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

    public RecordDetailEntity getData() {
        return data;
    }

    public void setData(RecordDetailEntity data) {
        this.data = data;
    }
}
