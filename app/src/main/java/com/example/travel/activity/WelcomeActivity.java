package com.example.travel.activity;

import android.util.Log;

import com.example.travel.R;
import com.example.travel.entity.User;
import com.example.travel.util.LoginUser;

public class WelcomeActivity extends BaseActivity {


    @Override
    protected int initLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

        String id = getStringFromSp("id");
        Log.e("welcome_userId",id);
        if (id == null || id.length() == 0) {
            navigateToWithTime(Login1Activity.class, 2000);
        } else {
            navigateToWithTime(HomeActivity.class, 2000);
        }
        String username = getStringFromSp("username");
        String phoneNum = getStringFromSp("phoneNum");
        String email = getStringFromSp("email");
        String gender = getStringFromSp("gender");
        String signature = getStringFromSp("signature");
        String headPortraitPath = getStringFromSp("headPortraitPath");
        String backgroundPath = getStringFromSp("backgroundPath");
        String birthday = getStringFromSp("birthday");
        String region = getStringFromSp("region");
        User loginUser = new User();
        loginUser.setId(id);
        loginUser.setUsername(username);
        loginUser.setPhoneNum(phoneNum);
        loginUser.setEmail(email);
        loginUser.setHeadPortraitPath(headPortraitPath);
        loginUser.setBackgroundPath(backgroundPath);
        loginUser.setGender(gender);
        loginUser.setSignature(signature);
        loginUser.setBirthday(birthday);
        loginUser.setRegion(region);
        LoginUser.getInstance().setUser(loginUser);
    }
}