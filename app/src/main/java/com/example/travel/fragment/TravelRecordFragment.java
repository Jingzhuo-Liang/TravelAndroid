package com.example.travel.fragment;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.example.ljzpopupwindowlibrary.MyPopWindow;
import com.example.ljzpopupwindowlibrary.PopUpWindowItem;
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
import com.example.travel.util.StringUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.siberiadante.customdialoglib.EnsureDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private MyPopWindow popupWindow;
    private List<PopUpWindowItem> mList = new ArrayList<>();

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
        //getActivity()?????????????????????homeActivity??????
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
                //refreshlayout.finishRefresh(2000/*,false*/);//??????false??????????????????
                pageNum = 0;
                getTravelNoteList(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//??????false??????????????????
                pageNum++;
                getTravelNoteList(false);
            }
        });

        noteAdapter = new TravelRecordAdapter(getActivity());
        noteAdapter.setOnItemChildClickListener(this);
        noteAdapter.setOnAdJudgeClickListener(this);
        getTravelNoteList(false);
        recyclerView.setAdapter(noteAdapter);
        initPopUpWindow();
    }

    private void getTravelNoteList(boolean isRefresh) {
        HashMap<String ,Object> params = new HashMap<>();
        params.put("page",pageNum);
        params.put("limit", ApiConfig.PAGE_SIZE);
        if (categoryId == ApiConfig.HOME_FOCUS) { //??????
            if (LoginUser.getInstance().getUser() == null) { //???????????????
                getTravelNoteListGETMETHOD(ApiConfig.GET_TRAVEL_RECORD_NO_FOCUS,params,isRefresh);
            } else {    //????????????
                params.put("userId",LoginUser.getInstance().getUser().getId());
                getTravelNoteListGETMETHOD(ApiConfig.GET_TRAVEL_RECORD_FOCUS,params,isRefresh);
            }
        } else { //??????
            if (!StringUtils.isEmpty(((EditText)Objects.requireNonNull(getActivity()).findViewById(R.id.home_fragment_search_text)).getText().toString())) {
                //?????????????????????????????????->?????????????????????????????????
                ((EditText)Objects.requireNonNull(getActivity()).findViewById(R.id.home_fragment_search_text)).setText("");
                datas.clear();
            }

            if (LoginUser.getInstance().getUser() == null) { //???????????????
                getTravelNoteListGETMETHOD(ApiConfig.GET_TRAVEL_RECORD_NO_RECOMMEND,params,isRefresh);
            } else {    //????????????
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
                    if (list == null) {
                        showToastSync("????????????");
                        return;
                    }
                    if (list.size() > 0) {
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
                        if (datas.size() == 0) {
                            return;
                        }
                        if (isRefresh) {
                            datas = list;
                            noteAdapter.setDatas(datas);
                            handler.sendEmptyMessage(0);
                            showToastSync("?????????????????????");
                        }
                        else{
                            showToastSync("??????????????????");
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
        //????????????????????????  ?????????????????????
        //navigateTo(TravelRecordDetailActivity.class);
        if (datas.get(position).getType() == 0) { //record
            HashMap<String, String> params = new HashMap<>();
            params.put("recordId", String.valueOf(datas.get(position).getRecordId()));
            params.put("authorId", String.valueOf(datas.get(position).getAuthorId()));
            params.put("isApproved", "True");
            params.put("inWhoseHome",String.valueOf(ApiConfig.IN_NO_HOME));
            navigateToWithPara(TravelRecordDetailActivity.class, params);
        } else { //ad
            EnsureDialog ensureDialog = new EnsureDialog(getContext()).builder()
                    .setGravity(Gravity.CENTER)//??????????????????????????????
                    .setTitle("????????????????????????", getResources().getColor(R.color.black))//????????????????????????????????????????????????
                    .setCancelable(false)
                    .setSubTitle("????????????",getResources().getColor(R.color.yellow0));
            ensureDialog.show();
            ensureDialog.setNegativeButton("??????", getResources().getColor(R.color.red0), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ensureDialog.dismiss();
                }
            });
            ensureDialog.setPositiveButton("??????", getResources().getColor(R.color.red0), new View.OnClickListener() {//??????????????????????????????????????????????????????
                @Override
                public void onClick(View view) {
                    ensureDialog.dismiss();
                    clickAdLink(position);
                    navigateToBrowserWithUrl(datas.get(position).getAdUrl());
                }
            });
        }
    }

    public void searchRecord(String keyWord) {
        //Log.e("searchRecord",keyWord);
        if (categoryId != 1 || keyWord == null || keyWord.trim().length() == 0) { //????????????
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
                        showToastSync("?????????????????????");
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

    public void initPopUpWindow() {
        int color = 0;
        PopUpWindowItem item = new PopUpWindowItem(R.mipmap.interest_icon, "?????????",color);
        mList.add(item);
        item = new PopUpWindowItem(R.mipmap.uninterest_icon, "????????????", color);
        mList.add(item);
        popupWindow = new MyPopWindow(getContext(), mList,2);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onAdJudgeClick(View view, int position) {
        showPopupMenu(view, position);
        //popupWindow.showAsDropDown(view, -45, 0);
    }

    private void showPopupMenu(View view, int position) {
        popupWindow.setOnItemClickListener(new MyPopWindow.OnItemClickListener() {
            @Override
            public void OnClick(int i) {
                //Log.e("new PopUpWindow", String.valueOf(i) + " " + String.valueOf(position));
                switch (i) {
                    case 0: {
                        clickAdInterest(position);
                        break;
                    }
                    case 1: {
                        clickAdUnInterest(position);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });
        popupWindow.showAsDropDown(view,-45,0);
    }

    private void clickAdLink(int position) {
        //Log.e("clickAdLink", String.valueOf(position));
        HashMap<String , Object> params = new HashMap<>();
        params.put("userId", LoginUser.getInstance().getUser().getId());
        params.put("adId", datas.get(position).getAdId());
        Api.config(ApiConfig.USER_ACCESS_AD_LINK, params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                //Log.e("clickAdLink", res);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void clickAdInterest(int position) {
        //Log.e("clickAdInterest", String.valueOf(position));
        HashMap<String , Object> params = new HashMap<>();
        params.put("userId", LoginUser.getInstance().getUser().getId());
        params.put("adId", datas.get(position).getAdId());
        Api.config(ApiConfig.USER_INTEREST_AD, params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                //Log.e("clickAdInterest", res);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void clickAdUnInterest(int position) {
        //Log.e("clickAdUnInterest", String.valueOf(position));
        HashMap<String , Object> params = new HashMap<>();
        params.put("userId", LoginUser.getInstance().getUser().getId());
        params.put("adId", datas.get(position).getAdId());
        Api.config(ApiConfig.USER_UNINTEREST_AD, params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                //Log.e("clickAdUnInterest", res);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:{
                    //?????????????????????
                    recyclerView.setAdapter(noteAdapter);
                    noteAdapter.notifyDataSetChanged();
                    break;
                }
                default:{

                }
            }
        }
    };


    private void getTravelNoteList() {
        for (int i = 0; i< 8;i++) {
            TravelRecordEntity te = new TravelRecordEntity();
            te.setRecordId(String.valueOf(i));
            te.setAuthorId(String.valueOf(i));
            te.setRecordCoverImage("");
            te.setRecordName("travel");
            te.setAuthorPortrait("");
            te.setLikeNum(i * 100 + i + 50);
            te.setRecordRegion("????????????");
            te.setAuthorName("????????????");
            datas.add(te);
        }
        noteAdapter.setDatas(datas);
        handler.sendEmptyMessage(0);
    }


    //????????? popUpWindow
    /*
    @SuppressLint("RestrictedApi")
    private void showPopupMenu(View view, int position) {
        // View??????PopupMenu???????????????View?????????
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        // menu??????
        popupMenu.getMenuInflater().inflate(R.menu.menu_advertisement_layout, popupMenu.getMenu());
        // menu???item????????????
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Log.e("menu", item.getTitle().toString());
                switch (item.getItemId()) {
                    case R.id.interest : {
                        clickAdInterest(position);
                        break;
                    }
                    case R.id.unInterest: {
                        clickAdUnInterest(position);
                        break;
                    }
                    default: {
                        break;
                    }
                }
                return false;
            }
        });
        // PopupMenu????????????
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                //Log.e("menu", "you close the menu");
            }
        });
        popupMenu.show();
    }
     */
}