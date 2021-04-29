package com.example.travel.entity;

/**
 * @@author:ljz
 * @@date:2021/4/29,15:57
 * @@version:1.0
 * @@annotation:
 **/
public class UserRelatedInfoResponse {

    private int code;
    private String msg;
    private UserRelatedInfoEntity data;

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

    public UserRelatedInfoEntity getData() {
        return data;
    }

    public void setData(UserRelatedInfoEntity data) {
        this.data = data;
    }
}
