package com.example.travel.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.travel.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public abstract class BaseActivity extends AppCompatActivity {

    public Context context;
    //地址选择器数据
    protected String province_data;
    protected String city_data;
    protected OptionsPickerView pvOptions;

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        context = this;
        setContentView(initLayout());
        province_data = readJsonFile("province.json");
        city_data = readJsonFile("city.json");
        initView();
        initData();
    }

    protected abstract int initLayout() ;
    protected abstract void initView();
    protected abstract void initData();

    public void showToast(String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public void showToastSync(String msg) {
        Looper.prepare();
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void navigateTo(Class c) {
        Intent in = new Intent(context, c);
        startActivity(in);
    }

    public void navigateToWithPara(Class c, HashMap<String, String> map) {
        Intent intentSimple = new Intent();
        intentSimple.setClass(context,c);
        Bundle bundle=new Bundle();
        for (String key : map.keySet()) {
            bundle.putString(key, map.get(key));
        }
        intentSimple.putExtras(bundle);
        startActivity(intentSimple);
    }

    public void saveStringToSp(String key, String value) {
        SharedPreferences sp = getSharedPreferences("sp_travel",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        //Log.e(key,value);
        editor.apply();
    }

    public String getStringFromSp(String key) {
        SharedPreferences sp = getSharedPreferences("sp_travel",MODE_PRIVATE);
        //Log.e(sp.getString(key,""),sp.getString(key,""));
        return sp.getString(key,"");
    }

    public void updateSp(User user) {
        clearSp();
        saveStringToSp("id",String.valueOf(user.getId()));
        saveStringToSp("username",user.getUsername());
        saveStringToSp("phoneNum", user.getPhoneNum());
        saveStringToSp("email",user.getEmail());
        saveStringToSp("gender",user.getGender());
        saveStringToSp("signature",user.getSignature());
        saveStringToSp("headPortraitPath","");
        saveStringToSp("backgroundPath","");
        saveStringToSp("birthday",user.getBirthday());
        saveStringToSp("region",user.getRegion());
    }

    public void clearSp() {
        SharedPreferences sp = getSharedPreferences("sp_travel",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        //getStringFromSp("id");
    }

    //传入：asset文件夹中json文件名
    //返回：读取的String
    protected String readJsonFile(String file){
        StringBuilder newstringBuilder = new StringBuilder();
        try {
            InputStream inputStream = getResources().getAssets().open(file);

            InputStreamReader isr = new InputStreamReader(inputStream);

            BufferedReader reader = new BufferedReader(isr);

            String jsonLine;
            while ((jsonLine = reader.readLine()) != null) {
                newstringBuilder.append(jsonLine);
            }
            reader.close();
            isr.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String data =  newstringBuilder.toString();
        return data;
    }
}
