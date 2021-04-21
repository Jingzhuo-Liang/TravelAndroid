package com.example.travel.activity;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.example.travel.bean.CommentEntity;
import com.example.travel.bean.CommentMoreBean;
import com.example.travel.bean.FirstLevelBean;
import com.example.travel.bean.SecondLevelBean;
import com.example.travel.listener.SoftKeyBoardListener;
import com.example.travel.util.RecyclerViewUtil;
import com.example.travel.widget.InputTextMsgDialog;
import com.example.travel.widget.TitleLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class TravelRecordDetailActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener{

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

    //dialog
    private List<MultiItemEntity> data = new ArrayList<>();
    private List<FirstLevelBean> datas = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private InputTextMsgDialog inputTextMsgDialog;
    private float slideOffset = 0;
    private String content = "我听见你的声音，有种特别的感觉。让我不断想，不敢再忘记你。如果真的有一天，爱情理想会实现，我会加倍努力好好对你，永远不改变";
    private CommentDialogMutiAdapter bottomSheetAdapter;
    private RecyclerView rv_dialog_lists;
    private long totalCount = 22;
    private int offsetY;
    private int positionCount = 0;
    private RecyclerViewUtil mRecyclerViewUtil;
    private SoftKeyBoardListener mKeyBoardListener;
    //dialog

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
        focusIcon.setOnClickListener(this);

        mRecyclerViewUtil = new RecyclerViewUtil();
        initDialogData();
        dataSort(0);
        showSheetDialog();
    }

    private void initRefresh() {
        datas.clear();
        initDialogData();
        dataSort(0);
        bottomSheetAdapter.setNewData(data);
    }

    //原始数据 一般是从服务器接口请求过来的
    private void initDialogData() {
        int size = 10;
        for (int i = 0; i < size; i++) {
            FirstLevelBean firstLevelBean = new FirstLevelBean();
            firstLevelBean.setContent("第" + (i + 1) + "人评论内容" + (i % 3 == 0 ? content + (i + 1) + "次" : ""));
            firstLevelBean.setCreateTime(System.currentTimeMillis());
            firstLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3370302115,85956606&fm=26&gp=0.jpg");
            firstLevelBean.setId(i + "");
            firstLevelBean.setIsLike(0);
            firstLevelBean.setLikeCount(i);
            firstLevelBean.setUserName("星梦缘" + (i + 1));
            firstLevelBean.setTotalCount(i + size);

            List<SecondLevelBean> beans = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                SecondLevelBean secondLevelBean = new SecondLevelBean();
                secondLevelBean.setContent("一级第" + (i + 1) + "人 二级第" + (j + 1) + "人评论内容" + (j % 3 == 0 ? content + (j + 1) + "次" : ""));
                secondLevelBean.setCreateTime(System.currentTimeMillis());
                secondLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
                secondLevelBean.setId(j + "");
                secondLevelBean.setIsLike(0);
                secondLevelBean.setLikeCount(j);
                secondLevelBean.setUserName("星梦缘" + (i + 1) + "  " + (j + 1));
                secondLevelBean.setIsReply(j % 5 == 0 ? 1 : 0);
                secondLevelBean.setReplyUserName(j % 5 == 0 ? "闭嘴家族" + j : "");
                secondLevelBean.setTotalCount(firstLevelBean.getTotalCount());
                beans.add(secondLevelBean);
                firstLevelBean.setSecondLevelBeans(beans);
            }
            datas.add(firstLevelBean);
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
            FirstLevelBean firstLevelBean = datas.get(i);
            if (firstLevelBean == null) continue;
            firstLevelBean.setPosition(i);
            posCount += 2;
            List<SecondLevelBean> secondLevelBeans = firstLevelBean.getSecondLevelBeans();
            if (secondLevelBeans == null || secondLevelBeans.isEmpty()) {
                firstLevelBean.setPositionCount(posCount);
                data.add(firstLevelBean);
                continue;
            }
            int beanSize = secondLevelBeans.size();
            posCount += beanSize;
            firstLevelBean.setPositionCount(posCount);
            data.add(firstLevelBean);

            //二级评论
            for (int j = 0; j < beanSize; j++) {
                SecondLevelBean secondLevelBean = secondLevelBeans.get(j);
                secondLevelBean.setChildPosition(j);
                secondLevelBean.setPosition(i);
                secondLevelBean.setPositionCount(posCount);
                data.add(secondLevelBean);
            }

            //展示更多的item
            if (beanSize <= 18) {
                CommentMoreBean moreBean = new CommentMoreBean();
                moreBean.setPosition(i);
                moreBean.setPositionCount(posCount);
                moreBean.setTotalCount(firstLevelBean.getTotalCount());
                data.add(moreBean);
            }

        }
    }

    public void show(View view) {
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
                            FirstLevelBean bean = (FirstLevelBean) bottomSheetAdapter.getData().get(position);
                            bean.setLikeCount(bean.getLikeCount() + (bean.getIsLike() == 0 ? 1 : -1));
                            bean.setIsLike(bean.getIsLike() == 0 ? 1 : 0);
                            datas.set(bean.getPosition(), bean);
                            TravelRecordDetailActivity.this.dataSort(0);
                            bottomSheetAdapter.notifyDataSetChanged();
                        }
                        break;
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
                    case CommentEntity.TYPE_COMMENT_MORE:
                        //在项目中是从服务器获取数据，其实就是二级评论分页获取
                        CommentMoreBean moreBean = (CommentMoreBean) bottomSheetAdapter.getData().get(position);
                        SecondLevelBean secondLevelBean = new SecondLevelBean();
                        secondLevelBean.setContent("more comment" + 1);
                        secondLevelBean.setCreateTime(System.currentTimeMillis());
                        secondLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
                        secondLevelBean.setId(1 + "");
                        secondLevelBean.setIsLike(0);
                        secondLevelBean.setLikeCount(0);
                        secondLevelBean.setUserName("星梦缘" + 1);
                        secondLevelBean.setIsReply(0);
                        secondLevelBean.setReplyUserName("闭嘴家族" + 1);
                        secondLevelBean.setTotalCount(moreBean.getTotalCount() + 1);

                        datas.get((int) moreBean.getPosition()).getSecondLevelBeans().add(secondLevelBean);
                        TravelRecordDetailActivity.this.dataSort(0);
                        bottomSheetAdapter.notifyDataSetChanged();

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
        final String userName = "hui";
        if (position >= 0) {
            //添加二级评论
            int pos = 0;
            String replyUserName = "未知";
            if (item instanceof FirstLevelBean) {
                FirstLevelBean firstLevelBean = (FirstLevelBean) item;
                positionCount = (int) (firstLevelBean.getPositionCount() + 1);
                pos = (int) firstLevelBean.getPosition();
                replyUserName = firstLevelBean.getUserName();
            } else if (item instanceof SecondLevelBean) {
                SecondLevelBean secondLevelBean = (SecondLevelBean) item;
                positionCount = (int) (secondLevelBean.getPositionCount() + 1);
                pos = (int) secondLevelBean.getPosition();
                replyUserName = secondLevelBean.getUserName();
            }

            SecondLevelBean secondLevelBean = new SecondLevelBean();
            secondLevelBean.setReplyUserName(replyUserName);
            secondLevelBean.setIsReply(isReply ? 1 : 0);
            secondLevelBean.setContent(msg);
            secondLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3370302115,85956606&fm=26&gp=0.jpg");
            secondLevelBean.setCreateTime(System.currentTimeMillis());
            secondLevelBean.setIsLike(0);
            secondLevelBean.setUserName(userName);
            secondLevelBean.setId("");
            secondLevelBean.setPosition(positionCount);

            datas.get(pos).getSecondLevelBeans().add(secondLevelBean);
            TravelRecordDetailActivity.this.dataSort(0);
            bottomSheetAdapter.notifyDataSetChanged();
            rv_dialog_lists.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((LinearLayoutManager) rv_dialog_lists.getLayoutManager())
                            .scrollToPositionWithOffset(positionCount >= data.size() - 1 ? data.size() - 1
                                    : positionCount, positionCount >= data.size() - 1 ? Integer.MIN_VALUE : rv_dialog_lists.getHeight());
                }
            }, 100);

        } else {
            //添加一级评论
            FirstLevelBean firstLevelBean = new FirstLevelBean();
            firstLevelBean.setUserName(userName);
            firstLevelBean.setId(bottomSheetAdapter.getItemCount() + 1 + "");
            firstLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
            firstLevelBean.setCreateTime(System.currentTimeMillis());
            firstLevelBean.setContent(msg);
            firstLevelBean.setLikeCount(0);
            firstLevelBean.setSecondLevelBeans(new ArrayList<SecondLevelBean>());
            datas.add(0, firstLevelBean);
            TravelRecordDetailActivity.this.dataSort(0);
            bottomSheetAdapter.notifyDataSetChanged();
            rv_dialog_lists.scrollToPosition(0);
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
        if (datas.size() >= totalCount) {
            bottomSheetAdapter.loadMoreEnd(false);
            return;
        }
        FirstLevelBean firstLevelBean = new FirstLevelBean();
        firstLevelBean.setUserName("hui");
        firstLevelBean.setId((datas.size() + 1) + "");
        firstLevelBean.setHeadImg("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1918451189,3095768332&fm=26&gp=0.jpg");
        firstLevelBean.setCreateTime(System.currentTimeMillis());
        firstLevelBean.setContent("add loadmore comment");
        firstLevelBean.setLikeCount(0);
        firstLevelBean.setSecondLevelBeans(new ArrayList<SecondLevelBean>());
        datas.add(firstLevelBean);
        dataSort(datas.size() - 1);
        bottomSheetAdapter.notifyDataSetChanged();
        bottomSheetAdapter.loadMoreComplete();

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
                //Log.e("recordDetail","click commit");
                bottomSheetAdapter.notifyDataSetChanged();
                slideOffset = 0;
                bottomSheetDialog.show();
                break;
            }
            case R.id.record_detail_focusIcon :{
                //Log.e("recordDetail","click focus");
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