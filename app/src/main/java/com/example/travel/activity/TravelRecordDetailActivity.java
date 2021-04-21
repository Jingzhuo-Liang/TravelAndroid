package com.example.travel.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel.R;
import com.example.travel.adapter.RecordDetailImageAdapter;
import com.example.travel.widget.TitleLayout;

import java.util.ArrayList;

public class TravelRecordDetailActivity extends BaseActivity implements View.OnClickListener{

    private TitleLayout titleLayout;
    private ImageView authorPortrait;
    private TextView authorName;
    private TextView authorSignature;
    private TextView releaseTime;
    private TextView releaseRegion;
    private TextView recordName;
    private TextView recordMain;
    private ImageView likeIcon;
    private TextView likeNum;
    private ImageView focusIcon;
    private TextView focusNum;
    private ImageView commitIcon;
    private TextView commitNum;

    private RecyclerView recordDetailImageRecycleView;
    private RecordDetailImageAdapter recordDetailAdapter;

    private Drawable unLikeDrawable;
    private Drawable likeDrawable;
    private Drawable focusDrawable;
    private Drawable unFocusDrawable;
    private Drawable commitDrawable;
    private boolean isFocus = false;
    private boolean isLike = false;

    @Override
    protected int initLayout() {
        return R.layout.activity_travel_record_detail;
    }

    @Override
    protected void initView() {
        titleLayout = findViewById(R.id.record_detail_title);
        authorPortrait = findViewById(R.id.record_detail_authorPortrait);
        authorName = findViewById(R.id.record_detail_authorName);
        authorSignature = findViewById(R.id.record_detail_authorSignature);
        releaseTime = findViewById(R.id.record_detail_releasedTime);
        releaseRegion = findViewById(R.id.record_detail_releasedRegion);
        recordName = findViewById(R.id.record_detail_recordName);
        recordMain = findViewById(R.id.record_detail_recordMain);
        likeIcon = findViewById(R.id.record_detail_likeIcon);
        likeNum = findViewById(R.id.record_detail_likeNum);
        focusIcon = findViewById(R.id.record_detail_focusIcon);
        focusNum = findViewById(R.id.record_detail_focusNum);
        commitIcon = findViewById(R.id.record_detail_commitIcon);
        commitNum = findViewById(R.id.record_detail_commitNum);
        initDrawable();
        recordDetailImageRecycleView = findViewById(R.id.record_detail_image);
        recordDetailImageRecycleView.setLayoutManager(new GridLayoutManager(this, 3));
        recordDetailAdapter = new RecordDetailImageAdapter(this);

        commitIcon.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        initTitleLayout();
        Bundle bundle = this.getIntent().getExtras();
        getTravelRecordDetail(bundle.getString("recordId"));

        likeIcon.setImageDrawable(isLike ? likeDrawable : unLikeDrawable);
        focusIcon.setImageDrawable(isFocus ? focusDrawable : unFocusDrawable);
        commitIcon.setImageDrawable(commitDrawable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_detail_commitIcon: {

                break;
            }
            default:

        }
    }

    private void getTravelRecordDetail(String recordId) {
        ArrayList<String> images = new ArrayList<>();
        images.add("http://114.115.173.237:8000/static/picture/picture_9ad18b83451d4fe38918b81565d424d5_0.png");
        images.add("http://114.115.173.237:8000/static/picture/picture_4d10391eb55a425882211d1952782ce4_0.png");
        images.add("http://114.115.173.237:8000/static/picture/picture_f8978682184642719ea69886f340cd71_0.png");
        images.add("http://114.115.173.237:8000/static/picture/picture_a914aeac119e4e389c69f1dc7567ab2d_0.png");
        images.add("http://114.115.173.237:8000/static/picture/picture_11048a38b7e645838ffab44f23981dd1_0.png");
        images.add("http://114.115.173.237:8000/static/picture/picture_9ad18b83451d4fe38918b81565d424d5_0.png");
        images.add("http://114.115.173.237:8000/static/picture/picture_4d10391eb55a425882211d1952782ce4_0.png");
        images.add("http://114.115.173.237:8000/static/picture/picture_f8978682184642719ea69886f340cd71_0.png");
        images.add("http://114.115.173.237:8000/static/picture/picture_a914aeac119e4e389c69f1dc7567ab2d_0.png");
        recordDetailAdapter.refresh(images);
        recordDetailImageRecycleView.setAdapter(recordDetailAdapter);
    }

    private void initDrawable() {
        likeDrawable = getResources().getDrawable(R.mipmap.like_icon);
        unLikeDrawable = getResources().getDrawable(R.mipmap.unlike_icon);
        focusDrawable = getResources().getDrawable(R.mipmap.focus_icon);
        unFocusDrawable = getResources().getDrawable(R.mipmap.unfocus_icon);
        commitDrawable = getResources().getDrawable(R.mipmap.commit_icon);
    }

    private void initTitleLayout() {
        titleLayout.getTextView_forward().setText("");
        titleLayout.setTitle("游记详情");
        titleLayout.getTextView_backward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}