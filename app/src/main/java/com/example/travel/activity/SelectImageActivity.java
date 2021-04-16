package com.example.travel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.example.travel.R;
import com.example.travel.adapter.ImageAdapter;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.fragment.HomeFragment;
import com.example.travel.fragment.UserInfoFragment;
import com.example.travel.util.CityBean;
import com.example.travel.util.LoginUser;
import com.example.travel.util.ProvinceBean;
import com.example.travel.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class SelectImageActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 0x00000011;
    private static final int PERMISSION_WRITE_EXTERNAL_REQUEST_CODE = 0x00000012;

    private RecyclerView rvImage;
    private ImageAdapter mAdapter;
    private EasyNavigationBar navigationBar;
    private String[] tabText = {};
    //未选中icon
    private int[] normalIcon = {R.mipmap.pure_white_icon,  R.mipmap.pure_white_icon};
    //选中时icon
    private int[] selectIcon = {R.mipmap.pure_white_icon, R.mipmap.pure_white_icon};
    private List<Fragment> fragments = new ArrayList<>();
    private Handler mHandler = new Handler();
    private boolean flag = true;
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    private Button selectImage_btn;
    private Button arrowBack_btn;
    private EditText recordName;
    private EditText recordMain;
    private TextView recordRegion;

    @Override
    protected int initLayout() {
        return R.layout.activity_select_image;
    }

    @Override
    protected void initView() {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rvImage = findViewById(R.id.rv_image);
        selectImage_btn = findViewById(R.id.si_selectImage_btn);
        arrowBack_btn = findViewById(R.id.si_arrowBack_btn);
        recordName = findViewById(R.id.si_recordName);
        recordMain = findViewById(R.id.si_recordMain);
        recordRegion = findViewById(R.id.si_recordRegion);
        navigationBar = findViewById(R.id.si_navigationBar);

        selectImage_btn.setOnClickListener(this);
        arrowBack_btn.setOnClickListener(this);
        recordRegion.setOnClickListener(this);

        rvImage.setLayoutManager(new GridLayoutManager(this, 3));
        recordName.setHorizontallyScrolling(false);
        recordMain.setSingleLine(false);
        recordMain.setHorizontallyScrolling(false);
        recordMain.setMinLines(5);
        recordMain.setMaxEms(255);

        mAdapter = new ImageAdapter(this);
        rvImage.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        initOptionData();
        setNavigationBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.si_arrowBack_btn: {
                finish();
                break;
            }
            case R.id.si_selectImage_btn: {
                //Log.e("selectImage","here???");
                //多选(最多9张)
                ImageSelector.builder()
                        .useCamera(false) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .canPreview(true) //是否点击放大图片查看,，默认为true
                        .setMaxSelectCount(9) // 图片的最大选择数量，小于等于0时，不限数量。
                        .start(this, REQUEST_CODE); // 打开相册
                break;
            }
            case R.id.si_recordRegion:{
                pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //选择了则显示并暂存LoginUser，退出时在保存至数据库
                        String tx = options1Items.get(options1).getPickerViewText() + "-"
                                + (options2Items.get(options1).size() == 0?
                                options1Items.get(options1).getPickerViewText():
                                options2Items.get(options1).get(options2));
                        recordRegion.setText(tx);
                        //ig_region.getContentEdt().setText(tx);
                        //loginUser.setRegion(tx);
                    }
                }).setCancelColor(Color.GRAY).build();
                pvOptions.setPicker(options1Items, options2Items);//二级选择器
                pvOptions.show();
                break;
            }
            default:{

            }
        }
    }

    private void releaseTravelRecord() {
        if (StringUtils.isEmpty(recordRegion.getText().toString())) {
            showToast("请选项城市");
            return;
        } else if (StringUtils.isEmpty(recordName.getText().toString())) {
            showToast("请输入游记名字");
            return;
        }
        ArrayList<String> images = mAdapter.getImages();
        if (images.size() == 0) {
            showToast("请至少选择一张图片");
            return;
        }
        //Log.e("releaseTravelRecord",String.valueOf(images.size()));
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", LoginUser.getInstance().getUser().getId());
        params.put("recordName",recordName.getText().toString());
        params.put("recordMain",recordMain.getText().toString());
        params.put("recordRegion",recordRegion.getText().toString());
        params.put("recordImages",images);
        Api.config(ApiConfig.RELEASE_TRAVEL_RECORD,params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                Log.e("release success",res);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
    private void initOptionData() {
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

    private void setNavigationBar() {
        int hasWriteExternalPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteExternalPermission == PackageManager.PERMISSION_GRANTED) {
            //预加载手机图片。加载图片前，请确保app有读取储存卡权限
            ImageSelector.preload(this);
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_REQUEST_CODE);
        }
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .centerImageRes(R.mipmap.release_icon)
                .centerTextStr("发布")
                //.anim(null)
                .centerLayoutRule(EasyNavigationBar.RULE_BOTTOM)
                .centerLayoutBottomMargin(0)
                .centerAlignBottom(true)
                .fragmentManager(getSupportFragmentManager())
                .setOnTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabSelectEvent(View view, int position) {
                        return false;
                    }

                    @Override
                    public boolean onTabReSelectEvent(View view, int position) {
                        return false;
                    }

                })
                .setOnCenterTabClickListener(new EasyNavigationBar.OnCenterTabSelectListener() {
                    @Override
                    public boolean onCenterTabSelectEvent(View view) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                //＋ 旋转动画
//                                if (flag) {
//                                    navigationBar.getCenterImage().animate().rotation(45).setDuration(400);
//                                } else {
//                                    navigationBar.getCenterImage().animate().rotation(0).setDuration(400);
//                                }
                                flag = !flag;
                                //Log.e("on tabSelectEvent","click");
                                //navigateTo(SelectImageActivity.class);
                                releaseTravelRecord();
                            }
                        });
                        return false;
                    }
                })
                .canScroll(true)
                .mode(EasyNavigationBar.NavigationMode.MODE_ADD)
                .build();
    }

    /**
     * 处理权限申请的回调。
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_WRITE_EXTERNAL_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //预加载手机图片
                ImageSelector.preload(this);
            } else {
                //拒绝权限。
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            //boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
            //Log.d("ImageSelector", "是否是拍照图片：" + isCameraImage);
            Log.e("onResult",String.valueOf(images.size()));
            mAdapter.refresh(images);
        }
    }
}