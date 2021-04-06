package com.example.travel.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    public Context context;

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        context = this;
        setContentView(initLayout());
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

    public void saveStringToSp(String key, String value) {
        SharedPreferences sp = getSharedPreferences("sp_travel",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.apply();
    }
}
