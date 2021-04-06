package com.example.travel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.travel.R;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.entity.LoginResponse;
import com.example.travel.entity.User;
import com.example.travel.util.LoginUser;
import com.example.travel.util.StringUtils;
import com.google.gson.Gson;

import java.util.HashMap;

public class Login1Activity extends BaseActivity {

    private EditText account;
    private EditText pwd;
    private TextView switchLogin;
    private TextView goRegister;
    private Button loginBtn;

    @Override
    protected int initLayout() {
        return R.layout.activity_login1;
    }

    @Override
    protected void initView() {
        account = findViewById(R.id.login1_id_account);
        pwd = findViewById(R.id.login1_id_pwd);
        switchLogin = findViewById(R.id.login1_id_switchLogin);
        goRegister = findViewById(R.id.login1_id_goRegister);
        loginBtn = findViewById(R.id.login1_id_login);
    }

    @Override
    protected void initData() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(account.getText().toString().trim(),
                        pwd.getText().toString().trim());
            }
        });
    }

    private void login(String account, String pwd) {
        if (StringUtils.isEmpty(account)) {
            showToast("请输入手机号或邮箱");
            return;
        }
        if (StringUtils.isEmpty(pwd)) {
            showToast("请输入密码");
            return;
        }
        if (account.equals("123") && pwd.equals("123")) {
            showToast("登录成功");
            LoginUser loginUser = LoginUser.getInstance();
            User user = new User();
            user.setId(1);
            user.setUsername("海绵宝宝");
            user.setPhoneNum("1234567890");
            user.setEmail("12334556@qq.com");
            user.setSignature("你好，派大星");
            user.setGender("男");
            user.setHeadPortraitPath(new byte[]{});
            user.setBackgroundPath(new byte[]{});
            user.setBirthday("2020-04-06");
            user.setRegion("太平洋比奇堡海滩");
            loginUser.setUser(user);
            updateSp(user);
            navigateTo(UserInfoActivity.class);
        } else {
            showToast("用户名或密码错误");
        }
        /*
        HashMap<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("password",pwd);
        Api.config(ApiConfig.LOGIN,params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res,LoginResponse.class);
                if (loginResponse.getCode() == 200) {
                    saveStringToSp("id",String.valueOf(loginResponse.getId()));
                    saveStringToSp("username",loginResponse.getUsername());
                    saveStringToSp("phoneNum", loginResponse.getPhoneNum());
                    saveStringToSp("email",loginResponse.getEmail());
                    saveStringToSp("signature",loginResponse.getSignature());
                    saveStringToSp("headPortraitPath",loginResponse.getHeadPortraitPath());
                    saveStringToSp("backgroundPath",loginResponse.getBackgroundPath());
                    saveStringToSp("birthday",loginResponse.getBirthday());
                    saveStringToSp("region",loginResponse.getRegion());
                    showToast("登录成功");
                } else {
                    showToast("登录失败");
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

         */
    }

}