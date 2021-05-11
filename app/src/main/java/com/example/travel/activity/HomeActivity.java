package com.example.travel.activity;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.travel.R;
import com.example.travel.adapter.ImageAdapter;
import com.example.travel.fragment.HomeFragment;
import com.example.travel.fragment.UserInfoFragment;
import com.example.travel.util.ActivityCollector;
import com.example.travel.util.LoginUser;
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
public class HomeActivity extends BaseActivity {

    private EasyNavigationBar navigationBar;

    private String[] tabText = {"首页","我的"};
    //未选中icon
    private int[] normalIcon = {R.mipmap.index,  R.mipmap.me};
    //选中时icon
    private int[] selectIcon = {R.mipmap.index1, R.mipmap.me1};

    private List<Fragment> fragments = new ArrayList<>();
    private Handler mHandler = new Handler();

    private boolean flag = true;

    @Override
    protected int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        ActivityCollector.finishAll();
        ActivityCollector.addActivity(this);
        navigationBar = findViewById(R.id.home_navigationBar);

        fragments.add(new HomeFragment());
        fragments.add(new UserInfoFragment());

        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .centerImageRes(R.mipmap.add_icon)
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
                                if (LoginUser.getInstance().getUser() == null) {
                                    showToast("请登录");
                                    return;
                                }
                                HashMap<String, String> params = new HashMap<>();
                                params.put("recordId", "");
                                navigateToWithPara(SelectImageActivity.class,params);
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

}