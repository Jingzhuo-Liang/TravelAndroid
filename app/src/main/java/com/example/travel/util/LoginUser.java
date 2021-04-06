package com.example.travel.util;

import android.app.Application;
import com.example.travel.entity.User;

//使用饿汉模式实现单例的登录用户信息记录
public class LoginUser extends Application {
    private static LoginUser login_user = new LoginUser();
    private static User user;
    private int type; // 1-username 2-phoneNum 3-email  4-signature

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


    public static LoginUser getInstance(){
        return login_user;
    }

    public void update(){
        if(user.getId()==this.id){
            user.setUsername(this.username);
            user.setPhoneNum(this.phoneNum);
            user.setEmail(this.email);
            user.setHeadPortraitPath(this.headPortraitPath);
            user.setBackgroundPath(this.backgroundPath);
            user.setSignature(this.signature);
            user.setGender(this.gender);
            user.setRegion(this.region);
            user.setBirthday(this.birthday);
            //user.update(user.getId());
        }
    }

    public void setType(int type) {
        this.type =  type;
    }

    public int getType() {
        return type;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user) {
        LoginUser.user = user;
        setUsername(user.getUsername());
        setPhoneNum(user.getPhoneNum());
        setEmail(user.getEmail());
        setSignature(user.getSignature());
        setHeadPortraitPath(user.getHeadPortraitPath());
        setBackgroundPath(user.getBackgroundPath());
        setBirthday(user.getBirthday());
        setGender(user.getGender());
        setRegion(user.getRegion());
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public static LoginUser getLogin_user() {
        return login_user;
    }

    public static void setLogin_user(LoginUser login_user) {
        LoginUser.login_user = login_user;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getHeadPortraitPath() {
        return headPortraitPath;
    }

    public void setHeadPortraitPath(byte[] headPortraitPath) {
        this.headPortraitPath = headPortraitPath;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public byte[] getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(byte[] backgroundPath) {
        this.backgroundPath = backgroundPath;
    }
}
