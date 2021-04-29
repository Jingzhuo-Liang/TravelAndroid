package com.example.travel.entity;

/**
 * @@author:ljz
 * @@date:2021/4/29,16:09
 * @@version:1.0
 * @@annotation:
 **/
public class OtherUserInfoResponse {

    private int code;
    private String msg;
    private OtherUserInfoEntity data;

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

    public OtherUserInfoEntity getData() {
        return data;
    }

    public void setData(OtherUserInfoEntity data) {
        this.data = data;
    }
}
