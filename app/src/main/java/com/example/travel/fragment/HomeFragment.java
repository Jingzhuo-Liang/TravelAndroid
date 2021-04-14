package com.example.travel.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.travel.R;
import com.example.travel.adapter.HomeAdapter;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.util.StringUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class HomeFragment extends BaseFragment {


    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private  String[] mTitles;

    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }


    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        viewPager = mRootView.findViewById(R.id.fixedViewPager);
        slidingTabLayout = mRootView.findViewById(R.id.slideTabLayout);
    }

    @Override
    protected void initData() {
        mTitles = new String[2];
        mTitles[0] = "关注";
        mTitles[1] = "推荐";
        mFragments.add(TravelNoteFragment.newInstance(0));
        mFragments.add(TravelNoteFragment.newInstance(1));
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new HomeAdapter(getFragmentManager(), mTitles, mFragments));
        slidingTabLayout.setViewPager(viewPager);
    }

}