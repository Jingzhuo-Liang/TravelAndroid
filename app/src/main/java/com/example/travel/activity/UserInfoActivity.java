package com.example.travel.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.MessagePattern;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.donkingliang.imageselector.utils.ImageUtil;
import com.donkingliang.imageselector.utils.UriUtils;
import com.example.travel.R;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.entity.CommonResponse;
import com.example.travel.util.ActivityCollector;
import com.example.travel.util.CityBean;
import com.example.travel.util.LoginUser;
import com.example.travel.util.PhotoUtils;
import com.example.travel.util.ProvinceBean;
import com.example.travel.util.StringUtils;
import com.example.travel.util.ToastUtils;
import com.example.travel.widget.ItemGroup;
import com.example.travel.widget.RoundImageView;
import com.example.travel.widget.TitleLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    private ItemGroup ig_id;
    private ItemGroup ig_name;
    private ItemGroup ig_gender;
    private ItemGroup ig_region;
    private ItemGroup ig_birthday;
    private ItemGroup ig_phoneNum;
    private ItemGroup ig_email;
    private ItemGroup ig_signature;
    private ItemGroup ig_password;
    private Button ig_exitLoginBtn;

    private LoginUser loginUser;
    private LinearLayout ll_portrait;

    private ArrayList<String> optionsItems_gender = new ArrayList<>();
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    private OptionsPickerView pvOptions;

    private RoundImageView ri_portrati;
    private PopupWindow popupWindow;
    private String imagePath;  //从相册中选的地址
    private PhotoUtils photoUtils = new PhotoUtils();

    private static final int TAKE_PHOTO = 1;
    private static final int FROM_ALBUMS = 2;
    private static final int EDIT_NAME = 3;
    private static final int EDIT_PHONENUM = 4;
    private static final int EDIT_EMAIL = 5;
    private static final int EDIT_SIGNATURE = 6;
    private static final int EDIT_PASSWORD = 7;
    private TitleLayout titleLayout;

    private boolean isDirty = false; //false: not modified true:modified
    private boolean pwdIsDirty = false; // false：not modify pwd  true: modify

    @Override
    protected int initLayout() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView() {
        ActivityCollector.addActivity(this);
        //ig_id = (ItemGroup)findViewById(R.id.ig_id);
        ig_name = (ItemGroup)findViewById(R.id.ig_name);
        ig_gender = (ItemGroup)findViewById(R.id.ig_gender);
        ig_region = (ItemGroup)findViewById(R.id.ig_region);
        ig_birthday = (ItemGroup)findViewById(R.id.ig_birthday);
        ig_phoneNum = findViewById(R.id.ig_phoneNum);
        ig_phoneNum.setJtRightIvIsVisible(false);
        ig_email = findViewById(R.id.ig_email);
        ig_email.setJtRightIvIsVisible(false);
        ig_signature = findViewById(R.id.ig_signature);
        ig_password = findViewById(R.id.ig_password);
        ig_exitLoginBtn = findViewById(R.id.ig_exitLogin);

        ll_portrait = (LinearLayout)findViewById(R.id.ll_portrait);
        ri_portrati = (RoundImageView)findViewById(R.id.ri_portrait);
        titleLayout = (TitleLayout)findViewById(R.id.tl_title);

        ig_name.setOnClickListener(this);
        ig_gender.setOnClickListener(this);
        ig_region.setOnClickListener(this);
        ig_birthday.setOnClickListener(this);
        ig_email.setOnClickListener(this);
        ig_phoneNum.setOnClickListener(this);
        ig_signature.setOnClickListener(this);
        ll_portrait.setOnClickListener(this);
        ig_exitLoginBtn.setOnClickListener(this);
        ig_password.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        loginUser = LoginUser.getInstance();
        initOptionData();
        titleLayout.getTextView_forward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLogin();
                setResult(RESULT_OK);
                //finish();
            }
        });
        titleLayout.getTextView_backward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initInfo();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    //退出登录
    private void exitLogin() {
        loginUser.clear(); //清除本地变量
        clearSp();
        navigateTo(Login1Activity.class);
        /*
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", loginUser.getUser().getId());
        Api.config(ApiConfig.EXIT_LOGIN,params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                showToast("登出成功");
            }

            @Override
            public void onFailure(Exception e) {
                showToast("登出失败");
            }
        });

         */
    }

    //更新用户信息
    private void updateLogin() {
        if (imagePath != null) { //修改头像
            //Log.e("UserInfoPortraitPath",imagePath);
            HashMap<String, Object> params = new HashMap<>();
            params.put("userId",LoginUser.getInstance().getUser().getId());
            params.put("portrait", StringUtils.bitmapToBase64(ImageUtil.getBitmapFromUri(this,UriUtils.getImageContentUri(this, imagePath))));
            //Log.e("portraitPath",String.valueOf(StringUtils.bitmapToBase64(PhotoUtils.getBitmap(imagePath)).length()));
            Api.config(ApiConfig.UPDATE_USER_PORTRAIT,params).postRequest(new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    //Log.e("UserInfoPortrait",res);
                    Gson gson = new Gson();
                    CommonResponse cr = gson.fromJson(res, CommonResponse.class);
                    if (cr.getCode() == 200) {
                        // 获取url
                        loginUser.setHeadPortraitPath(cr.getData());
                        loginUser.update();
                        updateSp(loginUser.getUser());
                        //Log.e("spHeadPortraitPath",getStringFromSp(""));
                        showToastSync("更换头像成功");
                    } else {
                        showToastSync("更换头像失败");
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
            imagePath = null;
        }
        //Log.e("updateUserInfo",String.valueOf(isDirty));
        if (isDirty) {
            //Log.e("updateUserInfoIsDirty", String.valueOf(isDirty));
            HashMap<String, Object> params = new HashMap<>();
            params.put("userId", loginUser.getUser().getId());
            params.put("phoneNum", ig_phoneNum.getContentEdt().getText().toString());
            params.put("email", ig_email.getContentEdt().getText().toString());
            params.put("username", ig_name.getContentEdt().getText().toString());
            String gender = ig_gender.getContentEdt().getText().toString();
            params.put("gender", gender.equals("男")?"male":gender.equals("女")?"female":"unknown");
            params.put("birthday", ig_birthday.getContentEdt().getText().toString().equals("保密")?"":ig_birthday.getContentEdt().getText().toString());
            params.put("region", ig_region.getContentEdt().getText().toString());
            params.put("signature", ig_signature.getContentEdt().getText().toString());
            Api.config(ApiConfig.UPDATE_USER, params).postRequest(new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    //Log.e("updateUserInfo", res);
                    CommonResponse commonResponse = new Gson().fromJson(res, CommonResponse.class);
                    if (commonResponse.getCode() == 200) {
                        loginUser.setUsername(ig_name.getContentEdt().getText().toString());
                        loginUser.setRegion(ig_region.getContentEdt().getText().toString());
                        loginUser.setGender(ig_gender.getContentEdt().getText().toString());
                        loginUser.setBirthday(ig_birthday.getContentEdt().getText().toString());
                        loginUser.setSignature(ig_signature.getContentEdt().getText().toString());
                        loginUser.update();
                        updateSp(loginUser.getUser());
                        showToastSync("修改个人信息成功");
                    } else {
                        showToastSync("修改个人信息失败");
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    showToastSync("网络不佳，修改失败");
                }
            });
        }
        if (pwdIsDirty) {
            pwdIsDirty = false;
            HashMap<String, Object> params = new HashMap<>();
            params.put("userId", LoginUser.getInstance().getUser().getId());
            params.put("oldPassword", LoginUser.getInstance().getOldPwd());
            params.put("newPassword", LoginUser.getInstance().getNewPwd());
            //Log.e("old", LoginUser.getInstance().getOldPwd());
            //Log.e("new", LoginUser.getInstance().getNewPwd());
            handler.sendEmptyMessage(0);
            /*
            Api.config(ApiConfig.UPDATE_USER_PASSWORD, params).postRequest(new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    CommonResponse commonResponse = new Gson().fromJson(res, CommonResponse.class);
                    if (commonResponse.getCode() == 200) {
                        showToastSync("修改密码成功");
                    } else if (commonResponse.getCode() == 400) {
                        showToastSync("原密码不正确");
                    } else {
                        showToastSync("修改密码失败");
                    }
                    handler.sendEmptyMessage(0);
                }
                @Override
                public void onFailure(Exception e) {
                    showToastSync("网络不佳，修改密码失败");
                    handler.sendEmptyMessage(0);
                }
            });
             */
        }
    }


    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
        switch (v.getId()){
            //修改用户名
            case R.id.ig_name: {
                loginUser.setType(1);
                Intent intent = new Intent(UserInfoActivity.this, EditName.class);
                startActivityForResult(intent, EDIT_NAME);
                break;
            }
            //修改密码
            case R.id.ig_password: {
                loginUser.setType(7);
                Intent intent = new Intent(UserInfoActivity.this, EditName.class);
                startActivityForResult(intent, EDIT_PASSWORD);
                break;
            }
            //修改个性签名
            case R.id.ig_signature:{
                loginUser.setType(4);
                Intent intent = new Intent(UserInfoActivity.this, EditName.class);
                startActivityForResult(intent, EDIT_SIGNATURE);
                break;
            }
            //点击修改地区逻辑
            case R.id.ig_region: {
                pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //选择了则显示并暂存LoginUser，退出时在保存至数据库
                        String tx = options1Items.get(options1).getPickerViewText() + "-"
                                + (options2Items.get(options1).size() == 0?
                                options1Items.get(options1).getPickerViewText():
                                options2Items.get(options1).get(options2));
                        isDirty = isDirty || !ig_region.getContentEdt().getText().toString().equals(tx);
                        ig_region.getContentEdt().setText(tx);
                        loginUser.setRegion(tx);
                    }
                }).setCancelColor(Color.GRAY).build();
                pvOptions.setPicker(options1Items, options2Items);//二级选择器
                pvOptions.show();
                break;
            }
            //点击修改性别逻辑
            case R.id.ig_gender: {
                //性别选择器
                pvOptions = new OptionsPickerBuilder(UserInfoActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //选择了则显示并暂存LoginUser，退出时在保存至数据库
                        String tx = optionsItems_gender.get(options1);
                        isDirty = isDirty || !ig_gender.getContentEdt().getText().toString().equals(tx);
                        ig_gender.getContentEdt().setText(tx);
                        loginUser.setGender(tx);
                    }
                }).setCancelColor(Color.GRAY).build();
                pvOptions.setPicker(optionsItems_gender);
                pvOptions.show();
                break;
            }
            //点击修改生日逻辑
            case R.id.ig_birthday: {
                //时间选择器
                //修改打开的默认时间，如果选择过则是选择过的时间，否则是系统默认时间
                Calendar selectedDate = Calendar.getInstance();
                if (loginUser.getBirthday() != null) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        selectedDate.setTime(sdf.parse(loginUser.getBirthday()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                //初始化picker并show
                TimePickerView pvTime = new TimePickerBuilder(UserInfoActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        //选择了则显示并暂存LoginUser，退出时在保存至数据库
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        isDirty = isDirty || !ig_birthday.getContentEdt().getText().toString().equals(sdf.format(date));
                        ig_birthday.getContentEdt().setText(sdf.format(date));
                        loginUser.setBirthday(sdf.format(date));
                    }
                }).setDate(selectedDate).setCancelColor(Color.GRAY).build();
                pvTime.show();
                break;
            }
            //点击修改头像的逻辑
            case R.id.ll_portrait: {
                //展示选择框，并设置选择框的监听器
                show_popup_windows();
                break;
            }
            // 退出登录
            case R.id.ig_exitLogin: {
                exitLogin();
                break;
            }
            default:
                break;
        }
    }

    //处理回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        switch (requestCode){
            //拍照得到图片
            case TAKE_PHOTO:
                break;
            //从相册中选择图片
            case FROM_ALBUMS:
                if(resultCode == RESULT_OK){
                    //判断手机版本号
                    if(Build.VERSION.SDK_INT >= 19){
                        imagePath =  photoUtils.handleImageOnKitKat(this, data);
                    }else {
                        imagePath = photoUtils.handleImageBeforeKitKat(this, data);
                    }
                }
                if(imagePath != null){
                    //将拍摄的图片展示
                    Uri uri = UriUtils.getImageContentUri(this, imagePath);
                    Bitmap bitmap = ImageUtil.getBitmapFromUri(this, uri);
                    //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    ri_portrati.setImageBitmap(bitmap);
                    //loginUser.setHeadPortraitPath(PhotoUtils.bitmapToString(bitmap));
                }else{
                    Log.d("travel","没有找到图片");
                }
                break;
            //如果是编辑名字，则修改展示
            case EDIT_NAME:
                if(resultCode == RESULT_OK){
                    isDirty = isDirty || !ig_name.getContentEdt().getText().toString().equals(loginUser.getTempString());
                    ig_name.getContentEdt().setText(loginUser.getTempString());
                }
                break;
            case EDIT_PHONENUM:{
                if (resultCode == RESULT_OK) {
                    ig_phoneNum.getContentEdt().setText(loginUser.getTempString());
                }
                break;
            }
            case EDIT_EMAIL: {
                if (resultCode == RESULT_OK) {
                    ig_email.getContentEdt().setText(loginUser.getTempString());
                }
                break;
            }
            case EDIT_SIGNATURE: {
                if (resultCode == RESULT_OK) {
                    isDirty = isDirty || !ig_signature.getContentEdt().getText().toString().equals(loginUser.getTempString());
                    ig_signature.getContentEdt().setText(loginUser.getTempString());
                }
                break;
            }
            case EDIT_PASSWORD: {
                if (resultCode == RESULT_OK) {
                    pwdIsDirty = true;
                    ig_password.getContentEdt().setText("请确认修改密码");
                }
                break;
            }
            default:
                break;
        }
    }
    //初始化数据并展示
    private void initInfo(){
        LoginUser loginUser = LoginUser.getInstance();
        //ig_id.getContentEdt().setText(String.valueOf(loginUser.getUser().getId()));  //ID是int，转string
        ig_name.getContentEdt().setText(loginUser.getUser().getUsername());
        if (!StringUtils.isEmpty(loginUser.getUser().getPhoneNum())) {
            ig_phoneNum.getContentEdt().setText(loginUser.getUser().getPhoneNum());
        }
        if (!StringUtils.isEmpty(loginUser.getUser().getEmail())) {
            ig_email.getContentEdt().setText(loginUser.getUser().getEmail());
        }
        ig_signature.getContentEdt().setText(loginUser.getUser().getSignature());
        if (loginUser.getUser() != null &&
            !loginUser.getUser().getHeadPortraitPath().equals("default") &&
            !loginUser.getUser().getHeadPortraitPath().equals("")) {
            //ri_portrati.setImageBitmap(PhotoUtils.stringToBitmap(loginUser.getUser().getHeadPortraitPath()));
            Picasso.with(this)
                    .load(LoginUser.getInstance().getUser().getHeadPortraitPath())
                    .into(ri_portrati);
        }
        ig_gender.getContentEdt().setText(loginUser.getUser().getGender());
        ig_region.getContentEdt().setText(loginUser.getUser().getRegion());
        ig_birthday.getContentEdt().setText(loginUser.getUser().getBirthday());
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


    //展示修改头像的选择框，并设置选择框的监听器
    private void show_popup_windows(){
        RelativeLayout layout_photo_selected = (RelativeLayout) getLayoutInflater().inflate(R.layout.photo_select,null);
        if(popupWindow==null){
            popupWindow = new PopupWindow(layout_photo_selected, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        }
        //显示popupwindows
        popupWindow.showAtLocation(layout_photo_selected, Gravity.CENTER, 0, 0);
        TextView from_albums = (TextView)  layout_photo_selected.findViewById(R.id.from_albums);
        LinearLayout cancel = (LinearLayout) layout_photo_selected.findViewById(R.id.cancel);
        //相册按钮监听
        from_albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //申请权限
                if(ContextCompat.checkSelfPermission(UserInfoActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(UserInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    //打开相册
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, FROM_ALBUMS);
                }
                //去除选择框
                popupWindow.dismiss();
            }
        });
        //取消按钮监听
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:{
                    //在主线程中执行
                    ig_password.getContentEdt().setText("");
                    break;
                }
                default:{
                    finish();
                    break;
                }
            }
        }
    };

}