package com.example.travel.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Timer;
import java.util.TimerTask;

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

    public void navigateToWithTime(Class c, int time) {
        //Intent in = new Intent(context, c);
        //startActivity(in);
        Intent in =new Intent(context,c);
        Timer timer=new Timer();
        TimerTask task=new TimerTask(){
            @Override
            public void run(){
                startActivity(in);
            }
        };
        timer.schedule(task,time);
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

    public void navigateToBrowserWithUrl(String s) {
        Uri uri = Uri.parse(s);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
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
        saveStringToSp("headPortraitPath",user.getHeadPortraitPath());
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

    protected void dismissSoftKeyBoard() {
        //有问题，软键盘出现时可让它消失；但是当软键盘不存在时却会出现
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    public void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
    public void  toggleSoftInput(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0,0);
        }
    }
}
