package com.example.travel.fragment;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.travel.R;
import com.example.travel.adapter.TravelNoteAdapter;
import com.example.travel.entity.TravelNoteEntity;
import com.example.travel.listener.OnItemChildClickListener;

import java.util.ArrayList;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class TravelNoteFragment extends BaseFragment implements OnItemChildClickListener {

    private int categoryId;
    private RecyclerView recyclerView;
    private ArrayList<TravelNoteEntity> datas = new ArrayList<>();
    private TravelNoteAdapter noteAdapter;
    private LinearLayoutManager linearLayoutManager;

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

    public TravelNoteFragment() {
        // Required empty public constructor
    }

    public static TravelNoteFragment newInstance(int categoryId) {
        TravelNoteFragment fragment = new TravelNoteFragment();
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

        //videoAdapter = new VideoAdapter(getActivity());
        //videoAdapter.setDatas(datas);
        noteAdapter = new TravelNoteAdapter(getActivity());
        noteAdapter.setOnItemChildClickListener(this);
        getTravelNoteList();
        //SystemClock.sleep(1000);
        recyclerView.setAdapter(noteAdapter);
        //Log.e("here????",String.valueOf(datas.size()));
    }

    private void getTravelNoteList() {
        for (int i = 0; i< 8;i++) {
            TravelNoteEntity te = new TravelNoteEntity();
            te.setId(String.valueOf(i));
            te.setCoverImage("");
            te.setNoteName("travel");
            te.setPortrait("");
            te.setLikeNum(i * 100 + i + 50);
            te.setRegion("吉林长春");
            te.setUsername("海绵宝宝");
            datas.add(te);
        }
        noteAdapter.setDatas(datas);
        handler.sendEmptyMessage(0);
    }

    private void getTravelNoteList(boolean isRefresh) {

        /*
        String token = getStringFromSp("token");
        //Log.e(token,"token");
        if (!StringUtils.isEmpty(token)) {
            HashMap<String ,Object> params = new HashMap<>();
            params.put("token",token);
            params.put("cid",categoryId);
            params.put("page",pageNum);
            params.put("limit", ApiConfig.PAGE_SIZE);
            Api.config(ApiConfig.VIDEO_LIST_BY_CATEGORY,params).getRequest(new TtitCallback() {
                @Override
                public void onSuccess(String res) {

                    if (isRefresh) {
                        refreshLayout.finishRefresh(true);
                    }
                    else {
                        refreshLayout.finishLoadMore(true);
                    }
                    VideoListResponse videoListResponse = new Gson().fromJson(res, VideoListResponse.class);
                    //Log.e("response",String.valueOf(videoListResponse.getCode()));
                    if (videoListResponse != null && videoListResponse.getCode() == 200 ) {
                        ArrayList<VideoEntity> list = (ArrayList)videoListResponse.getData();
                        if (list != null && list.size() > 0) {
                            if (isRefresh) {
                                datas = list;
                            }
                            else {
                                datas.addAll(list);
                            }

                            videoAdapter.setDatas(datas);
                            handler.sendEmptyMessage(0);
                        }
                        else {
                            if (isRefresh) {
                                showToastSync("暂时加载无数据");
                            }
                            else {
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
        else {
            navigateTo(LoginActivity.class);
        }
         */
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onItemChildClick(int position) {
        //Log.e("TravelNoteFragment-onItemChildClick", String.valueOf(categoryId) + " " + String.valueOf(position));
        //进入游记详情界面
    }
}