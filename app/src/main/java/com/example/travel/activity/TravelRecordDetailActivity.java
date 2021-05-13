package com.example.travel.activity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.travel.R;
import com.example.travel.adapter.CommentDialogMutiAdapter;
import com.example.travel.adapter.RecordDetailImageAdapter;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.entity.CommonResponse;
import com.example.travel.entity.CommentEntity;
import com.example.travel.entity.CommentMoreEntity;
import com.example.travel.entity.FirstLevelEntity;
import com.example.travel.entity.RecordDetailEntity;
import com.example.travel.entity.RecordDetailResponse;
import com.example.travel.entity.SecondLevelEntity;
import com.example.travel.listener.SoftKeyBoardListener;
import com.example.travel.util.LoginUser;
import com.example.travel.util.RecyclerViewUtil;
import com.example.travel.util.TimeUtils;
import com.example.travel.widget.InputTextMsgDialog;
import com.example.travel.widget.TitleLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TravelRecordDetailActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnLongClickListener{

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
    private TextView commentNum;
    private TextView browseNum;

    private RecyclerView recordDetailImageRecycleView;
    private RecordDetailImageAdapter recordDetailAdapter;

    private Drawable unLikeDrawable;
    private Drawable likeDrawable;
    private Drawable focusDrawable;
    private Drawable unFocusDrawable;
    private Drawable commitDrawable;
    private boolean isFocus = false;
    private boolean isLike = false;

    //dialog
    private List<MultiItemEntity> data = new ArrayList<>();
    private List<FirstLevelEntity> datas = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private InputTextMsgDialog inputTextMsgDialog;
    private float slideOffset = 0;
    private String content = "我听见你的声音，有种特别的感觉。让我不断想，不敢再忘记你。如果真的有一天，爱情理想会实现，我会加倍努力好好对你，永远不改变";
    private CommentDialogMutiAdapter bottomSheetAdapter;
    private RecyclerView rv_dialog_lists;
    //private long totalCount = 22;
    private int offsetY;
    private int positionCount = 0;
    private RecyclerViewUtil mRecyclerViewUtil;
    private SoftKeyBoardListener mKeyBoardListener;
    //dialog

    private int likeTempNum;
    private int focusTempNum;
    private boolean isApproved;//查看自己待审核的游记或者被驳回的游记 false；发布的游记为true

    private String recordId;
    private String authorId;
    private RecordDetailEntity recordDetailEntity = new RecordDetailEntity();
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:{
                    //在主线程中执行
                    setData(recordDetailEntity);
                    browseTravelRecord();
                    break;
                }
                case 1: {
                    // 一级评论
                    bottomSheetAdapter.notifyDataSetChanged();
                    rv_dialog_lists.scrollToPosition(0);
                    break;
                }
                case 2: {
                    // 点赞
                    likeIcon.setImageDrawable(isLike ? likeDrawable : unLikeDrawable);
                    likeNum.setText(String.valueOf(likeTempNum));
                    showToast(isLike ? "点赞成功":"取消点赞");
                    break;
                }
                case 3:  {
                    //关注
                    focusIcon.setImageDrawable(isFocus ? focusDrawable : unFocusDrawable);
                    focusNum.setText(String.valueOf(focusTempNum));
                    showToast(isFocus ? "关注成功":"取消关注");
                    break;
                }
                case 4: {
                    notExist();
                    break;
                }
                default:{

                }
            }
        }
    };

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
        likeTempNum = Integer.parseInt(likeNum.getText().toString());
        focusIcon = findViewById(R.id.record_detail_focusIcon);
        focusNum = findViewById(R.id.record_detail_focusNum);
        focusTempNum = Integer.parseInt(focusNum.getText().toString());
        commitIcon = findViewById(R.id.record_detail_commentIcon);
        commentNum = findViewById(R.id.record_detail_commentNum);
        browseNum = findViewById(R.id.record_detail_browseNum);
        initDrawable();
        recordDetailImageRecycleView = findViewById(R.id.record_detail_image);
        recordDetailImageRecycleView.setLayoutManager(new GridLayoutManager(this, 3));
        recordDetailAdapter = new RecordDetailImageAdapter(this);

        commitIcon.setOnClickListener(this);
        focusIcon.setOnClickListener(this);
        likeIcon.setOnClickListener(this);
        authorPortrait.setOnLongClickListener(this);
        authorPortrait.setOnClickListener(this);

        mRecyclerViewUtil = new RecyclerViewUtil();
        //initDialogData();
        dataSort(0);
        showSheetDialog();
    }

    @Override
    protected void initData() {
        initTitleLayout();
        Bundle bundle = this.getIntent().getExtras();
        this.recordId = bundle.getString("recordId");
        this.authorId = bundle.getString("authorId");
        this.isApproved = Boolean.parseBoolean(bundle.getString("isApproved"));
        getTravelRecordDetail(recordId,authorId);
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.record_detail_authorPortrait: {
                // 进入他人主页
                //Log.e("onLongClick","here");
                HashMap<String , String> params = new HashMap<>();
                params.put("otherUserId",authorId);
                navigateToWithPara(OtherUserInfoActivity.class, params);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_detail_commentIcon: {
                //Log.e("recordDetail","click commit");
                showCommit();
                break;
            }
            case R.id.record_detail_focusIcon :{
                //Log.e("recordDetail","click focus");
                showFocus();
                break;
            }
            case R.id.record_detail_likeIcon: {
                showLike();
                break;
            }
            case R.id.record_detail_authorPortrait: {
                showToast("长按可进入主页");
                break;
            }
            default:
        }
    }

    private void showLike() {
        if (TimeUtils.isFastDoubleClick()) {
            return;
        }
        isLike = !isLike;
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId",LoginUser.getInstance().getUser().getId());
        params.put("recordId",recordId);
        params.put("isLike",isLike ? 1:0);
        Api.config(ApiConfig.LIKE_RECORD,params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                //Log.e("likeClick",res);
                Gson gson = new Gson();
                CommonResponse cr = gson.fromJson(res, CommonResponse.class);
                if (cr.getCode() == 200) {
                    likeTempNum = Integer.parseInt(likeNum.getText().toString());
                    if (isLike) {
                        likeTempNum++;
                    } else {
                        likeTempNum = Math.max(likeTempNum - 1, 0);
                    }
                    handler.sendEmptyMessage(2);
                } else {
                    showToast(cr.getMsg());
                }

            }
            @Override
            public void onFailure(Exception e) {
                showToast("操作失败");
            }
        });
    }

    private void showFocus() {
        if (TimeUtils.isFastDoubleClick()) {
            return;
        }
        isFocus = !isFocus;
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId",LoginUser.getInstance().getUser().getId());
        params.put("authorId",authorId);
        params.put("isFocus",isFocus ? 1:0);
        Api.config(ApiConfig.FOCUS_AUTHOR,params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                Gson gson = new Gson();
                //Log.e("focus",res);
                CommonResponse cr = gson.fromJson(res, CommonResponse.class);
                if (cr.getCode() == 200) {
                    //Log.e("clickFocus",res);
                    focusTempNum = Integer.parseInt(focusNum.getText().toString());
                    if (isFocus) {
                        focusTempNum++;
                    } else  {
                        focusTempNum = Math.max(focusTempNum - 1, 0);
                    }
                    handler.sendEmptyMessage(3);
                } else  {
                    showToastSync(cr.getMsg());
                }
            }

            @Override
            public void onFailure(Exception e) {
                showToast("操作失败");
            }
        });
    }

    private void getTravelRecordDetail(String recordId, String authorId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", LoginUser.getInstance().getUser().getId());
        params.put("recordId",recordId);
        params.put("authorId",authorId);
        Api.config(ApiConfig.GET_RECORD_DETAIL,params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                //Log.e("getDetailSuccess",res);
                Gson gson = new Gson();
                RecordDetailResponse rdr = new RecordDetailResponse();
                rdr = gson.fromJson(res, RecordDetailResponse.class);
                if (rdr.getCode() == 200) {
                    recordDetailEntity = rdr.getData();
                    //Log.e("commentDetail",String.valueOf(recordDetailEntity.getF1LevelComments().get(0).getF1LevelContent()));
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(4);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        /*
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
         */

    }

    private void browseTravelRecord() {
        if (!isApproved) {
            return;
        }
        HashMap<String , Object> params = new HashMap<>();
        params.put("userId",LoginUser.getInstance().getUser().getId());
        params.put("recordId", this.recordId);
        Api.config(ApiConfig.BROWSE_TRAVEL_RECORD, params).postRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                //Log.e("browseRecord",res);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private void setData(RecordDetailEntity rde){
        authorName.setText(rde.getAuthorName());
        authorSignature.setText(rde.getAuthorSignature());
        releaseTime.setText(rde.getRecordReleasedTime().split(" ")[0]);
        releaseRegion.setText(rde.getRecordRegion());
        recordName.setText(rde.getRecordName());
        recordMain.setText(rde.getRecordMain());
        likeNum.setText(String.valueOf(rde.getLikeNum()));
        focusNum.setText(String.valueOf(rde.getFocusNum()));
        commentNum.setText(String.valueOf(rde.getCommentNum()));
        browseNum.setText(String.valueOf(rde.getBrowseNum()));

        isLike = rde.getIsLike() == 1;
        isFocus = rde.getIsFocus() == 1;
        likeIcon.setImageDrawable(isLike ? likeDrawable : unLikeDrawable);
        focusIcon.setImageDrawable(isFocus ? focusDrawable : unFocusDrawable);
        datas = rde.getF1LevelComments();
        dataSort(0);
        bottomSheetAdapter.setNewData(data);

        Picasso.with(this)
                .load(rde.getAuthorPortrait())
                .into(authorPortrait);
        recordDetailAdapter.refresh(rde.getRecordImages());
        recordDetailImageRecycleView.setAdapter(recordDetailAdapter);
    }

    private void initRefresh() {
        datas.clear();
        //initDialogData();
        dataSort(0);
        bottomSheetAdapter.setNewData(data);
    }

    private void notExist() {
        showToast("获取游记失败");
        finish();
    }

    //原始数据 一般是从服务器接口请求过来的
    private void initDialogData() {
        int size = 10;
        for (int i = 0; i < size; i++) {
            FirstLevelEntity firstLevelEntity = new FirstLevelEntity();
            firstLevelEntity.setF1LevelContent("第" + (i + 1) + "人评论内容" + (i % 3 == 0 ? content + (i + 1) + "次" : ""));
            firstLevelEntity.setF1LevelCreateTime(System.currentTimeMillis());
            firstLevelEntity.setF1LevelMessengerPortrait("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3370302115,85956606&fm=26&gp=0.jpg");
            firstLevelEntity.setF1LevelMessengerId(i + "");
            //firstLevelEntity.setIsLike(0);
            //firstLevelEntity.setLikeCount(i);
            firstLevelEntity.setF1LevelMessengerName("星梦缘" + (i + 1));
            //firstLevelEntity.setTotalCount(i + size);

            List<SecondLevelEntity> beans = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                SecondLevelEntity secondLevelEntity = new SecondLevelEntity();
                secondLevelEntity.setS2LevelContent("一级第" + (i + 1) + "人 二级第" + (j + 1) + "人评论内容" + (j % 3 == 0 ? content + (j + 1) + "次" : ""));
                secondLevelEntity.setS2LevelCreateTime(System.currentTimeMillis());
                secondLevelEntity.setS2LevelReplierPortrait("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
                secondLevelEntity.setS2LevelCommentId(j + "");
                //secondLevelEntity.setIsLike(0);
                //secondLevelEntity.setLikeCount(j);
                secondLevelEntity.setS2LevelReplierName("星梦缘" + (i + 1) + "  " + (j + 1));
                //secondLevelBean.setIsReply(j % 5 == 0 ? 1 : 0);
                //secondLevelEntity.setReplyUserName("");
                //secondLevelEntity.setS2LevelTotalCount(firstLevelEntity.getF1LevelTotalCount());
                beans.add(secondLevelEntity);
                firstLevelEntity.setS2LevelComments(beans);
            }
            datas.add(firstLevelEntity);
        }
    }

    private void dataSort(int position) {
        if (datas.isEmpty()) {
            data.add(new MultiItemEntity() {
                @Override
                public int getItemType() {
                    return CommentEntity.TYPE_COMMENT_EMPTY;
                }
            });
            return;
        }

        if (position <= 0) data.clear();
        int posCount = data.size();
        int count = datas.size();
        for (int i = 0; i < count; i++) {
            if (i < position) continue;
            //一级评论
            FirstLevelEntity firstLevelEntity = datas.get(i);
            if (firstLevelEntity == null) continue;
            firstLevelEntity.setF1LevelPosition(i);
            posCount += 2;
            List<SecondLevelEntity> secondLevelEntities = firstLevelEntity.getS2LevelComments();
            if (secondLevelEntities == null || secondLevelEntities.isEmpty()) {
                firstLevelEntity.setF1LevelPositionCount(posCount);
                data.add(firstLevelEntity);
                continue;
            }
            int beanSize = secondLevelEntities.size();
            posCount += beanSize;
            firstLevelEntity.setF1LevelPositionCount(posCount);
            data.add(firstLevelEntity);

            //二级评论
            for (int j = 0; j < beanSize; j++) {
                SecondLevelEntity secondLevelEntity = secondLevelEntities.get(j);
                secondLevelEntity.setS2LevelChildPosition(j);
                secondLevelEntity.setS2LevelPosition(i);
                secondLevelEntity.setS2LevelPositionCount(posCount);
                data.add(secondLevelEntity);
            }

            //展示更多的item
            if (beanSize <= 18) {
                CommentMoreEntity moreBean = new CommentMoreEntity();
                moreBean.setPosition(i);
                moreBean.setPositionCount(posCount);
                //moreBean.setTotalCount(firstLevelEntity.getF1LevelTotalCount());
                data.add(moreBean);
            }

        }
    }

    public void showCommit() {
        bottomSheetAdapter.notifyDataSetChanged();
        slideOffset = 0;
        bottomSheetDialog.show();
    }


    private void showSheetDialog() {
        if (bottomSheetDialog != null) {
            return;
        }

        //view
        View view = View.inflate(this, R.layout.dialog_bottomsheet, null);
        ImageView iv_dialog_close = (ImageView) view.findViewById(R.id.dialog_bottomsheet_iv_close);
        rv_dialog_lists = (RecyclerView) view.findViewById(R.id.dialog_bottomsheet_rv_lists);
        RelativeLayout rl_comment = view.findViewById(R.id.rl_comment);
        iv_dialog_close.setOnClickListener(v -> bottomSheetDialog.dismiss());
        rl_comment.setOnClickListener(v -> {
            //添加二级评论
            initInputTextMsgDialog(null, false, null, -1);
        });

        //adapter
        bottomSheetAdapter = new CommentDialogMutiAdapter(data);
        rv_dialog_lists.setHasFixedSize(true);
        rv_dialog_lists.setLayoutManager(new LinearLayoutManager(this));
        closeDefaultAnimator(rv_dialog_lists);
        bottomSheetAdapter.setOnLoadMoreListener(this, rv_dialog_lists);
        rv_dialog_lists.setAdapter(bottomSheetAdapter);

        //dialog
        bottomSheetDialog = new BottomSheetDialog(this, R.style.dialog);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        mDialogBehavior.setPeekHeight(getWindowHeight());
        //dialog滑动监听
        mDialogBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    if (slideOffset <= -0.28) {
                        //当向下滑动时 值为负数
                        bottomSheetDialog.dismiss();
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                TravelRecordDetailActivity.this.slideOffset = slideOffset;//记录滑动值
            }
        });

        initListener();
    }

    private void initListener() {
        // 点击事件
        bottomSheetAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view1, int position) {
                switch ((int) view1.getTag()) {
                    case CommentEntity.TYPE_COMMENT_PARENT:
                        if (view1.getId() == R.id.rl_group) {
                            //添加二级评论
                            TravelRecordDetailActivity.this.initInputTextMsgDialog((View) view1.getParent(), false, bottomSheetAdapter.getData().get(position), position);
                        } else if (view1.getId() == R.id.ll_like) {
                            //一级评论点赞 项目中还得通知服务器 成功才可以修改
                            FirstLevelEntity bean = (FirstLevelEntity) bottomSheetAdapter.getData().get(position);
                            //bean.setLikeCount(bean.getLikeCount() + (bean.getIsLike() == 0 ? 1 : -1));
                            //bean.setIsLike(bean.getIsLike() == 0 ? 1 : 0);
                            datas.set(bean.getF1LevelPosition(), bean);
                            TravelRecordDetailActivity.this.dataSort(0);
                            bottomSheetAdapter.notifyDataSetChanged();
                        }
                        break;
                    /* 关闭回复二级评论
                    case CommentEntity.TYPE_COMMENT_CHILD:

                        if (view1.getId() == R.id.rl_group) {
                            //添加二级评论（回复）
                            TravelRecordDetailActivity.this.initInputTextMsgDialog(view1, true, bottomSheetAdapter.getData().get(position), position);
                        } else if (view1.getId() == R.id.ll_like) {
                            //二级评论点赞 项目中还得通知服务器 成功才可以修改
                            SecondLevelBean bean = (SecondLevelBean) bottomSheetAdapter.getData().get(position);
                            bean.setLikeCount(bean.getLikeCount() + (bean.getIsLike() == 0 ? 1 : -1));
                            bean.setIsLike(bean.getIsLike() == 0 ? 1 : 0);

                            List<SecondLevelBean> secondLevelBeans = datas.get((int) bean.getPosition()).getSecondLevelBeans();
                            secondLevelBeans.set(bean.getChildPosition(), bean);
//                            CommentMultiActivity.this.dataSort(0);
                            bottomSheetAdapter.notifyDataSetChanged();
                        }
                        break;

                     */
                    case CommentEntity.TYPE_COMMENT_MORE:
                        /*
                        //在项目中是从服务器获取数据，其实就是二级评论分页获取
                        CommentMoreEntity moreBean = (CommentMoreEntity) bottomSheetAdapter.getData().get(position);
                        SecondLevelEntity secondLevelEntity = new SecondLevelEntity();
                        secondLevelEntity.setS2LevelContent("more comment" + 1);
                        secondLevelEntity.setS2LevelCreateTime(System.currentTimeMillis());
                        secondLevelEntity.setS2LevelReplierPortrait("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
                        secondLevelEntity.setS2LevelCommentId(1 + "");
                        //secondLevelEntity.setIsLike(0);
                        //secondLevelEntity.setLikeCount(0);
                        secondLevelEntity.setS2LevelReplierName("星梦缘" + 1);
                        //secondLevelEntity.setIsReply(0);
                        //secondLevelEntity.setReplyUserName("闭嘴家族" + 1);
                        //secondLevelEntity.setS2LevelTotalCount(moreBean.getTotalCount() + 1);

                        datas.get((int) moreBean.getPosition()).getS2LevelComments().add(secondLevelEntity);
                        TravelRecordDetailActivity.this.dataSort(0);
                        bottomSheetAdapter.notifyDataSetChanged();
                        */
                        break;
                    case CommentEntity.TYPE_COMMENT_EMPTY:
                        TravelRecordDetailActivity.this.initRefresh();
                        break;

                }

            }
        });
        //滚动事件
        if (mRecyclerViewUtil != null) mRecyclerViewUtil.initScrollListener(rv_dialog_lists);

        mKeyBoardListener = new SoftKeyBoardListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                dismissInputDialog();
            }
        });
    }

    private void initInputTextMsgDialog(View view, final boolean isReply, final MultiItemEntity item, final int position) {
        dismissInputDialog();
        if (view != null) {
            offsetY = view.getTop();
            scrollLocation(offsetY);
        }
        if (inputTextMsgDialog == null) {
            inputTextMsgDialog = new InputTextMsgDialog(this, R.style.dialog);
            inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                @Override
                public void onTextSend(String msg) {
                    addComment(isReply, item, position, msg);
                }

                @Override
                public void dismiss() {
                    //item滑动到原位
                    scrollLocation(-offsetY);
                }
            });
        }
        showInputTextMsgDialog();
    }

    //添加评论
    private void addComment(boolean isReply, MultiItemEntity item, final int position, String msg) {
        if (position >= 0) {
            //添加二级评论
            FirstLevelEntity firstLevelEntity = (FirstLevelEntity) item;
            HashMap<String, Object> params = new HashMap<>();
            params.put("recordId",recordId);
            params.put("userId",LoginUser.getInstance().getUser().getId());
            params.put("content",msg);
            params.put("createTime",System.currentTimeMillis());
            params.put("firstLevelCommentId",firstLevelEntity.getF1LevelCommentId());
            positionCount = (firstLevelEntity.getF1LevelPositionCount() + 1);
            int pos = firstLevelEntity.getF1LevelPosition();
            //Log.e("addS2CommentBegin","1111");
            Api.config(ApiConfig.ADD_SECOND_LEVEL_COMMENT,params).postRequest(new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    //Log.e("addS2Comment",res);
                    Gson gson = new Gson();
                    CommonResponse addComment = gson.fromJson(res, CommonResponse.class);
                    if (addComment.getCode() == 200) {
                        //Log.e("addS2Comment",res);
                        SecondLevelEntity secondLevelEntity = new SecondLevelEntity();
                        secondLevelEntity.setS2LevelContent(msg);
                        secondLevelEntity.setS2LevelReplierPortrait(LoginUser.getInstance().getUser().getHeadPortraitPath());
                        secondLevelEntity.setS2LevelCreateTime(System.currentTimeMillis());
                        secondLevelEntity.setS2LevelReplierName(LoginUser.getInstance().getUser().getUsername());
                        secondLevelEntity.setS2LevelReplierId(addComment.getData());
                        secondLevelEntity.setS2LevelPosition(positionCount);
                        datas.get(pos).getS2LevelComments().add(secondLevelEntity);
                        TravelRecordDetailActivity.this.dataSort(0);
                        rv_dialog_lists.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((LinearLayoutManager) rv_dialog_lists.getLayoutManager())
                                        .scrollToPositionWithOffset(positionCount >= data.size() - 1 ? data.size() - 1
                                                : positionCount, positionCount >= data.size() - 1 ? Integer.MIN_VALUE : rv_dialog_lists.getHeight());
                            }
                        }, 100);
                        handler.sendEmptyMessage(1);
                    } else {

                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        } else {
            //添加一级评论
            //Log.e("firstLevelComment",recordId + " " + LoginUser.getInstance().getUser().getId());
            HashMap<String, Object> params = new HashMap<>();
            params.put("recordId",recordId);
            params.put("userId",LoginUser.getInstance().getUser().getId());
            params.put("content",msg);
            params.put("createTime",System.currentTimeMillis());
            Api.config(ApiConfig.ADD_FIRST_LEVEL_COMMENT,params).postRequest(new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    //Log.e("addF1Comment",res);
                    Gson gson = new Gson();
                    CommonResponse addF1Comment = gson.fromJson(res, CommonResponse.class);
                    if (addF1Comment.getCode() == 200) {
                        FirstLevelEntity firstLevelEntity = new FirstLevelEntity();
                        firstLevelEntity.setF1LevelMessengerName(LoginUser.getInstance().getUser().getUsername());
                        firstLevelEntity.setF1LevelCommentId(addF1Comment.getData());
                        //Log.e("userPortrait",LoginUser.getInstance().getUser().getHeadPortraitPath());
                        firstLevelEntity.setF1LevelMessengerPortrait(LoginUser.getInstance().getUser().getHeadPortraitPath());
                        firstLevelEntity.setF1LevelCreateTime(System.currentTimeMillis());
                        //Log.e("addF1Comment",String.valueOf(System.currentTimeMillis()));
                        firstLevelEntity.setF1LevelContent(msg);
                        //firstLevelEntity.setLikeCount(0);
                        firstLevelEntity.setS2LevelComments(new ArrayList<SecondLevelEntity>());
                        datas.add(0, firstLevelEntity);
                        dataSort(0);
                        handler.sendEmptyMessage(1);
                    } else {

                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }
    }

    private void dismissInputDialog() {
        if (inputTextMsgDialog != null) {
            if (inputTextMsgDialog.isShowing()) inputTextMsgDialog.dismiss();
            inputTextMsgDialog.cancel();
            inputTextMsgDialog = null;
        }
    }

    private void showInputTextMsgDialog() {
        inputTextMsgDialog.show();
    }

    private int getWindowHeight() {
        Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    @Override
    public void onLoadMoreRequested() {
        bottomSheetAdapter.loadMoreEnd(false);
        // 加载更多评论
        /*
        if (datas.size() >= totalCount) {
            bottomSheetAdapter.loadMoreEnd(false);
            return;
        }
        FirstLevelEntity firstLevelEntity = new FirstLevelEntity();
        firstLevelEntity.setUserName("hui");
        firstLevelEntity.setId((datas.size() + 1) + "");
        firstLevelEntity.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
        firstLevelEntity.setCreateTime(System.currentTimeMillis());
        firstLevelEntity.setContent("add loadmore comment");
        firstLevelEntity.setLikeCount(0);
        firstLevelEntity.setSecondLevelEntities(new ArrayList<SecondLevelEntity>());
        datas.add(firstLevelEntity);
        dataSort(datas.size() - 1);
        bottomSheetAdapter.notifyDataSetChanged();
        bottomSheetAdapter.loadMoreComplete();
        */
    }

    // item滑动
    public void scrollLocation(int offsetY) {
        try {
            rv_dialog_lists.smoothScrollBy(0, offsetY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭默认局部刷新动画
     */
    public void closeDefaultAnimator(RecyclerView mRvCustomer) {
        if (null == mRvCustomer) return;
        mRvCustomer.getItemAnimator().setAddDuration(0);
        mRvCustomer.getItemAnimator().setChangeDuration(0);
        mRvCustomer.getItemAnimator().setMoveDuration(0);
        mRvCustomer.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) mRvCustomer.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    protected void onDestroy() {
        if (mRecyclerViewUtil != null){
            mRecyclerViewUtil.destroy();
            mRecyclerViewUtil = null;
        }
        if (mKeyBoardListener != null) {
            mKeyBoardListener.setOnSoftKeyBoardChangeListener(null);
            mKeyBoardListener = null;
        }
        bottomSheetAdapter = null;
        super.onDestroy();
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