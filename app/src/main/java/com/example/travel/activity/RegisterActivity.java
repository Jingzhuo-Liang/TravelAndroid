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
        gender = gender.equals("???")?"male":gender.equals("???")?"female":"unknown";
        birthday = birthday.equals("") ? "": birthday;

        if (StringUtils.isEmpty(account)) {
            showToast("???????????????");
            return;
        } else if (!StringUtils.judgeAccount(account)) {
            showToast("??????????????????????????????????????????");
            return;
        } else if (StringUtils.isEmpty(pwd)) {
            showToast("???????????????");
            return;
        } else if (StringUtils.isEmpty(confirmPwd)) {
            showToast("???????????????");
            return;
        } else if (StringUtils.isEmpty(username)) {
            showToast("??????????????????");
            return;
        }
        if (!pwd.equals(confirmPwd)) {
            showToast("?????????????????????????????????");
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
                    showToastSync("????????????????????????");
                } else if (rg.getCode() == 401) {
                    showToastSync("???????????????");
                } else if (rg.getCode() == 402) {
                    showToastSync("????????????");
                } else {
                    showToastSync("????????????");
                }
            }

            @Override
            public void onFailure(Exception e) {
                showToastSync("???????????????????????????");
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
                        //???????????????????????????LoginUser?????????????????????????????????
                        String tx = options1Items.get(options1).getPickerViewText() + "-"
                                + (options2Items.get(options1).size() == 0?
                                options1Items.get(options1).getPickerViewText():
                                options2Items.get(options1).get(options2));
                        registerRegion.setText(tx);
                        //ig_region.getContentEdt().setText(tx);
                        //loginUser.setRegion(tx);
                    }
                }).setCancelColor(Color.GRAY).build();
                pvOptions.setPicker(options1Items, options2Items);//???????????????
                pvOptions.show();
                break;
            }
            case R.id.register_id_birthday : {
                //???????????????
                //???????????????????????????????????????????????????????????????????????????????????????????????????
                Calendar selectedDate = Calendar.getInstance();
                //?????????picker???show
                TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        //???????????????????????????LoginUser?????????????????????????????????
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
                //???????????????
                pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //???????????????????????????LoginUser?????????????????????????????????
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

    //??????????????????????????????????????????
    private void initOptionData(){
        //?????????????????????
        optionsItems_gender.add(new String("??????"));
        optionsItems_gender.add(new String("???"));
        optionsItems_gender.add(new String("???"));

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