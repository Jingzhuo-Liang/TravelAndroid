package com.example.travel.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.example.travel.R;
import com.example.travel.adapter.ImageAdapter;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.util.CityBean;
import com.example.travel.util.LoginUser;
import com.example.travel.util.PhotoUtils;
import com.example.travel.util.ProvinceBean;
import com.example.travel.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.next.easynavigation.view.EasyNavigationBar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    private static final int REQUEST_LOCATION = 0x00000013;

    private RecyclerView rvImage;
    private ImageAdapter mAdapter;
    private EasyNavigationBar navigationBar;
    private String[] tabText = {};
    //未选中icon
    private int[] normalIcon = {R.mipmap.pure_white_icon, R.mipmap.pure_white_icon};
    //选中时icon
    private int[] selectIcon = {R.mipmap.pure_white_icon, R.mipmap.pure_white_icon};
    private List<Fragment> fragments = new ArrayList<>();
    private Handler mHandler = new Handler();
    private boolean flag = true;
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private Location userLocation;

    private Button selectImage_btn;
    private Button arrowBack_btn;
    private EditText recordName;
    private EditText recordMain;
    private TextView recordRegion;
    private TextView recordLocation;
    private TextView selectImageHint;

    private LocationManager locationManager;
    private String locationProvider;

    //private ImageView imageTest;

    @Override
    protected int initLayout() {
        return R.layout.activity_select_image;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        rvImage = findViewById(R.id.rv_image);
        selectImage_btn = findViewById(R.id.si_selectImage_btn);
        arrowBack_btn = findViewById(R.id.si_arrowBack_btn);
        recordName = findViewById(R.id.si_recordName);
        recordMain = findViewById(R.id.si_recordMain);
        recordRegion = findViewById(R.id.si_recordRegion);
        navigationBar = findViewById(R.id.si_navigationBar);
        recordLocation = findViewById(R.id.si_recordLocation);
        recordLocation = findViewById(R.id.si_recordLocation);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        selectImageHint = findViewById(R.id.si_selectImage_hint);

        selectImage_btn.setOnClickListener(this);
        arrowBack_btn.setOnClickListener(this);
        recordRegion.setOnClickListener(this);
        recordLocation.setOnClickListener(this);

        rvImage.setLayoutManager(new GridLayoutManager(this, 3));
        recordName.setHorizontallyScrolling(false);
        recordMain.setSingleLine(false);
        recordMain.setHorizontallyScrolling(false);
        recordMain.setMinLines(5);
        recordMain.setMaxEms(255);

        mAdapter = new ImageAdapter(this);
        rvImage.setAdapter(mAdapter);

        //imageTest = findViewById(R.id.si_image_test);
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
                if (mAdapter.getImages().size() > 0) {
                    selectImageHint.setText("");
                }
                break;
            }
            case R.id.si_recordRegion: {
                pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //选择了则显示并暂存LoginUser，退出时在保存至数据库
                        String tx = options1Items.get(options1).getPickerViewText() + "-"
                                + (options2Items.get(options1).size() == 0 ?
                                options1Items.get(options1).getPickerViewText() :
                                options2Items.get(options1).get(options2));
                        recordRegion.setText(tx);
                    }
                }).setCancelColor(Color.GRAY).build();
                pvOptions.setPicker(options1Items, options2Items);//二级选择器
                pvOptions.show();
                break;
            }
            case R.id.si_recordLocation: {
                userLocation = getLocation();
                if (userLocation != null) {
                    recordLocation.setText("获取位置成功");
                    //Log.e("latitude",String.valueOf(userLocation.getLatitude()));
                    //Log.e("longitude",String.valueOf(userLocation.getLongitude()));
                } else {
                    recordLocation.setText("获取位置失败");
                }
            }
            default: {

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
        ArrayList<String> imagePaths = mAdapter.getImages();
        ArrayList<String> images = new ArrayList<>();
        if (imagePaths.size() == 0) {
            showToast("请至少选择一张图片");
            return;
        }
        //根据图片路径获取图片并转成base64字符串
        for (int i = 0;i < imagePaths.size();i++) {
            images.add(PhotoUtils.bitmapToString(PhotoUtils.getBitmap(imagePaths.get(i))));
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", LoginUser.getInstance().getUser().getId());
        params.put("recordName", recordName.getText().toString());
        params.put("recordMain", recordMain.getText().toString());
        params.put("recordRegion", recordRegion.getText().toString());
        params.put("recordImages", images);
        if (userLocation != null) {
            params.put("latitude", userLocation.getLatitude());
            params.put("longitude", userLocation.getLongitude());
        }

        Api.config(ApiConfig.RELEASE_TRAVEL_RECORD, params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                showToastSync("发布成功");
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        finish();
    }


    private void initOptionData() {
        Gson gson = new Gson();
        options1Items = gson.fromJson(province_data, new TypeToken<ArrayList<ProvinceBean>>() {
        }.getType());
        ArrayList<CityBean> cityBean_data = gson.fromJson(city_data, new TypeToken<ArrayList<CityBean>>() {
        }.getType());
        for (ProvinceBean provinceBean : options1Items) {
            ArrayList<String> temp = new ArrayList<>();
            for (CityBean cityBean : cityBean_data) {
                if (provinceBean.getProvince().equals(cityBean.getProvince())) {
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
                                //showNormalDialog();
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

    // 提示框
    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        AlertDialog normalDialog =
                new AlertDialog.Builder(SelectImageActivity.this).create();
        normalDialog.setMessage("确认发布");
        normalDialog.setButton(DialogInterface.BUTTON_POSITIVE, "是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                releaseTravelRecord();
            }
        });
        normalDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this, "您单击了否按钮", Toast.LENGTH_SHORT).show();
                //Log.e("click cancel","2222");
            }
        });
        normalDialog.show();
        WindowManager.LayoutParams params = normalDialog.getWindow().getAttributes();
        params.width = 800;
        params.height = 600;
        params.gravity = Gravity.CENTER;
        normalDialog.getWindow().setAttributes(params);
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
        } else if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Log.e("permission","here");

            } else {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            mAdapter.refresh(images);
        }
    }

    private Location getLocation() {
        int hasWriteExternalPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasWriteExternalPermission != PackageManager.PERMISSION_GRANTED) {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            //Log.e("GPS","here");
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            //Log.e("NETWORK","here");
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            //showToastSync("没有可用的位置提供器");
            return null;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
        return location;
    }

    /**
     * LocationListen监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */
    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationManager!=null){
            //移除监听器
            locationManager.removeUpdates(locationListener);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}