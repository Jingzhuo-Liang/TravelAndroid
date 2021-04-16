package com.example.travel.entity;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class LoginResponse {
    private String msg;
    private int code;
    private User data;

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

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getId() {
        return data.getId();
    }

    public String getPhoneNum() {
        return data.getPhoneNum();
    }

    public String getEmail() {
        return data.getEmail();
    }

    public String getUsername() {
        return data.getUsername();
    }

    public String getHeadPortraitPath() {
        return data.getHeadPortraitPath();
    }

    public String getSignature() {
        return data.getSignature();
    }


    public String getBirthday() {
        return data.getBirthday();
    }

    public String getRegion() {
        return data.getRegion();
    }


}
