package com.example.travel.entity;

public class User {
    private int id;
    private String phoneNum;
    //private String password;
    private String email;
    private String username;
    private String gender;
    private byte[] headPortraitPath;
    private String signature;
    private byte[] backgroundPath;
    private String birthday;
    private String region;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public byte[] getHeadPortraitPath() {
        return headPortraitPath;
    }

    public void setHeadPortraitPath(byte[] headPortraitPath) {
        this.headPortraitPath = headPortraitPath;
    }

    public byte[] getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(byte[] backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
