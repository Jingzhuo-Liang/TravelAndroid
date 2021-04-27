package com.example.travel.fragment;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.travel.R;
import com.example.travel.adapter.HomeAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class HomeFragment extends BaseFragment implements View.OnClickListener {


    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private  String[] mTitles;

    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    private ImageView search_btn;
    private EditText search_text;

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
        search_btn = mRootView.findViewById(R.id.home_fragment_search_icon);
        search_text = mRootView.findViewById(R.id.home_fragment_search_text);
        search_btn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mTitles = new String[2];
        mTitles[0] = "关注";
        mTitles[1] = "推荐";
        mFragments.add(TravelRecordFragment.newInstance(0));
        mFragments.add(TravelRecordFragment.newInstance(1));
        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new HomeAdapter(getFragmentManager(), mTitles, mFragments));
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_fragment_search_icon: {
                ((TravelRecordFragment)((HomeAdapter)viewPager.getAdapter()).getItem(1)).searchRecord(search_text.getText().toString());
            }
            default:
                break;
        }
    }
}