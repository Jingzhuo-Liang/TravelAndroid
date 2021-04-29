package com.example.travel.activity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travel.R;
import com.example.travel.adapter.HomeAdapter;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.entity.OtherUserInfoEntity;
import com.example.travel.entity.OtherUserInfoResponse;
import com.example.travel.entity.RecordDetailResponse;
import com.example.travel.fragment.MapFragment;
import com.example.travel.fragment.MyTravelRecordFragment;
import com.example.travel.widget.TitleLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class OtherUserInfoActivity extends BaseActivity{

    private String otherUserId;

    private TitleLayout titleLayout;
    private TextView other_username;
    private TextView other_signature;
    private ImageView other_portrait;
    private TextView other_focusNum;
    private TextView other_beFocusNum;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    private OtherUserInfoEntity otherInfo;

    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:{
                    //在主线程中执行
                    updateOtherUserInfo();
                    break;
                }
                default:{

                }
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_other_user_info;
    }

    @Override
    protected void initView() {
        Bundle bundle = this.getIntent().getExtras();
        this.otherUserId = bundle.getString("otherUserId");

        titleLayout = findViewById(R.id.other_info_title);
        titleLayout.setTitle("主页");
        titleLayout.getTextView_forward().setText("");
        other_username = findViewById(R.id.other_info_username);
        other_signature = findViewById(R.id.other_info_signature);
        other_portrait = findViewById(R.id.other_info_portrait);
        other_focusNum = findViewById(R.id.other_info_focusNum);
        other_beFocusNum = findViewById(R.id.other_info_beFocusedNum);

        titleLayout.getTextView_backward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager = findViewById(R.id.other_info_fixedViewPager);
        slidingTabLayout = findViewById(R.id.other_info_slideTabLayout);
        mTitles = new String[2];
        mTitles[0] = "游记";
        mTitles[1] = "足迹";
        mFragments.add(MyTravelRecordFragment.newInstance(otherUserId));
        mFragments.add(MapFragment.newInstance(otherUserId));
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new HomeAdapter(getSupportFragmentManager(), mTitles, mFragments));
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    protected void initData() {
        //获取其他用户信息
        HashMap<String , Object> params = new HashMap<>();
        params.put("otherUserId",otherUserId);
        Api.config(ApiConfig.GET_OTHER_USER_INFO, params).getRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                //Log.e("getOtherUserInfo",res);
                Gson gson = new Gson();
                OtherUserInfoResponse otherUserRes = gson.fromJson(res, OtherUserInfoResponse.class);
                if (otherUserRes.getCode() == 200) {
                    otherInfo = otherUserRes.getData();
                    handler.sendEmptyMessage(0);
                } else {
                    showToast(otherUserRes.getMsg());
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void updateOtherUserInfo() {
        other_username.setText(otherInfo.getOtherUserName());
        other_signature.setText(otherInfo.getOtherUserSignature());
        Picasso.with(this)
                .load(otherInfo.getOtherUserPortrait())
                .into(other_portrait);
        other_focusNum.setText(otherInfo.getFocusNum());
        other_beFocusNum.setText(otherInfo.getBeFocusedNum());
    }
}