package com.example.travel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.example.travel.R;
import com.example.travel.adapter.ImageAdapter;
import com.example.travel.fragment.HomeFragment;
import com.example.travel.fragment.UserInfoFragment;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

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

    private Button selectImage_btn;
    private EditText noteName;
    private EditText noteMain;

    @Override
    protected int initLayout() {
        return R.layout.activity_select_image;
    }

    @Override
    protected void initView() {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rvImage = findViewById(R.id.rv_image);
        rvImage.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new ImageAdapter(this);
        rvImage.setAdapter(mAdapter);

        selectImage_btn = findViewById(R.id.si_selectImage_btn);
        noteName = findViewById(R.id.si_noteName);
        noteMain = findViewById(R.id.si_noteMain);

        selectImage_btn.setOnClickListener(this);
        noteName.setHorizontallyScrolling(false);
        noteMain.setSingleLine(false);
        noteMain.setHorizontallyScrolling(false);
        noteMain.setMinLines(5);
        noteMain.setMaxEms(255);

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
        navigationBar = findViewById(R.id.si_navigationBar);
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
                            }
                        });
                        return false;
                    }
                })
                .canScroll(true)
                .mode(EasyNavigationBar.NavigationMode.MODE_ADD)
                .build();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
//            Log.d("ImageSelector", "是否是拍照图片：" + isCameraImage);
            mAdapter.refresh(images);
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.si_selectImage_btn: {
                //多选(最多9张)
                ImageSelector.builder()
                        .useCamera(false) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .canPreview(true) //是否点击放大图片查看,，默认为true
                        .setMaxSelectCount(9) // 图片的最大选择数量，小于等于0时，不限数量。
                        .start(this, REQUEST_CODE); // 打开相册
                break;
            }
            default:{

            }
        }
    }
}