package com.example.travel.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travel.R;
import com.example.travel.activity.EditName;
import com.example.travel.activity.UserInfoActivity;
import com.example.travel.adapter.HomeAdapter;
import com.example.travel.entity.User;
import com.example.travel.util.LoginUser;
import com.example.travel.util.PhotoUtils;
import com.example.travel.widget.TitleLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class UserInfoFragment extends BaseFragment {

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
        titleLayout.getTextView_backward().setImageBitmap(null);
        my_username = mRootView.findViewById(R.id.my_username);
        my_signature = mRootView.findViewById(R.id.my_signature);
        my_portrait = mRootView.findViewById(R.id.my_portrait);
        my_focusNum = mRootView.findViewById(R.id.my_focusNum);
        my_beFocusNum = mRootView.findViewById(R.id.my_beFocusedNum);

        titleLayout.getTextView_forward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivityForResult(intent, 0);
                //navigateTo(UserInfoActivity.class);
            }
        });

        viewPager = mRootView.findViewById(R.id.user_info_fixedViewPager);
        slidingTabLayout = mRootView.findViewById(R.id.user_info_slideTabLayout);
        mTitles = new String[2];
        mTitles[0] = "我的";
        mTitles[1] = "足迹";
        mFragments.add(MyTravelRecordFragment.newInstance());
        mFragments.add(MapFragment.newInstance());
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new HomeAdapter(getFragmentManager(), mTitles, mFragments));
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    protected void initData() {
        getUserInfo();
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
                my_portrait.setBackground(getResources().getDrawable(R.mipmap.default_portrait));
            } else {
                //my_portrait.setImageBitmap(PhotoUtils.stringToBitmap(user.getHeadPortraitPath()));
                Picasso.with(getContext())
                        .load(LoginUser.getInstance().getUser().getHeadPortraitPath())
                        .into(my_portrait);
            }
            my_focusNum.setText("123");
            my_beFocusNum.setText("456");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        //Log.e("userInfoBack","here???");
        getUserInfo();
    }
}