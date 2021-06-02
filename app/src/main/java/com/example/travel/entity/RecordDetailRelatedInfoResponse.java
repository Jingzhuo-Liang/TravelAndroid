package com.example.travel.entity;

/**
 * @@author:ljz
 * @@date:2021/6/2,17:13
 * @@version:1.0
 * @@annotation:
 **/
public class RecordDetailRelatedInfoResponse {

    private int code;
    private String msg;
    private RecordDetailRelatedInfoEntity data;

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

    public RecordDetailRelatedInfoEntity getData() {
        return data;
    }

    public void setData(RecordDetailRelatedInfoEntity data) {
        this.data = data;
    }
}
