package com.example.travel.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.travel.R;
import com.example.travel.adapter.SystemMessageAdapter;
import com.example.travel.adapter.TravelRecordAdapter;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.entity.CommonResponse;
import com.example.travel.entity.SystemMessageEntity;
import com.example.travel.entity.SystemMessageResponse;
import com.example.travel.entity.TravelRecordEntity;
import com.example.travel.listener.OnItemChildClickListener;
import com.example.travel.listener.OnItemLongClickListener;
import com.example.travel.util.ActivityCollector;
import com.example.travel.util.LoginUser;
import com.example.travel.util.TimeUtils;
import com.example.travel.widget.TitleLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageCenterActivity extends BaseActivity implements OnItemChildClickListener, OnItemLongClickListener {

    private TitleLayout titleLayout;
    private RecyclerView recyclerView;
    private ArrayList<SystemMessageEntity> datas = new ArrayList<>();
    private SystemMessageAdapter systemMessageAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int initLayout() {
        return R.layout.activity_message_center;
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.message_center_recyclerView);
        titleLayout = findViewById(R.id.message_center_title);
        titleLayout.setTitle("????????????");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.read_all_icon);
        titleLayout.getTv_myImage().setVisibility(View.VISIBLE);
        titleLayout.getTv_myImage().setImageBitmap(bitmap);

        Drawable drawable = getResources().getDrawable(R.mipmap.delete_all_icon);
        drawable.setBounds(40,0,110,70);
        titleLayout.getTextView_forward().setCompoundDrawables(drawable,null,null,null);
        titleLayout.getTextView_forward().setText("");
    }

    @Override
    protected void initData() {
        titleLayout.getTextView_forward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllMessage();
            }
        });
        titleLayout.getTextView_backward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleLayout.getTv_myImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readAllMessage();
            }
        });
        linearLayoutManager = new LinearLayoutManager(this);
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

        systemMessageAdapter = new SystemMessageAdapter(this, BitmapFactory.decodeResource(getResources(), R.mipmap.un_read_icon));
        systemMessageAdapter.setOnItemChildClickListener(this);
        systemMessageAdapter.setOnLongClickListener(this);
        getSystemMessage();
    }

    public void getSystemMessage() {
        /*
        ArrayList<SystemMessageEntity> smEntities = new ArrayList<>();
        for (int i = 0;i < 10;i++) {
            SystemMessageEntity smEntity = new SystemMessageEntity();
            smEntity.setSystemMessageId(String.valueOf(i));
            smEntity.setMessagerName("????????????");
            smEntity.setMessageMain("aaaaaaaaaaaaaaaabbbbbbbbbbbbb");
            smEntity.setMessageState(i % 2);
            smEntity.setMessageTime("2021-05-14");
            smEntity.setMessagerPortrait("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3639493961,3576864380&fm=26&gp=0.jpg");
            smEntities.add(smEntity);
        }
        datas = smEntities;
        handler.sendEmptyMessage(0);

         */

        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", LoginUser.getInstance().getUser().getId());
        Api.config(ApiConfig.GET_ALL_SYSTEM_MESSAGE, params).getRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                //Log.e("getAllMessage", res);
                SystemMessageResponse smRes = new Gson().fromJson(res, SystemMessageResponse.class);
                if (smRes.getCode() == 200) {
                    datas = smRes.getData();
                    handler.sendEmptyMessage(0);
                } else {
                    showToastSync("????????????????????????");
                }
            }

            @Override
            public void onFailure(Exception e) {
                showToastSync("???????????????????????????????????????");
            }
        });
    }

    public void deleteAllMessage() {
        //Log.e("message","delete All");
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", LoginUser.getInstance().getUser().getId());
        Api.config(ApiConfig.DELETE_ALL_SYSTEM_MESSAGE, params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                CommonResponse commonResponse = new Gson().fromJson(res, CommonResponse.class);
                if (commonResponse.getCode() == 200) {
                    datas.clear();
                    handler.sendEmptyMessage(0);
                    showToastSync("??????????????????");
                } else {
                    showToastSync("??????????????????");
                }
            }

            @Override
            public void onFailure(Exception e) {
                showToastSync("?????????????????????????????????");
            }
        });
    }

    private void readAllMessage()  {
        if (TimeUtils.isFastDoubleClick()) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", LoginUser.getInstance().getUser().getId());
        Api.config(ApiConfig.READ_ALL_SYSTEM_MESSAGE, params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                CommonResponse commonResponse = new Gson().fromJson(res, CommonResponse.class);
                if (commonResponse.getCode() == 200) {
                    //Log.e("readAllMessage",res);
                    for (int i = 0;i < datas.size();i++) {
                        datas.get(i).setMessageState(1);
                    }
                    handler.sendEmptyMessage(0);
                    showToastSync("????????????????????????");
                } else {
                    showToastSync("????????????????????????");
                }
            }

            @Override
            public void onFailure(Exception e) {
                showToastSync("???????????????????????????????????????");
            }
        });
    }

    @Override
    public void onItemChildClick(int position) {
        // ????????????
        //Log.e("messageClick",String.valueOf(position));
        if (datas.get(position).getMessageState() == 1) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", LoginUser.getInstance().getUser().getId());
        params.put("systemMessageId", datas.get(position).getSystemMessageId());
        Api.config(ApiConfig.READ_SYSTEM_MESSAGE, params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                CommonResponse commonResponse = new Gson().fromJson(res, CommonResponse.class);
                if (commonResponse.getCode() == 200) {
                    datas.get(position).setMessageState(1);
                    handler.sendEmptyMessage(0);
                    showToastSync("????????????");
                } else {
                    showToastSync("????????????");
                }
            }

            @Override
            public void onFailure(Exception e) {
                showToastSync("???????????????????????????");
            }
        });
    }

    @Override
    public void onItemLongClick(int position) {
        //?????? ??????
        //Log.e("messageClick", "delete this");
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", LoginUser.getInstance().getUser().getId());
        params.put("systemMessageId", datas.get(position).getSystemMessageId());
        Api.config(ApiConfig.DELETE_SINGLE_SYSTEM_MESSAGE, params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                CommonResponse commonResponse = new Gson().fromJson(res, CommonResponse.class);
                if (commonResponse.getCode() == 200) {
                    datas.remove(position);
                    handler.sendEmptyMessage(0);
                    showToastSync("????????????");
                } else {
                    showToastSync("????????????");
                }
            }

            @Override
            public void onFailure(Exception e) {
                showToastSync("???????????????????????????");
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
                    systemMessageAdapter.setDatas(datas);
                    recyclerView.setAdapter(systemMessageAdapter);
                    systemMessageAdapter.notifyDataSetChanged();
                    break;
                }
                default:{

                }
            }
        }
    };

}