package com.example.travel.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel.R;
import com.example.travel.activity.SelectImageActivity;
import com.example.travel.activity.TravelRecordDetailActivity;
import com.example.travel.adapter.MyTravelRecordAdapter;
import com.example.travel.adapter.TravelRecordAdapter;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.entity.CommonResponse;
import com.example.travel.entity.MyTravelRecordEntity;
import com.example.travel.entity.MyTravelRecordResponse;
import com.example.travel.entity.TravelRecordEntity;
import com.example.travel.entity.TravelRecordResponse;
import com.example.travel.listener.OnItemChildClickListener;
import com.example.travel.listener.OnItemDeleteListener;
import com.example.travel.listener.OnItemModifyClickListener;
import com.example.travel.util.LoginUser;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;


public class MyTravelRecordFragment extends BaseFragment implements OnItemChildClickListener, OnItemDeleteListener, OnItemModifyClickListener {

    private RecyclerView myRecordRecyclerView;
    private ArrayList<MyTravelRecordEntity> datas = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private RefreshLayout myRecordRefreshLayout;
    private int pageNum = 0;
    private MyTravelRecordAdapter myTravelRecordAdapter;

    private String userId;

    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:{
                    //在主线程中执行
                    myRecordRecyclerView.setAdapter(myTravelRecordAdapter);
                    myTravelRecordAdapter.notifyDataSetChanged();
                    break;
                }
                default:{

                }
            }
        }
    };

    public MyTravelRecordFragment() {
    }

    public static MyTravelRecordFragment newInstance(String userId) {
        MyTravelRecordFragment fragment = new MyTravelRecordFragment();
        fragment.userId = userId;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_my_travel_record;
    }

    @Override
    protected void initView() {
        myRecordRecyclerView = mRootView.findViewById(R.id.my_record_recyclerView);
        myRecordRefreshLayout = mRootView.findViewById(R.id.my_record_refreshLayout);
    }

    @Override
    protected void initData() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        //getActivity()可以得到父组件homeActivity组件
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myRecordRecyclerView.setLayoutManager(linearLayoutManager);
        myRecordRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                //Log.e("tNoteFragment initData1",view.toString());
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                //Log.e("tNoteFragment initData2",view.toString());
            }
        });
        myRecordRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                pageNum = 0;
                getTravelRecordList(true);
            }
        });
        myRecordRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageNum++;
                getTravelRecordList(false);
            }
        });
        myTravelRecordAdapter = new MyTravelRecordAdapter(getActivity(), userId.equals(LoginUser.getInstance().getUser().getId()));
        myTravelRecordAdapter.setOnItemChildClickListener(this);
        myTravelRecordAdapter.setOnItemDeleteListener(this);
        myTravelRecordAdapter.setOnItemModifyClickListener(this);
        getTravelRecordList(true);
        myRecordRecyclerView.setAdapter(myTravelRecordAdapter);
    }

    private void getTravelRecordList(boolean isRefresh) {
        HashMap<String, Object> params = new HashMap<>();
        if (userId.equals(LoginUser.getInstance().getUser().getId())) { //我的游记
            //Log.e("getMyTravel",userId);
            params.put("userId", userId);
            params.put("page",pageNum);
            params.put("limit",ApiConfig.PAGE_SIZE);
            getMyTravelRecordList(isRefresh, ApiConfig.GET_MY_TRAVEL_RECORD, params);
        } else { //其他用户的游记
            //Log.e("getMyTravel",userId);
            params.put("otherUserId", userId);
            params.put("page",pageNum);
            params.put("limit",ApiConfig.PAGE_SIZE);
            getMyTravelRecordList(isRefresh, ApiConfig.GET_OTHER_USER_TRAVEL_RECORD, params);
        }
    }

    private void getMyTravelRecordList(boolean isRefresh, String url, HashMap<String, Object> params) {
        //Log.e("myTravelId",LoginUser.getInstance().getUser().getId());
        Api.config(url,params).getRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                if (isRefresh) {
                    myRecordRefreshLayout.finishRefresh(true);
                }
                else {
                    myRecordRefreshLayout.finishLoadMore(true);
                }
                //Log.e("getMyTravel",res);
                MyTravelRecordResponse tr = new Gson().fromJson(res, MyTravelRecordResponse.class);
                //Log.e("response",String.valueOf(videoListResponse.getCode()));
                if (tr != null && tr.getCode() == 200 ) {
                    ArrayList<MyTravelRecordEntity> list = tr.getData();
                    if (list != null && list.size() > 0) {
                        if (isRefresh) {
                            datas = list;
                        }
                        else {
                            datas.addAll(list);
                        }
                        myTravelRecordAdapter.setDatas(datas);
                        handler.sendEmptyMessage(0);
                    }
                    else {
                        if (isRefresh) {
                            showToastSync("暂时加载无数据");
                        }
                        else {
                            //Log.e("getMyTravel","no more");
                            showToastSync("没有更多数据");
                        }
                    }
                    //showToastSync(datas.get(0).getVtitle());
                }else {
                    //navigateTo(LoginActivity.class);
                }
                //showToastSync(res);
            }

            @Override
            public void onFailure(Exception e) {
                if (isRefresh) {
                    myRecordRefreshLayout.finishRefresh(true);
                }
                else {
                    myRecordRefreshLayout.finishLoadMore(true);
                }
            }
        });
    }

    @Override
    public void onItemChildClick(int position) {
        //进入我的游记详情界面
        HashMap<String, String> params = new HashMap<>();
        params.put("recordId", datas.get(position).getRecordId());
        navigateToWithPara(TravelRecordDetailActivity.class, params);
    }
    @Override
    public void onItemModifyListener(int position) {
        HashMap<String, String> params = new HashMap<>();
        params.put("recordId", datas.get(position).getRecordId());
        navigateToWithPara(SelectImageActivity.class, params);
    }

    @Override
    public void onItemDeleteListener(int position) {
        //删除我的游记
        HashMap<String , Object> params = new HashMap<>();
        params.put("userId",LoginUser.getInstance().getUser().getId());
        params.put("recordId",datas.get(position).getRecordId());
        Api.config(ApiConfig.DELETE_MY_TRAVEL_RECORD,params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                //Log.e("deleteMyRecord",res);
                Gson gson = new Gson();
                CommonResponse commonResponse = gson.fromJson(res, CommonResponse.class);
                if (commonResponse.getCode() == 200) {
                    datas.remove(position);
                    handler.sendEmptyMessage(0);
                    showToastSync("删除成功");
                } else {
                    showToastSync(commonResponse.getMsg());
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


}