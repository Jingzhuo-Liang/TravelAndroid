package com.example.travel.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.ImageUtil;
import com.donkingliang.imageselector.utils.UriUtils;
import com.example.travel.R;
import com.example.travel.adapter.ImageAdapter;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.entity.CommonNoDataResponse;
import com.example.travel.entity.ModifyTravelRecordEntity;
import com.example.travel.entity.ModifyTravelRecordResponse;
import com.example.travel.util.CityBean;
import com.example.travel.util.LoginUser;
import com.example.travel.util.MyLocationListener;
import com.example.travel.util.PhotoUtils;
import com.example.travel.util.ProvinceBean;
import com.example.travel.util.StringUtils;
import com.example.travel.util.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.next.easynavigation.view.EasyNavigationBar;
import com.siberiadante.customdialoglib.CustomDialog;
import com.siberiadante.customdialoglib.EnsureDialog;
import com.wang.avi.AVLoadingIndicatorView;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.nio.channels.SelectionKey;
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

    private static final int REQUEST_LIST_CODE = 0;

    private int recordLimitCode = -1;
    private boolean recordImagesDirty = false;
    private String recordId = "";

    private RecyclerView rvImage;
    private ImageAdapter mAdapter;
    private EasyNavigationBar navigationBar;
    private String[] tabText = {};
    //?????????icon
    private int[] normalIcon = {R.mipmap.pure_white_icon, R.mipmap.pure_white_icon};
    //?????????icon
    private int[] selectIcon = {R.mipmap.pure_white_icon, R.mipmap.pure_white_icon};
    private List<Fragment> fragments = new ArrayList<>();
    private Handler mHandler = new Handler();
    private boolean flag = true;
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<String> optionsLimit = new ArrayList<>();
    private Location userLocation;
    private ModifyTravelRecordEntity modifyTravelRecordEntity;

    private Button selectImage_btn;
    private Button arrowBack_btn;
    private EditText recordName;
    private EditText recordMain;
    private TextView recordRegion;
    private TextView recordLocation;
    private TextView selectImageHint;
    private TextView recordLimit;

    private LocationManager locationManager;
    private String locationProvider;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private MaterialDialog mLoadingDialog;
    private EnsureDialog ensureDialog;
    //private Boolean locDifButCon = false; //location is different but continue
    private String selectRegion = "";
    private ArrayList<String> imagePaths;
    private ProgressDialog progressDialog;

    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:{
                    //?????????????????????
                    //progressDialog.dismiss();//???????????????
                    //dismissLoadingDialog();
                    //finish();
                    break;
                }
                case 1: {
                    setModifyData();
                    break;
                }
                default:{

                }
            }
        }
    };

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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        selectImageHint = findViewById(R.id.si_selectImage_hint);
        recordLimit = findViewById(R.id.si_recordLimit);

        /* ???enableAllButton?????????
        selectImage_btn.setOnClickListener(this);
        arrowBack_btn.setOnClickListener(this);
        recordRegion.setOnClickListener(this);
        recordLocation.setOnClickListener(this);
        recordLimit.setOnClickListener(this);
         */

        rvImage.setLayoutManager(new GridLayoutManager(this, 3));
        recordName.setHorizontallyScrolling(false);
        recordName.setSingleLine(false);
        recordName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        recordMain.setSingleLine(false);
        recordMain.setHorizontallyScrolling(false);
        recordMain.setMinLines(5);
        recordMain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});

        mAdapter = new ImageAdapter(this);
        rvImage.setAdapter(mAdapter);
        ImageSelector.preload(context);
        getLocation();
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
    }


    @Override
    protected void initData() {
        initOptionData();
        setNavigationBar();
        Bundle bundle = this.getIntent().getExtras();
        recordId = bundle.getString("recordId");
        if (recordId != null && recordId.length() > 0) {
            getModifyData(recordId);
        } else {
            //Log.e("recordId", "is null");
        }
    }

    private void getModifyData(String recordId) {
        HashMap<String , Object> params = new HashMap<>();
        params.put("userId",LoginUser.getInstance().getUser().getId());
        params.put("recordId",recordId);
        Api.config(ApiConfig.MODIFY_MY_TRAVEL_RECORD_STEP1,params).getRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                ModifyTravelRecordResponse mtr = new Gson().fromJson(res,ModifyTravelRecordResponse.class);
                if (mtr.getCode() == 200) {
                    modifyTravelRecordEntity = mtr.getData();
                    handler.sendEmptyMessage(1);
                } else {
                    showToastSync("??????????????????");
                }
                //Log.e("modifyRecord_get",res);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void setModifyData() {
        if (modifyTravelRecordEntity == null) {
            return;
        }
        mAdapter.refresh(modifyTravelRecordEntity.getRecordImages());
        recordName.setText(modifyTravelRecordEntity.getRecordName());
        recordMain.setText(modifyTravelRecordEntity.getRecordMain());
        recordRegion.setText(modifyTravelRecordEntity.getRecordRegion());
        recordLimit.setText(optionsLimit.get(modifyTravelRecordEntity.getRecordLimit()));
        recordLimitCode = modifyTravelRecordEntity.getRecordLimit();
        myListener.setRegion(modifyTravelRecordEntity.getRecordRegion());
        if (modifyTravelRecordEntity.getLatitude() != 0 && modifyTravelRecordEntity.getLongitude() != 0) {
            recordLocation.setText("??????????????????");
            myListener.setLongitude(modifyTravelRecordEntity.getLongitude());
            myListener.setLatitude(modifyTravelRecordEntity.getLatitude());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.si_arrowBack_btn: {
                finish();
                break;
            }
            case R.id.si_selectImage_btn: {
                /*
                //??????(??????9???)
                ImageSelector.builder()
                        .useCamera(false) // ????????????????????????
                        .setSingle(false)  //??????????????????
                        .canPreview(true) //??????????????????????????????,????????????true
                        .setMaxSelectCount(9) // ??????????????????????????????????????????0?????????????????????
                        .start(this, REQUEST_CODE); // ????????????

                */
                ISListConfig config = new ISListConfig.Builder()
                        // ????????????, ??????true
                        .multiSelect(true)
                        // ??????????????????????????????, ??????multiSelect???true???????????????????????????true
                        .rememberSelected(false)
                        // ???????????????????????????
                        .btnBgColor(Color.GRAY)
                        // ??????????????????????????????
                        .btnTextColor(Color.BLUE)
                        // ????????????????????????
                        .statusBarColor(Color.parseColor("#FF000000"))
                        // ??????
                        .title("????????????")
                        // ??????????????????
                        .titleColor(Color.WHITE)
                        // TitleBar?????????
                        .titleBgColor(Color.parseColor("#3F51B5"))
                        // ????????????????????????????????????true
                        .needCamera(false)
                        // ?????????????????????????????????9
                        .maxNum(9)
                        .build();

                // ????????????????????????
                ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
                break;
            }
            case R.id.si_recordRegion: {
                hideKeyboard(recordMain);
                ensureDialog = new EnsureDialog(this).builder()
                        .setGravity(Gravity.CENTER)//??????????????????????????????
                        .setTitle("???????????????????????????????????????????????????", getResources().getColor(R.color.black))//????????????????????????????????????????????????
                        .setCancelable(false)
                        .setSubTitle("????????????",getResources().getColor(R.color.yellow0));
                ensureDialog.setNegativeButton("??????", getResources().getColor(R.color.red0), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //locDifButCon = false;
                        ensureDialog.dismiss();
                    }
                });
                ensureDialog.setPositiveButton("??????", getResources().getColor(R.color.red0), new View.OnClickListener() {//??????????????????????????????????????????????????????
                    @Override
                    public void onClick(View view) {
                        recordRegion.setText(selectRegion);
                        ensureDialog.dismiss();

                    }
                });
                pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //???????????????????????????LoginUser?????????????????????????????????
                        selectRegion = options1Items.get(options1).getPickerViewText() + "-"
                                + (options2Items.get(options1).size() == 0 ?
                                options1Items.get(options1).getPickerViewText() :
                                options2Items.get(options1).get(options2));
                        if (!StringUtils.isEmpty(myListener.getRegion()) &&!myListener.getRegion().equals("null-null") && !myListener.getRegion().equals(selectRegion)) {
                            //customDialog.show();
                            ensureDialog.show();
                        } else {
                            recordRegion.setText(selectRegion);
                        }
                    }
                }).setCancelColor(Color.GRAY).build();
                pvOptions.setPicker(options1Items, options2Items);//???????????????
                pvOptions.show();
                break;
            }
            case R.id.si_recordLocation: {
                //userLocation = getLocation();
                //getLocation();
                if (myListener.getLocation() != null) {
                    //mLocationClient.stop();
                    recordLocation.setText("??????????????????");
                    if (!myListener.getRegion().equals("null-null")) {
                        recordRegion.setText(myListener.getRegion());
                    } else {
                        getLocation();
                        if (!myListener.getRegion().equals("null-null")) {
                            recordRegion.setText(myListener.getRegion());
                        }
                    }
                } else {
                    getLocation();
                    if (myListener.getLocation() != null) {
                        //mLocationClient.stop();
                        recordLocation.setText("??????????????????");
                        if (!myListener.getRegion().equals("null-null")) {
                            recordRegion.setText(myListener.getRegion());
                        }
                    } else {
                        recordLocation.setText("??????????????????");
                    }
                }
                break;
            }
            case R.id.si_recordLimit : {
                //dismissSoftKeyBoard();
                hideKeyboard(recordName);
                pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //???????????????????????????LoginUser?????????????????????????????????
                        String tx = optionsLimit.get(options1);
                        //Log.e("recordLimit",String.valueOf(options1));
                        recordLimit.setText(tx);
                        recordLimitCode = options1;
                    }
                }).setCancelColor(Color.GRAY).build();
                pvOptions.setPicker(optionsLimit);//???????????????
                pvOptions.show();
                break;
            }
            default: {

            }
        }
    }


    private void modifyTravelRecord() {
        disableAllButton();
        if (StringUtils.isEmpty(recordRegion.getText().toString())) {
            enableAllButton();
            showToast("???????????????");
            return;
        }
        else if (StringUtils.isEmpty(recordName.getText().toString())) {
            enableAllButton();
            showToast("?????????????????????");
            return;
        } else if (recordLimitCode == -1) {
            enableAllButton();
            showToast("???????????????");
            return;
        }
        //showLoadingDialog();
        ArrayList<String> imagePaths = mAdapter.getImages();
        ArrayList<String> images = new ArrayList<>();
        if (imagePaths.size() == 0) {
            enableAllButton();
            //dismissLoadingDialog();
            showToast("???????????????????????????");
            return;
        }
        hideKeyboard(recordMain);
        new Thread(new Runnable() {
            @SuppressLint("HandlerLeak")
            @Override
            public void run() {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 4;
                if (recordImagesDirty) { //???????????????
                    for (int i = 0;i < imagePaths.size();i++) {
                        Uri uri = UriUtils.getImageContentUri(SelectImageActivity.this, imagePaths.get(i));
                        Bitmap bitmap = ImageUtil.getBitmapFromUri(SelectImageActivity.this, uri,opts);
                        images.add(PhotoUtils.bitmapToString(bitmap));
                        if (bitmap.getByteCount() > ApiConfig.IMAGE_MAX_LIMIT) {
                            enableAllButton();
                            showToast("???????????????????????????????????????");
                            //dismissLoadingDialog();
                            return;
                        }
                    }
                }
                HashMap<String, Object> params = new HashMap<>();
                params.put("userId", LoginUser.getInstance().getUser().getId());
                params.put("recordId", recordId);
                params.put("recordName", recordName.getText().toString());
                params.put("recordMain", recordMain.getText().toString());
                params.put("recordRegion", recordRegion.getText().toString());
                params.put("recordLimit", recordLimitCode);
                params.put("recordImages", images);
                if (myListener.getLatitude() != 0 && myListener.getLongitude() != 0) {
                    //params.put("latitude", userLocation.getLatitude());
                    //params.put("longitude", userLocation.getLongitude());
                    params.put("latitude",myListener.getLatitude());
                    params.put("longitude", myListener.getLongitude());
                }
                //Log.e("images",String.valueOf(images.size()));
                Api.config(ApiConfig.MODIFY_MY_TRAVEL_RECORD_STEP2, params).postRequest(new TtitCallback() {
                    @Override
                    public void onSuccess(String res) {
                        //Log.e("releaseRecord",res);
                        try {
                            CommonNoDataResponse commonResponse = new Gson().fromJson(res, CommonNoDataResponse.class);
                            if (commonResponse.getCode() == 200) {
                                handler.sendEmptyMessage(0);
                                showToastSync("??????????????????");
                            } else {
                                handler.sendEmptyMessage(0);
                                showToastSync("??????????????????");
                            }
                        } catch (Exception e) {
                            handler.sendEmptyMessage(0);
                            showToastSync("??????????????????");
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        handler.sendEmptyMessage(0);
                        showToastSync("?????????????????????????????????");
                    }
                });
            }
        }).start();
        showToast("??????????????????????????????");
        finish();
    }

    private void releaseTravelRecord() {
        disableAllButton();
        //Log.d("clickRelease",recordRegion.getText().toString());
        if (StringUtils.isEmpty(recordRegion.getText().toString())) {
            enableAllButton();
            //dismissLoadingDialog();
            showToast("???????????????");
            return;
        } else if (StringUtils.isEmpty(recordName.getText().toString())) {
            enableAllButton();
            //dismissLoadingDialog();
            showToast("?????????????????????");
            return;
        } else if (recordLimitCode == -1) {
            enableAllButton();
            //dismissLoadingDialog();
            showToast("???????????????");
            return;
        }
        //showLoadingDialog();
        ArrayList<String> images = new ArrayList<>();
        if (imagePaths == null || imagePaths.size() == 0) {
            enableAllButton();
            //dismissLoadingDialog();
            showToast("???????????????????????????");
            return;
        }
        hideKeyboard(recordMain);
        new Thread(new Runnable() {
            @SuppressLint("HandlerLeak")
            @Override
            public void run() {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 4;
                for (int i = 0;i < imagePaths.size();i++) {
                    Uri uri = UriUtils.getImageContentUri(SelectImageActivity.this, imagePaths.get(i));
                    Bitmap bitmap = ImageUtil.getBitmapFromUri(SelectImageActivity.this, uri, opts);
                    //Log.e("bitmapByteCount",String.valueOf(bitmap.getByteCount()));
                    if (bitmap.getByteCount() > ApiConfig.IMAGE_MAX_LIMIT) {
                        //enableAllButton();
                        showToastSync("???????????????????????????????????????");
                        //dismissLoadingDialog();
                        return;
                    }
                    images.add(PhotoUtils.bitmapToString(bitmap));
                }
                HashMap<String, Object> params = new HashMap<>();
                params.put("userId", LoginUser.getInstance().getUser().getId());
                params.put("recordName", recordName.getText().toString());
                params.put("recordMain", recordMain.getText().toString());
                params.put("recordRegion", recordRegion.getText().toString());
                params.put("recordLimit", recordLimitCode);
                params.put("recordImages", images);
                if (myListener.getLatitude() != 0 && myListener.getLongitude() != 0) {
                    //params.put("latitude", userLocation.getLatitude());
                    //params.put("longitude", userLocation.getLongitude());
                    params.put("latitude",myListener.getLatitude());
                    params.put("longitude", myListener.getLongitude());
                }
                //Log.e("images",String.valueOf(images.size()));
                Api.config(ApiConfig.RELEASE_TRAVEL_RECORD, params).postRequest(new TtitCallback() {
                    @Override
                    public void onSuccess(String res) {
                        //Log.e("releaseRecord",res);
                        try {
                            CommonNoDataResponse commonResponse = new Gson().fromJson(res, CommonNoDataResponse.class);
                            if (commonResponse.getCode() == 200) {
                                handler.sendEmptyMessage(0);
                                showToastSync("??????????????????");
                            } else {
                                handler.sendEmptyMessage(0);
                                showToastSync("??????????????????");
                            }
                        } catch (Exception e) {
                            handler.sendEmptyMessage(0);
                            showToastSync("??????????????????");
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        handler.sendEmptyMessage(0);
                        showToastSync("?????????????????????????????????");
                    }
                });
            }
        }).start();
        showToast("??????????????????????????????");
        finish();
    }


    private void initOptionData() {
        optionsLimit.add("????????????");
        optionsLimit.add("???????????????");
        optionsLimit.add("???????????????");

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
            //???????????????????????????????????????????????????app????????????????????????
            ImageSelector.preload(this);
        } else {
            //??????????????????????????????
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_REQUEST_CODE);
        }
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .centerImageRes(R.mipmap.release_icon)
                .centerTextStr("??????")
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

                });
        enableAllButton();
    }

    private void enableAllButton() {
        navigationBar.setOnCenterTabClickListener(new EasyNavigationBar.OnCenterTabSelectListener() {
            @Override
            public boolean onCenterTabSelectEvent(View view) {
                    if (TimeUtils.isFastDoubleClickWithin2Second()) {
                        //Log.e("click quickly","here");
                        return false;
                    }
                    //Log.e("releaseRecord","1");
                    //showLoadingDialog();
                    //Log.e("click not quickly","here");
                    if (!StringUtils.isEmpty(recordId)) {
                        modifyTravelRecord();
                    } else {
                        releaseTravelRecord();
                    }
                    return true;
                }})
                .canScroll(true)
                .mode(EasyNavigationBar.NavigationMode.MODE_ADD)
                .build();
        selectImage_btn.setOnClickListener(this);
        arrowBack_btn.setOnClickListener(this);
        recordRegion.setOnClickListener(this);
        recordLocation.setOnClickListener(this);
        recordLimit.setOnClickListener(this);
        recordMain.setEnabled(true);
        recordName.setEnabled(true);
    }

    private void disableAllButton() {
        navigationBar.setOnCenterTabClickListener(null);
        selectImage_btn.setOnClickListener(null);
        arrowBack_btn.setOnClickListener(null);
        recordRegion.setOnClickListener(null);
        recordLocation.setOnClickListener(null);
        recordLimit.setOnClickListener(null);
        recordMain.setEnabled(false);
        recordName.setEnabled(false);
    }



    /**
     * ??????????????????????????????
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
                //?????????????????????
                ImageSelector.preload(this);
            } else {
                //???????????????
            }
        } else if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Log.e("permission","here");

            } else {

            }
        }
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            mAdapter.refresh(images);
            recordImagesDirty = true;
        }
    }
     */

    private void getLocation() {
        int hasWriteExternalPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasWriteExternalPermission != PackageManager.PERMISSION_GRANTED) {
            //??????????????????????????????
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        /*
        //????????????????????????????????????
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //?????????GPS
            //Log.e("GPS","here");
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //?????????Network
            //Log.e("NETWORK","here");
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            //showToastSync("??????????????????????????????");
            return null;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        //????????????????????????
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
        */

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setAddrType("all");// ???????????????????????????????????????
        option.setCoorType("bd09ll");
        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //?????????????????????????????????????????????????????????????????????false
        //?????????????????????????????????????????????????????????????????????true

        option.setNeedNewVersionRgc(true);
        //????????????????????????????????????????????????????????????????????????????????????true
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * LocationListen?????????
     * ???????????????????????????????????????????????????????????????????????????????????????????????????LocationListener?????????
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
            //????????????????????????,????????????
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationManager!=null){
            //???????????????
            locationManager.removeUpdates(locationListener);
        }
        ImageSelector.clearCache(context);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /*
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new MaterialDialog.Builder(this)
                    .widgetColorRes(R.color.colorPrimary)
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
            mLoadingDialog.setContent("???????????????...");
        }
        mLoadingDialog.show();
    }
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ????????????????????????
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            imagePaths = data.getStringArrayListExtra("result");
            mAdapter.refresh(imagePaths);
            recordImagesDirty = true;
        }
    }
}