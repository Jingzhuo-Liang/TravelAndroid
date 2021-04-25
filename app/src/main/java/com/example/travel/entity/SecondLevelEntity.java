package com.example.travel.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import static com.example.travel.entity.CommentEntity.TYPE_COMMENT_CHILD;


public class SecondLevelEntity implements MultiItemEntity {

    //二级评论id
    private String s2LevelCommentId;
    //二级评论头像
    private String s2LevelReplierPortrait;
    //二级评论的留言人昵称
    private String s2LevelReplierName;
    //二级评论的留言人id
    private String s2LevelReplierId;
    //回复评论人的用户名
    //private String replyUserName;
    //回复评论人的用户id
    //private String replyUserId;
    //评论内容（回复内容）
    private String s2LevelContent;
    //创建时间
    private long s2LevelCreateTime;
    //点赞数量
    //private long likeCount;
    //是否点赞了  0没有 1点赞
    //private int isLike;
    //本条评论是否为回复
    //private int isReply;
    //当前评论的总条数（包括这条一级评论）ps:处于未使用状态
    //private long s2LevelTotalCount;
    //当前一级评论的位置（下标）
    private int s2LevelPosition;
    //当前二级评论所在的位置(下标)
    private int s2LevelPositionCount;
    //当前二级评论所在一级评论条数的位置（下标）
    private int s2LevelChildPosition;

    public String getS2LevelCommentId() {
        return s2LevelCommentId;
    }

    public void setS2LevelCommentId(String s2LevelCommentId) {
        this.s2LevelCommentId = s2LevelCommentId;
    }

    public String getS2LevelReplierPortrait() {
        return s2LevelReplierPortrait;
    }

    public void setS2LevelReplierPortrait(String s2LevelReplierPortrait) {
        this.s2LevelReplierPortrait = s2LevelReplierPortrait;
    }

    public String getS2LevelReplierName() {
        return s2LevelReplierName;
    }

    public void setS2LevelReplierName(String s2LevelReplierName) {
        this.s2LevelReplierName = s2LevelReplierName;
    }

    public String getS2LevelReplierId() {
        return s2LevelReplierId;
    }

    public void setS2LevelReplierId(String s2LevelReplierId) {
        this.s2LevelReplierId = s2LevelReplierId;
    }

    public String getS2LevelContent() {
        return s2LevelContent;
    }

    public void setS2LevelContent(String s2LevelContent) {
        this.s2LevelContent = s2LevelContent;
    }

    public long getS2LevelCreateTime() {
        return s2LevelCreateTime;
    }

    public void setS2LevelCreateTime(long s2LevelCreateTime) {
        this.s2LevelCreateTime = s2LevelCreateTime;
    }

//    public long getS2LevelTotalCount() {
//        return s2LevelTotalCount;
//    }
//
//    public void setS2LevelTotalCount(long s2LevelTotalCount) {
//        this.s2LevelTotalCount = s2LevelTotalCount;
//    }

    public int getS2LevelPosition() {
        return s2LevelPosition;
    }

    public void setS2LevelPosition(int s2LevelPosition) {
        this.s2LevelPosition = s2LevelPosition;
    }

    public int getS2LevelPositionCount() {
        return s2LevelPositionCount;
    }

    public void setS2LevelPositionCount(int s2LevelPositionCount) {
        this.s2LevelPositionCount = s2LevelPositionCount;
    }

    public int getS2LevelChildPosition() {
        return s2LevelChildPosition;
    }

    public void setS2LevelChildPosition(int s2LevelChildPosition) {
        this.s2LevelChildPosition = s2LevelChildPosition;
    }

    @Override
    public int getItemType() {
        return TYPE_COMMENT_CHILD;
    }
}
