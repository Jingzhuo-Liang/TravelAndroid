package com.example.travel.activity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.icu.text.AlphabeticIndex;
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
import com.example.travel.entity.RecordDetailRelatedInfoEntity;
import com.example.travel.entity.RecordDetailRelatedInfoResponse;
import com.example.travel.entity.RecordDetailResponse;
import com.example.travel.entity.SecondLevelEntity;
import com.example.travel.fragment.UserInfoFragment;
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
    private String content = "???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
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
    private boolean isApproved;//?????????????????????????????????????????????????????? false?????????????????????true
    private int inWhoseHome; //?????????????????? 0?????? 1??????????????? 2???????????????

    private String recordId;
    private String authorId;
    private RecordDetailEntity recordDetailEntity = new RecordDetailEntity();
    private RecordDetailRelatedInfoEntity recordDetailRelatedInfoEntity = new RecordDetailRelatedInfoEntity();
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:{
                    //?????????????????????
                    setRecordDetailData(recordDetailEntity);
                    browseTravelRecord();
                    break;
                }
                case 1: {
                    // ????????????
                    bottomSheetAdapter.notifyDataSetChanged();
                    rv_dialog_lists.scrollToPosition(0);
                    break;
                }
                case 2: {
                    // ??????
                    likeIcon.setImageDrawable(isLike ? likeDrawable : unLikeDrawable);
                    likeNum.setText(String.valueOf(likeTempNum));
                    showToast(isLike ? "????????????":"????????????");
                    break;
                }
                case 3:  {
                    //??????
                    focusIcon.setImageDrawable(isFocus ? focusDrawable : unFocusDrawable);
                    focusNum.setText(String.valueOf(focusTempNum));
                    showToast(isFocus ? "????????????":"????????????");
                    UserInfoFragment.getUserInfoFragment().refreshUserInfo();
                    break;
                }
                case 4: {
                    //???????????????
                    showToast("??????????????????????????????????????????");
                    finish();
                    break;
                }
                case 5: {
                    showToast("????????????");
                    finish();
                    break;
                }
                case 6: {
                    // ????????????????????????
                    setRecordDetailRelatedInfoData(recordDetailRelatedInfoEntity);
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
        this.inWhoseHome = Integer.parseInt(bundle.getString("inWhoseHome"));
        getTravelRecordDetail(recordId,authorId);
        //getTravelRecordDetailRelatedInfo(recordId, authorId);
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.record_detail_authorPortrait: {
                if (inWhoseHome == ApiConfig.IN_OTHER_HOME) {
                    showToast("????????????????????????");
                    return false;
                } else if (inWhoseHome == ApiConfig.IN_MY_HOME) {
                    showToast("????????????????????????");
                    return false;
                }
                // ??????????????????
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
                if (inWhoseHome == ApiConfig.IN_OTHER_HOME) {
                    showToast("????????????????????????");
                    break;
                } else if (inWhoseHome == ApiConfig.IN_MY_HOME) {
                    showToast("????????????????????????");
                    break;
                }
                showToast("?????????????????????");
                break;
            }
            default: {

            }
        }
    }

    private void showLike() {
        if (TimeUtils.isFastDoubleClick()) {
            //????????????
            return;
        } else if (!isApproved) {
            //?????????????????????
            showToast("????????????????????????");
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
                    showToastSync("???????????????????????????");
                }

            }
            @Override
            public void onFailure(Exception e) {
                showToast("????????????");
            }
        });
    }

    private void showFocus() {
        if (TimeUtils.isFastDoubleClick()) {
            //????????????
            return;
        } else if (!isApproved) {
            //?????????????????????
            showToast("????????????????????????");
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
                    if (cr.getMsg().equals("Cannot follow oneself")) {
                        showToastSync("??????????????????");
                    } {
                        showToastSync(cr.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                showToastSync("???????????????????????????");
            }
        });
    }

    private void getTravelRecordDetail(String recordId, String authorId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", LoginUser.getInstance().getUser().getId());
        params.put("recordId",recordId);
        params.put("authorId",authorId);
        Api.config(ApiConfig.GET_RECORD_DETAIL,params).getRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                //Log.e("getDetailSuccess1",res);
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
                handler.sendEmptyMessage(5);
            }
        });
    }

    private void getTravelRecordDetailRelatedInfo(String recordId, String authorId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", LoginUser.getInstance().getUser().getId());
        params.put("recordId",recordId);
        params.put("authorId",authorId);
        Api.config(ApiConfig.GET_RECORD_DETAIL_RELATED_INFO, params).getRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                //Log.e("getDetailSuccess2",res + "111");
                RecordDetailRelatedInfoResponse rdrie = new Gson().fromJson(res, RecordDetailRelatedInfoResponse.class);
                if (rdrie.getCode() == 200) {
                    recordDetailRelatedInfoEntity = rdrie.getData();
                    handler.sendEmptyMessage(6);
                } else {
                    handler.sendEmptyMessage(4);
                }
            }

            @Override
            public void onFailure(Exception e) {
                handler.sendEmptyMessage(5);
            }
        });
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

    private void setRecordDetailRelatedInfoData(RecordDetailRelatedInfoEntity rdr) {
        likeNum.setText(String.valueOf(rdr.getLikeNum()));
        focusNum.setText(String.valueOf(rdr.getFocusNum()));
        commentNum.setText(String.valueOf(rdr.getCommentNum()));
        browseNum.setText(String.valueOf(rdr.getBrowseNum()));

        isLike = rdr.getIsLike() == 1;
        isFocus = rdr.getIsFocus() == 1;
        likeIcon.setImageDrawable(isLike ? likeDrawable : unLikeDrawable);
        focusIcon.setImageDrawable(isFocus ? focusDrawable : unFocusDrawable);
        datas = rdr.getF1LevelComments();
        dataSort(0);
        bottomSheetAdapter.setNewData(data);
    }

    private void setRecordDetailData(RecordDetailEntity rde){
        authorName.setText(rde.getAuthorName());
        authorSignature.setText(rde.getAuthorSignature());
        releaseTime.setText(rde.getRecordReleasedTime().split(" ")[0]);
        releaseRegion.setText(rde.getRecordRegion());
        recordName.setText(rde.getRecordName());
        recordMain.setText(rde.getRecordMain());

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
            //????????????
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

            //????????????
            for (int j = 0; j < beanSize; j++) {
                SecondLevelEntity secondLevelEntity = secondLevelEntities.get(j);
                secondLevelEntity.setS2LevelChildPosition(j);
                secondLevelEntity.setS2LevelPosition(i);
                secondLevelEntity.setS2LevelPositionCount(posCount);
                data.add(secondLevelEntity);
            }

            //???????????????item
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
        if (!isApproved) {
            //?????????????????????
            showToast("????????????????????????");
            return;
        }
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
            //??????????????????
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
        //dialog????????????
        mDialogBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    if (slideOffset <= -0.28) {
                        //?????????????????? ????????????
                        bottomSheetDialog.dismiss();
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                TravelRecordDetailActivity.this.slideOffset = slideOffset;//???????????????
            }
        });

        initListener();
    }

    private void initListener() {
        // ????????????
        bottomSheetAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view1, int position) {
                switch ((int) view1.getTag()) {
                    case CommentEntity.TYPE_COMMENT_PARENT:
                        if (view1.getId() == R.id.rl_group) {
                            //??????????????????
                            TravelRecordDetailActivity.this.initInputTextMsgDialog((View) view1.getParent(), false, bottomSheetAdapter.getData().get(position), position);
                        } else if (view1.getId() == R.id.ll_like) {
                            //?????????????????? ?????????????????????????????? ?????????????????????
                            FirstLevelEntity bean = (FirstLevelEntity) bottomSheetAdapter.getData().get(position);
                            //bean.setLikeCount(bean.getLikeCount() + (bean.getIsLike() == 0 ? 1 : -1));
                            //bean.setIsLike(bean.getIsLike() == 0 ? 1 : 0);
                            datas.set(bean.getF1LevelPosition(), bean);
                            TravelRecordDetailActivity.this.dataSort(0);
                            bottomSheetAdapter.notifyDataSetChanged();
                        }
                        break;
                    case CommentEntity.TYPE_COMMENT_MORE:
                        break;
                    case CommentEntity.TYPE_COMMENT_EMPTY:
                        TravelRecordDetailActivity.this.initRefresh();
                        break;

                }

            }
        });
        //????????????
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

    //????????????
    private void addComment(boolean isReply, MultiItemEntity item, final int position, String msg) {
        if (position >= 0) {
            //??????????????????
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
            //??????????????????
            //Log.e("firstLevelComment",recordId + " " + LoginUser.getInstance().getUser().getId());
            HashMap<String, Object> params = new HashMap<>();
            params.put("recordId",recordId);
            params.put("userId",LoginUser.getInstance().getUser().getId());
            params.put("content",msg);
            params.put("createTime",System.currentTimeMillis());
            Api.config(ApiConfig.ADD_FIRST_LEVEL_COMMENT,params).postRequest(new TtitCallback() {
                @Override
                public void onSuccess(String res) {
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
                    handler.sendEmptyMessage(5);
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
        titleLayout.setTitle("????????????");
        titleLayout.getTextView_backward().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // item??????
    public void scrollLocation(int offsetY) {
        try {
            rv_dialog_lists.smoothScrollBy(0, offsetY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????????????????????????????
     */
    public void closeDefaultAnimator(RecyclerView mRvCustomer) {
        if (null == mRvCustomer) return;
        mRvCustomer.getItemAnimator().setAddDuration(0);
        mRvCustomer.getItemAnimator().setChangeDuration(0);
        mRvCustomer.getItemAnimator().setMoveDuration(0);
        mRvCustomer.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) mRvCustomer.getItemAnimator()).setSupportsChangeAnimations(false);
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
                    //item???????????????
                    scrollLocation(-offsetY);
                }
            });
        }
        showInputTextMsgDialog();
    }


    @Override
    public void onLoadMoreRequested() {
        bottomSheetAdapter.loadMoreEnd(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.e("travelDetail", " i am back");
        getTravelRecordDetailRelatedInfo(recordId, authorId);
    }

}