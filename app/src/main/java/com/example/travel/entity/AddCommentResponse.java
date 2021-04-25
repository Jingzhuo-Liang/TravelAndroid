package com.example.travel.entity;

/**
 * @@author:ljz
 * @@date:2021/4/25,10:10
 * @@version:1.0
 * @@annotation:
 **/
public class AddCommentResponse {

    private int code;
    private String msg;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
