package com.example.travel.entity;

/**
 * @@author:ljz
 * @@date:2021/4/15,19:54
 * @@version:1.0
 * @@annotation:
 **/
public class RegisterResponse {

    private String msg;
    private int code;
    private RegisterData data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public RegisterData getData() {
        return data;
    }

    public void setData(RegisterData data) {
        this.data = data;
    }
}
