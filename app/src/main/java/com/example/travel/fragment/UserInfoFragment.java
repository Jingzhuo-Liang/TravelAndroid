package com.example.travel.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travel.R;
import com.example.travel.activity.EditName;
import com.example.travel.activity.MessageCenterActivity;
import com.example.travel.activity.UserInfoActivity;
import com.example.travel.adapter.HomeAdapter;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.entity.User;
import com.example.travel.entity.UserRelatedInfoEntity;
import com.example.travel.entity.UserRelatedInfoResponse;
import com.example.travel.util.LoginUser;
import com.example.travel.util.PhotoUtils;
import com.example.travel.widget.TitleLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class UserInfoFragment extends BaseFragment {

    private static UserInfoFragment userInfoFragment = null;

    private TitleLayout titleLayout;
    private TextView my_username;
    private TextView my_signature;
    private ImageView my_portrait;
    private TextView my_focusNum;
    private TextView my_beFocusNum;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private UserRelatedInfoEntity userRelatedInfo;

    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:{
                    //在主线程中执行
                    updateUserRelatedInfo();
                    break;
                }
                default:{

                }
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initView() {
        titleLayout = mRootView.findViewById(R.id.my_title);
        titleLayout.setTitle("我的");
        Drawable drawable = getResources().getDrawable(R.mipmap.user_info_icon);
        drawable.setBounds(40,0,110,70);
        titleLayout.getTextView_forward().setCompoundDrawables(drawable,null,null,null);
        titleLayout.getTextView_forward().setText("");
        titleLayout.getTv_myImage().setVisibility(View.VISIBLE);
        my_username = mRootView.findViewById(R.id.my_username);
        my_signature = mRootView.findViewById(R.id.my_signature);
        my_portrait = mRootView.findViewById(R.id.my_portrait);
        my_focusNum = mRootView.findViewById(R.id.my_focusNum);
        my_beFocusNum = mRootView.findViewById(R.id.my_beFocusedNum);

        titleLayout.getTextView_backward().setVisibility(View.INVISIBLE);
        titleLayout.getTextView_forward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivityForResult(intent, 0);
                //navigateTo(UserInfoActivity.class);
            }
        });
        titleLayout.getTv_myImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(MessageCenterActivity.class);
            }
        });

        viewPager = mRootView.findViewById(R.id.user_info_fixedViewPager);
        slidingTabLayout = mRootView.findViewById(R.id.user_info_slideTabLayout);
        mTitles = new String[2];
        mTitles[0] = "我的";
        mTitles[1] = "足迹";
        mFragments.add(MyTravelRecordFragment.newInstance(LoginUser.getInstance().getUser().getId()));
        mFragments.add(MapFragment.newInstance(LoginUser.getInstance().getUser().getId()));
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new HomeAdapter(getFragmentManager(), mTitles, mFragments));
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    protected void initData() {
        getUserInfo();
        getUserRelatedInfo();
    }

    private void getUserRelatedInfo() {
        HashMap<String , Object> params = new HashMap<>();
        params.put("userId",LoginUser.getInstance().getUser().getId());
        Api.config(ApiConfig.GET_USER_RELATED_INFO,params).getRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                //Log.e("getUserRelatedInfo",res);
                Gson gson = new Gson();
                UserRelatedInfoResponse userRes = gson.fromJson(res, UserRelatedInfoResponse.class);
                if (userRes.getCode() == 200) {
                    userRelatedInfo = userRes.getData();
                    handler.sendEmptyMessage(0);
                } else {
                    showToastSync(userRes.getMsg());
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void updateUserRelatedInfo() {
        my_focusNum.setText(userRelatedInfo.getFocusNum());
        my_beFocusNum.setText(userRelatedInfo.getBeFocusedNum());
    }

    private void getUserInfo() {
        User user = LoginUser.getInstance().getUser();
        if (user == null) {
            my_username.setText("昵称");
            my_signature.setText("个性签名");
            my_portrait.setBackground(getResources().getDrawable(R.mipmap.default_portrait));
            my_focusNum.setText("0");
            my_beFocusNum.setText("0");
        }
        else {
            my_username.setText(user.getUsername());
            my_signature.setText(user.getSignature());
            if (user.getHeadPortraitPath().equals("default") || user.getHeadPortraitPath().equals("")) {
                //Log.e("updatePortraitDefault","1234567");
                my_portrait.setBackground(getResources().getDrawable(R.mipmap.default_portrait));
            } else {
                //my_portrait.setImageBitmap(PhotoUtils.stringToBitmap(user.getHeadPortraitPath()));
                //Log.e("updatePortrait",user.getHeadPortraitPath());
                Picasso.with(getContext())
                        .load(user.getHeadPortraitPath())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(my_portrait);
            }
            //my_focusNum.setText("123");
            //my_beFocusNum.setText("456");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        //Log.e("userInfoBack","here???");
        getUserInfo();
    }

    public static UserInfoFragment newStance () {
        if (userInfoFragment == null) {
            userInfoFragment = new UserInfoFragment();
        }
        return userInfoFragment;
    }

    public static UserInfoFragment getUserInfoFragment() {
        if (userInfoFragment == null) {
            userInfoFragment = new UserInfoFragment();
        }
        return userInfoFragment;
    }

    public UserInfoFragment() {
        userInfoFragment = this;
    }

    public void refreshUserInfo() {
        //Log.e("refresh" , "related UserInfo");
        getUserRelatedInfo();
    }
}