package com.example.travel.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import static com.example.travel.entity.CommentEntity.TYPE_COMMENT_PARENT;

public class FirstLevelEntity implements MultiItemEntity {
    
    private List<SecondLevelEntity> s2LevelComments;

    //一级评论id
    private String f1LevelCommentId;
    //一级评论头像
    private String f1LevelMessengerPortrait;
    //一级评论的用户名
    private String f1LevelMessengerName;
    //一级评论的用户id
    private String f1LevelMessengerId;
    //评论内容
    private String f1LevelContent;
    //创建时间
    private long f1LevelCreateTime;
    //点赞数量
    //private long likeCount;
    //是否点赞了  0没有 1点赞
    //private int isLike;
    //当前评论的总条数（包括这条一级评论）ps:处于未使用状态
    //private long f1LevelTotalCount;
    //当前一级评论的位置（下标）
    private int f1LevelPosition;
    //当前一级评论所在的位置(下标)
    private int f1LevelPositionCount;

    public List<SecondLevelEntity> getS2LevelComments() {
        return s2LevelComments;
    }

    public void setS2LevelComments(List<SecondLevelEntity> s2LevelComments) {
        this.s2LevelComments = s2LevelComments;
    }

    public String getF1LevelCommentId() {
        return f1LevelCommentId;
    }

    public void setF1LevelCommentId(String f1LevelCommentId) {
        this.f1LevelCommentId = f1LevelCommentId;
    }

    public String getF1LevelMessengerPortrait() {
        return f1LevelMessengerPortrait;
    }

    public void setF1LevelMessengerPortrait(String f1LevelMessengerPortrait) {
        this.f1LevelMessengerPortrait = f1LevelMessengerPortrait;
    }

    public String getF1LevelMessengerName() {
        return f1LevelMessengerName;
    }

    public void setF1LevelMessengerName(String f1LevelMessengerName) {
        this.f1LevelMessengerName = f1LevelMessengerName;
    }

    public String getF1LevelMessengerId() {
        return f1LevelMessengerId;
    }

    public void setF1LevelMessengerId(String f1LevelMessengerId) {
        this.f1LevelMessengerId = f1LevelMessengerId;
    }

    public String getF1LevelContent() {
        return f1LevelContent;
    }

    public void setF1LevelContent(String f1LevelContent) {
        this.f1LevelContent = f1LevelContent;
    }

    public long getF1LevelCreateTime() {
        return f1LevelCreateTime;
    }

    public void setF1LevelCreateTime(long f1LevelCreateTime) {
        this.f1LevelCreateTime = f1LevelCreateTime;
    }

//    public long getF1LevelTotalCount() {
//        return f1LevelTotalCount;
//    }
//
//    public void setF1LevelTotalCount(long f1LevelTotalCount) {
//        this.f1LevelTotalCount = f1LevelTotalCount;
//    }

    public int getF1LevelPosition() {
        return f1LevelPosition;
    }

    public void setF1LevelPosition(int f1LevelPosition) {
        this.f1LevelPosition = f1LevelPosition;
    }

    public int getF1LevelPositionCount() {
        return f1LevelPositionCount;
    }

    public void setF1LevelPositionCount(int f1LevelPositionCount) {
        this.f1LevelPositionCount = f1LevelPositionCount;
    }

    @Override
    public int getItemType() {
        return TYPE_COMMENT_PARENT;
    }
}
