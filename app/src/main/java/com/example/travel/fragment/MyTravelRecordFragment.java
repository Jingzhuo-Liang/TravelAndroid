package com.example.travel.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel.R;
import com.example.travel.adapter.MyTravelRecordAdapter;
import com.example.travel.adapter.TravelRecordAdapter;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.entity.MyTravelRecordEntity;
import com.example.travel.entity.TravelRecordEntity;
import com.example.travel.entity.TravelRecordResponse;
import com.example.travel.listener.OnItemChildClickListener;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;


public class MyTravelRecordFragment extends BaseFragment implements OnItemChildClickListener {

    private RecyclerView myRecordRecyclerView;
    private ArrayList<MyTravelRecordEntity> datas = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private RefreshLayout myRecordRefreshLayout;
    private int pageNum = 1;
    private MyTravelRecordAdapter myTravelRecordAdapter;

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

    public static MyTravelRecordFragment newInstance() {
        MyTravelRecordFragment fragment = new MyTravelRecordFragment();
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
                pageNum = 1;
                getMyTravelRecordList(true);
            }
        });
        myRecordRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageNum++;
                getMyTravelRecordList(false);
            }
        });
        myTravelRecordAdapter = new MyTravelRecordAdapter(getActivity());
        myTravelRecordAdapter.setOnItemChildClickListener(this);
        getMyTravelRecordList(true);
        myRecordRecyclerView.setAdapter(myTravelRecordAdapter);
    }

    private void getMyTravelRecordList(boolean isRefresh) {
        //Log.e("getMyRecord",String.valueOf(isRefresh));
        ArrayList<MyTravelRecordEntity> list = new ArrayList<>();
        for (int i = (pageNum - 1) * ApiConfig.PAGE_SIZE; i < pageNum * ApiConfig.PAGE_SIZE && i < 15; i++) {
            MyTravelRecordEntity te = new MyTravelRecordEntity();
            if (i % 2 == 0) {
                te.setRecordState(0);
                te.setLikeNum(i * 100 + i + 50);
                te.setCommitNum(i * 50 + i + 50);
                te.setBrowseNum(i * 30 + i + 50);
            } else {
                te.setRecordState(1);
                te.setLikeNum(0);
                te.setCommitNum(0);
                te.setBrowseNum(0);
            }
            te.setRecordId(String.valueOf(i));
            te.setRecordCoverImage("");
            te.setRecordName("这是我的第" + i + "个游记");
            te.setRecordReleaseTime("2021-0" +i +  "-0" + i);
            te.setRecordRegion("吉林省-长春市");
            list.add(te);
        }
        if (list.size() > 0) {
            if (isRefresh) {
                datas = list;
            } else {
                datas.addAll(list);
            }
        } else {
            if (isRefresh) {
                showToast("暂时加载无数据");
            }
            else {
                showToast("没有更多数据");
            }
        }
        //Log.e("getMyRecord",String.valueOf(datas.size()));
        myTravelRecordAdapter.setDatas(datas);
        handler.sendEmptyMessage(0);
                    /*
            Api.config(url,params).getRequest(new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh(true);
                    }
                    else {
                        refreshLayout.finishLoadMore(true);
                    }
                    TravelRecordResponse tr = new Gson().fromJson(res, TravelRecordResponse.class);
                    Log.e("getTravel",res);
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
                                Log.e("getTravel","no more");
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
        */

    }

    @Override
    public void onItemChildClick(int position) {
        //进入我的游记详情界面
    }
}