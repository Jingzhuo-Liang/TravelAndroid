package com.example.travel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Typeface;
import android.opengl.ETC1;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.travel.R;
import com.example.travel.api.ApiConfig;
import com.example.travel.util.LoginUser;
import com.example.travel.util.ActivityCollector;
import com.example.travel.util.StringUtils;
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
    private static final int EDIT_PASSWORD = 7;

    private LoginUser loginUser = LoginUser.getInstance();
    private TitleLayout tl_title;
    private EditText edit_name;
    private EditText edit_newPwd;
    private EditText edit_confirmNewPwd;
    private View edit_view1;
    private View edit_view2;

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
        tl_title.getTextView_forward().setText("确认");
        edit_name = (EditText) findViewById(R.id.et_edit_name);
        edit_newPwd = findViewById(R.id.et_edit_name1);
        edit_confirmNewPwd = findViewById(R.id.et_edit_name2);
        edit_view1 = findViewById(R.id.et_view1);
        edit_view2 = findViewById(R.id.et_view2);
        switch (loginUser.getType()) {
            case EDIT_NAME: {
                edit_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(ApiConfig.USER_NAME_MAX_LENGTH)});
                Bundle bundle = this.getIntent().getExtras();
                edit_name.setText(bundle.get("tempString").toString());
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
                edit_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(ApiConfig.USER_SIGNATURE_MAX_LENGTH)});
                Bundle bundle = this.getIntent().getExtras();
                edit_name.setText(bundle.get("tempString").toString());
                break;
            }
            case EDIT_PASSWORD: {
                edit_name.setHint("请输入原密码");
                edit_name.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edit_name.setTransformationMethod(new PasswordTransformationMethod());
                edit_newPwd.setVisibility(View.VISIBLE);
                edit_confirmNewPwd.setVisibility(View.VISIBLE);
                edit_view1.setVisibility(View.VISIBLE);
                edit_view2.setVisibility(View.VISIBLE);
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
                    case EDIT_PASSWORD: {
                        String oldPwd = edit_name.getText().toString().trim();
                        String newPwd = edit_newPwd.getText().toString().trim();
                        String confirmNewPwd = edit_confirmNewPwd.getText().toString().trim();
                        if (StringUtils.isEmpty(oldPwd)) {
                            showToast("原密码不可为空");
                            return;
                        } else if (StringUtils.isEmpty(newPwd)) {
                            showToast("新密码不可为空");
                            return;
                        } else if (StringUtils.isEmpty(confirmNewPwd)) {
                            showToast("确认密码不可为空");
                            return;
                        } else if (!newPwd.equals(confirmNewPwd)) {
                            showToast("新密码与确认密码不一致");
                            return;
                        }
                        loginUser.setOldPwd(oldPwd);
                        loginUser.setNewPwd(newPwd);
                        setResult(RESULT_OK);
                        finish();
                        break;
                    }
                    default:{
                        if (loginUser.getType() == EDIT_NAME &&  edit_name.getText().toString().length() == 0) {
                            showToast("用户名不可为空");
                            return;
                        }
                        loginUser.setTempString(edit_name.getText().toString());
                        setResult(RESULT_OK);
                        finish();
                        break;
                    }
                }
            }
        });
        tl_title.getTextView_backward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
