package com.example.travel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.travel.R;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.entity.RegisterResponse;
import com.example.travel.util.ActivityCollector;
import com.example.travel.util.CityBean;
import com.example.travel.util.PhotoUtils;
import com.example.travel.util.ProvinceBean;
import com.example.travel.util.StringUtils;
import com.example.travel.widget.RoundImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private ArrayList<String> optionsItems_gender = new ArrayList<>();
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private PopupWindow popupWindow;

    private EditText registerAccount;
    private EditText registerPwd;
    private EditText registerConfirmPwd;
    private EditText registerUsername;
    private EditText registerSignature;
    private TextView registerGender;
    private TextView registerBirthday;
    private TextView registerRegion;
    private Button registerBtn;
    private TextView goLogin;


    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        ActivityCollector.addActivity(this);
        registerAccount = findViewById(R.id.register_id_account);
        registerPwd = findViewById(R.id.register_id_pwd);
        registerConfirmPwd = findViewById(R.id.register_id_confirmPwd);
        registerUsername = findViewById(R.id.register_id_username);
        registerSignature = findViewById(R.id.register_id_signature);
        registerGender = findViewById(R.id.register_id_gender);
        registerRegion = findViewById(R.id.register_id_region);
        registerBtn = findViewById(R.id.register_id_register);
        registerBirthday = findViewById(R.id.register_id_birthday);
        goLogin = findViewById(R.id.register_id_goLogin);
        registerBtn = findViewById(R.id.register_id_register);

        goLogin.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        registerRegion.setOnClickListener(this);
        registerBirthday.setOnClickListener(this);
        registerGender.setOnClickListener(this);

        registerUsername.setFilters(new InputFilter[]{new InputFilter.LengthFilter(ApiConfig.USER_NAME_MAX_LENGTH)});
        registerSignature.setFilters(new InputFilter[]{new InputFilter.LengthFilter(ApiConfig.USER_SIGNATURE_MAX_LENGTH)});
    }

    @Override
    protected void initData() {
        initOptionData();
    }


    private void register() {
        String account = registerAccount.getText().toString().trim();
        String pwd = registerPwd.getText().toString().trim();
        String confirmPwd = registerConfirmPwd.getText().toString().trim();
        String username = registerUsername.getText().toString();
        String signature = registerSignature.getText().toString();
        String region = registerRegion.getText().toString();
        String gender = registerGender.getText().toString();
        String birthday = registerBirthday.getText().toString();
        gender = gender.equals("男")?"male":gender.equals("女")?"female":"unknown";
        birthday = birthday.equals("") ? "": birthday;

        if (StringUtils.isEmpty(account)) {
            showToast("请输入账号");
            return;
        } else if (!StringUtils.judgeAccount(account)) {
            showToast("请输入正确格式的手机号或邮箱");
            return;
        } else if (StringUtils.isEmpty(pwd)) {
            showToast("请输入密码");
            return;
        } else if (StringUtils.isEmpty(confirmPwd)) {
            showToast("请确认密码");
            return;
        } else if (StringUtils.isEmpty(username)) {
            showToast("请输入用户名");
            return;
        }
        if (!pwd.equals(confirmPwd)) {
            showToast("密码不一致，请重新输入");
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("account",account);
        params.put("username",username);
        params.put("password",pwd);
        params.put("confirmPwd",confirmPwd);
        params.put("signature",signature);
        params.put("region",region);
        params.put("gender",gender);
        params.put("birthday",birthday);
        Api.config(ApiConfig.REGISTER,params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                Gson gson = new Gson();
                //Log.e("register",res);
                RegisterResponse rg = gson.fromJson(res,RegisterResponse.class);
                if (rg.getCode() == 200) {
                    finish();
                    //Log.e("register success",res);
                    showToastSync("注册成功，请登录");
                } else if (rg.getCode() == 401) {
                    showToastSync("手机号重复");
                } else if (rg.getCode() == 402) {
                    showToastSync("邮箱重复");
                } else {
                    showToastSync("注册失败");
                }
            }

            @Override
            public void onFailure(Exception e) {
                showToastSync("网络不佳，注册失败");
                //Log.e("register failure",e.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_id_goLogin: {
                navigateTo(Login1Activity.class);
                break;
            }
            case R.id.register_id_register: {
                register();
                break;
            }
            case R.id.register_id_region: {
                pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //选择了则显示并暂存LoginUser，退出时在保存至数据库
                        String tx = options1Items.get(options1).getPickerViewText() + "-"
                                + (options2Items.get(options1).size() == 0?
                                options1Items.get(options1).getPickerViewText():
                                options2Items.get(options1).get(options2));
                        registerRegion.setText(tx);
                        //ig_region.getContentEdt().setText(tx);
                        //loginUser.setRegion(tx);
                    }
                }).setCancelColor(Color.GRAY).build();
                pvOptions.setPicker(options1Items, options2Items);//二级选择器
                pvOptions.show();
                break;
            }
            case R.id.register_id_birthday : {
                //时间选择器
                //修改打开的默认时间，如果选择过则是选择过的时间，否则是系统默认时间
                Calendar selectedDate = Calendar.getInstance();
                //初始化picker并show
                TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        //选择了则显示并暂存LoginUser，退出时在保存至数据库
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        registerBirthday.setText(sdf.format(date));
                        //ig_birthday.getContentEdt().setText(sdf.format(date));
                        //loginUser.setBirthday(sdf.format(date));
                    }
                }).setDate(selectedDate).setCancelColor(Color.GRAY).build();
                pvTime.show();
                break;
            }
            case R.id.register_id_gender : {
                //性别选择器
                pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //选择了则显示并暂存LoginUser，退出时在保存至数据库
                        String tx = optionsItems_gender.get(options1);
                        registerGender.setText(tx);
                        //ig_gender.getContentEdt().setText(tx);
                        //loginUser.setGender(tx);
                    }
                }).setCancelColor(Color.GRAY).build();
                pvOptions.setPicker(optionsItems_gender);
                pvOptions.show();
                break;
            }
            default:{

            }
        }
    }

    //初始化性别、地址和生日的数据
    private void initOptionData(){
        //性别选择器数据
        optionsItems_gender.add(new String("保密"));
        optionsItems_gender.add(new String("男"));
        optionsItems_gender.add(new String("女"));

        Gson gson = new Gson();

        options1Items = gson.fromJson(province_data, new TypeToken<ArrayList<ProvinceBean>>(){}.getType());
        ArrayList<CityBean> cityBean_data = gson.fromJson(city_data, new TypeToken<ArrayList<CityBean>>(){}.getType());
        for(ProvinceBean provinceBean:options1Items){
            ArrayList<String> temp = new ArrayList<>();
            for (CityBean cityBean : cityBean_data){
                if(provinceBean.getProvince().equals(cityBean.getProvince())){
                    temp.add(cityBean.getName());
                }
            }
            options2Items.add(temp);
        }

    }
}