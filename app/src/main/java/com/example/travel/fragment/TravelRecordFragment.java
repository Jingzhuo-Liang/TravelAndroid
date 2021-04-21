package com.example.travel.fragment;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.travel.R;
import com.example.travel.activity.TravelRecordDetailActivity;
import com.example.travel.adapter.TravelRecordAdapter;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.entity.TravelRecordEntity;
import com.example.travel.entity.TravelRecordResponse;
import com.example.travel.listener.OnItemChildClickListener;
import com.example.travel.util.LoginUser;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class TravelRecordFragment extends BaseFragment implements OnItemChildClickListener {

    private int categoryId;
    private RecyclerView recyclerView;
    private ArrayList<TravelRecordEntity> datas = new ArrayList<>();
    private TravelRecordAdapter noteAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RefreshLayout refreshLayout;
    private int pageNum = 0;


    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:{
                    //在主线程中执行
                    recyclerView.setAdapter(noteAdapter);
                    noteAdapter.notifyDataSetChanged();
                    break;
                }
                default:{

                }
            }
        }
    };

    public TravelRecordFragment() {
        // Required empty public constructor
    }

    public static TravelRecordFragment newInstance(int categoryId) {
        TravelRecordFragment fragment = new TravelRecordFragment();
        fragment.categoryId = categoryId;
        return fragment;
    }


    @Override
    protected int initLayout() {
        return R.layout.fragment_travel_record;
    }

    @Override
    protected void initView() {
        recyclerView = mRootView.findViewById(R.id.recyclerView);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        //getActivity()可以得到父组件homeActivity组件
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                //Log.e("tNoteFragment initData1",view.toString());
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                //Log.e("tNoteFragment initData2",view.toString());
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                pageNum = 0;
                getTravelNoteList(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageNum++;
                getTravelNoteList(false);
            }
        });

        noteAdapter = new TravelRecordAdapter(getActivity());
        noteAdapter.setOnItemChildClickListener(this);
        getTravelNoteList(true);
        recyclerView.setAdapter(noteAdapter);
    }

    private void getTravelNoteList() {
        for (int i = 0; i< 8;i++) {
            TravelRecordEntity te = new TravelRecordEntity();
            te.setRecordId(String.valueOf(i));
            te.setAuthorId(String.valueOf(i));
            te.setRecordCoverImage("");
            te.setRecordName("travel");
            te.setAuthorPortrait("");
            te.setLikeNum(i * 100 + i + 50);
            te.setRecordRegion("吉林长春");
            te.setAuthorName("海绵宝宝");
            datas.add(te);
        }
        noteAdapter.setDatas(datas);
        handler.sendEmptyMessage(0);
    }

    private void getTravelNoteList(boolean isRefresh) {
        HashMap<String ,Object> params = new HashMap<>();
        params.put("page",pageNum);
        params.put("limit", ApiConfig.PAGE_SIZE);
        if (categoryId == 0) { //关注
            if (LoginUser.getInstance().getUser() == null) { //用户未登录
                getTravelNoteListGETMETHOD(ApiConfig.GET_TRAVEL_RECORD_NO_FOCUS,params,isRefresh);
            } else {    //用户登录
                params.put("userId",LoginUser.getInstance().getUser().getId());
                getTravelNoteListGETMETHOD(ApiConfig.GET_TRAVEL_RECORD_FOCUS,params,isRefresh);
            }
        } else { //推荐
            if (LoginUser.getInstance().getUser() == null) { //用户未登录
                getTravelNoteListGETMETHOD(ApiConfig.GET_TRAVEL_RECORD_NO_RECOMMEND,params,isRefresh);
            } else {    //用户登录
                params.put("userId",LoginUser.getInstance().getUser().getId());
                getTravelNoteListGETMETHOD(ApiConfig.GET_TRAVEL_RECORD_RECOMMEND,params,isRefresh);
            }
        }

    }

    private void getTravelNoteListGETMETHOD(String url, HashMap<String,Object> params,boolean isRefresh) {
        Api.config(url,params).getRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true);
                }
                else {
                    refreshLayout.finishLoadMore(true);
                }
                /*
                ArrayList<TravelRecordEntity> list = new ArrayList<>();
                for (int i = (pageNum - 1) * ApiConfig.PAGE_SIZE;i < pageNum * ApiConfig.PAGE_SIZE && i < 15;i++) {
                    TravelRecordEntity te = new TravelRecordEntity();
                    te.setId(String.valueOf(i));
                    te.setCoverImage("");
                    te.setNoteName("travel");
                    te.setPortrait("");
                    te.setLikeNum(i * 100 + i + 50);
                    te.setRegion("吉林长春");
                    te.setUsername("海绵宝宝");
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
                        showToastSync("暂时加载无数据");
                    }
                    else {
                        showToastSync("没有更多数据");
                    }
                }
                noteAdapter.setDatas(datas);
                handler.sendEmptyMessage(0);
                 */

                TravelRecordResponse tr = new Gson().fromJson(res, TravelRecordResponse.class);
                Log.e("getTravel",res);
                //Log.e("response",String.valueOf(videoListResponse.getCode()));
                if (tr != null && tr.getCode() == 200 ) {
                    ArrayList<TravelRecordEntity> list = tr.getData();
                    if (list != null && list.size() > 0) {
                        if (isRefresh) {
                            datas = list;
                        }
                        else {
                            datas.addAll(list);
                        }
                        noteAdapter.setDatas(datas);
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
                    refreshLayout.finishRefresh(true);
                }
                else {
                    refreshLayout.finishLoadMore(true);
                }
            }
        });

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onItemChildClick(int position) {
        //Log.e("TravelNoteFragment-onItemChildClick", String.valueOf(categoryId) + " " + String.valueOf(position));
        //进入游记详情界面
        //navigateTo(TravelRecordDetailActivity.class);
        HashMap<String, String> params = new HashMap<>();
        params.put("recordId", String.valueOf(datas.get(position).getRecordId()));
        navigateToWithPara(TravelRecordDetailActivity.class,params);
    }
}