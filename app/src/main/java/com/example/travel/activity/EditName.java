package com.example.travel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.opengl.ETC1;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.travel.R;
import com.example.travel.util.LoginUser;
import com.example.travel.util.ActivityCollector;
import com.example.travel.widget.TitleLayout;

import javax.security.auth.login.LoginException;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class EditName extends BaseActivity {
    private static final int EDIT_NAME = 1;
    private static final int EDIT_PHONENUM = 2;
    private static final int EDIT_EMAIL = 3;
    private static final int EDIT_SIGNATURE = 4;

    private LoginUser loginUser = LoginUser.getInstance();
    private TitleLayout tl_title;
    private EditText edit_name;

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_edit_name);

        tl_title = (TitleLayout) findViewById(R.id.tl_title);
        edit_name = (EditText) findViewById(R.id.et_edit_name);
        edit_name.setText(loginUser.getName());

        //设置监听器
        //如果点击完成，则更新loginUser并销毁
        tl_title.getTextView_forward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser.setName(edit_name.getText().toString());
                setResult(RESULT_OK);
                finish();
            }
        });
    }

     */

    @Override
    protected int initLayout() {
        return R.layout.activity_edit_name;
    }

    @Override
    protected void initView() {
        ActivityCollector.addActivity(this);
        tl_title = (TitleLayout) findViewById(R.id.tl_title);
        edit_name = (EditText) findViewById(R.id.et_edit_name);
        switch (loginUser.getType()) {
            case EDIT_NAME: {
                edit_name.setText(loginUser.getUsername());
                break;
            }
            case EDIT_PHONENUM: {
                edit_name.setText(loginUser.getPhoneNum());;
                break;
            }
            case EDIT_EMAIL:{
                edit_name.setText(loginUser.getEmail());
                break;
            }
            case EDIT_SIGNATURE:{
                edit_name.setText(loginUser.getSignature());
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        //设置监听器
        //如果点击完成，则更新loginUser并销毁
        tl_title.getTextView_forward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("type:",String.valueOf(loginUser.getType()));
                switch (loginUser.getType()) {

                    case 1: {
                        loginUser.setUsername(edit_name.getText().toString());
                        break;
                    }
                    case 2: {
                        loginUser.setPhoneNum(edit_name.getText().toString());
                        Log.e("phoneNum:",edit_name.getText().toString());
                        break;
                    }
                    case 3:{
                        loginUser.setEmail(edit_name.getText().toString());
                        break;
                    }
                    case 4:{
                        loginUser.setSignature(edit_name.getText().toString());
                        break;
                    }
                    default:
                        break;
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
