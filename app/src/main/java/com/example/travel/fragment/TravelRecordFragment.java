package com.example.travel.fragment;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.travel.R;
import com.example.travel.activity.TravelRecordDetailActivity;
import com.example.travel.adapter.TravelRecordAdapter;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.entity.TravelRecordEntity;
import com.example.travel.entity.TravelRecordResponse;
import com.example.travel.listener.OnAdJudgeClickListener;
import com.example.travel.listener.OnItemChildClickListener;
import com.example.travel.util.LoginUser;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class TravelRecordFragment extends BaseFragment implements OnItemChildClickListener, OnAdJudgeClickListener {

    private TravelRecordFragment travelRecordFragment;
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
        fragment.travelRecordFragment = fragment;
        fragment.categoryId = categoryId;
        return fragment;
    }

    public TravelRecordFragment getInstance() {
        return travelRecordFragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_travel_record;
    }

    @Override
    protected void initView() {
        //setHasOptionsMenu(true);
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
        noteAdapter.setOnAdJudgeClickListener(this);
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
        if (categoryId == ApiConfig.HOME_FOCUS) { //关注
            if (LoginUser.getInstance().getUser() == null) { //用户未登录
                getTravelNoteListGETMETHOD(ApiConfig.GET_TRAVEL_RECORD_NO_FOCUS,params,isRefresh);
            } else {    //用户登录
                params.put("userId",LoginUser.getInstance().getUser().getId());
                getTravelNoteListGETMETHOD(ApiConfig.GET_TRAVEL_RECORD_FOCUS,params,isRefresh);
            }
        } else { //推荐
            if (isRefresh) { //将搜索栏情况
                ((EditText)Objects.requireNonNull(getActivity()).findViewById(R.id.home_fragment_search_text)).setText("");
            }
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

                TravelRecordResponse tr = new Gson().fromJson(res, TravelRecordResponse.class);
                //Log.e("getTravel",res);
                if (tr != null && tr.getCode() == 200 ) {
                    ArrayList<TravelRecordEntity> list = tr.getData();
                    if (list != null && list.size() > 0) {

                        for (int i = 0;i < list.size();i++) {
                            if (categoryId == ApiConfig.HOMEPAGE_RECOMMEND) {
                                if (i == 2) {
                                    list.get(i).setType(1);
                                    list.get(i).setAdUrl("www.baidu.com");
                                    list.get(i).setRecordName("这是广告!!!!!!!!");
                                } else {
                                    list.get(i).setType(0);
                                }
                            }
                        }

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

    @Override
    public void onItemChildClick(int position) {
        //Log.e("TravelNoteFragment-onItemChildClick", String.valueOf(categoryId) + " " + String.valueOf(position));
        //进入游记详情界面
        //navigateTo(TravelRecordDetailActivity.class);
        HashMap<String, String> params = new HashMap<>();
        params.put("recordId", String.valueOf(datas.get(position).getRecordId()));
        params.put("authorId", String.valueOf(datas.get(position).getAuthorId()));
        navigateToWithPara(TravelRecordDetailActivity.class,params);
    }

    public void searchRecord(String keyWord) {
        //Log.e("searchRecord",keyWord);
        if (categoryId != 1 || keyWord == null || keyWord.trim().length() == 0) { //推荐页面
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId",LoginUser.getInstance().getUser().getId());
        params.put("keyword",keyWord);
        Api.config(ApiConfig.SEARCH_TRAVEL_RECORD,params).getRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                TravelRecordResponse tr = new Gson().fromJson(res, TravelRecordResponse.class);
                //Log.e("search",res);
                if (tr != null && tr.getCode() == 200 ) {
                    ArrayList<TravelRecordEntity> list = tr.getData();
                    if (list != null && list.size() > 0) {
                        datas = list;
                        noteAdapter.setDatas(datas);
                        handler.sendEmptyMessage(0);
                    }
                    else {
                        showToastSync("无相关搜索内容");
                    }
                }else {
                    //navigateTo(LoginActivity.class);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    public void onAdJudgeClick(View view, int position) {
        //Log.e("click an ad", String.valueOf(position));
        showPopupMenu(view);
    }

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_advertisement_layout, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.e("menu", item.getTitle().toString());
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                //Log.e("menu", "you close the menu");
            }
        });
        popupMenu.show();
    }
}