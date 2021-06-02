package com.example.travel.entity;

import java.util.ArrayList;

/**
 * @@author:ljz
 * @@date:2021/6/2,17:13
 * @@version:1.0
 * @@annotation:
 **/
public class RecordDetailRelatedInfoEntity {

    private int isFocus;
    private int focusNum;
    private int isLike;
    private int likeNum;
    private int commentNum;
    private int browseNum;
    private ArrayList<FirstLevelEntity> f1LevelComments;

    public int getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(int isFocus) {
        this.isFocus = isFocus;
    }

    public int getFocusNum() {
        return focusNum;
    }

    public void setFocusNum(int focusNum) {
        this.focusNum = focusNum;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(int browseNum) {
        this.browseNum = browseNum;
    }

    public ArrayList<FirstLevelEntity> getF1LevelComments() {
        return f1LevelComments;
    }

    public void setF1LevelComments(ArrayList<FirstLevelEntity> f1LevelComments) {
        this.f1LevelComments = f1LevelComments;
    }
}
