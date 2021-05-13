package com.example.travel.entity;

import java.util.ArrayList;

/**
 * @@author:ljz
 * @@date:2021/4/23,0:11
 * @@version:1.0
 * @@annotation:
 **/
public class RecordDetailEntity {

    private String authorName;
    private String authorSignature;
    private String authorPortrait;
    private String recordReleasedTime;
    private String recordRegion;
    private String recordName;
    private String recordMain;
    private ArrayList<String> recordImages;
    private int isFocus;
    private int focusNum;
    private int isLike;
    private int likeNum;
    private int commentNum;
    private int browseNum;
    private ArrayList<FirstLevelEntity> f1LevelComments;


    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorSignature() {
        return authorSignature;
    }

    public void setAuthorSignature(String authorSignature) {
        this.authorSignature = authorSignature;
    }

    public String getAuthorPortrait() {
        return authorPortrait;
    }

    public void setAuthorPortrait(String authorPortrait) {
        this.authorPortrait = authorPortrait;
    }

    public String getRecordReleasedTime() {
        return recordReleasedTime;
    }

    public void setRecordReleasedTime(String recordReleasedTime) {
        this.recordReleasedTime = recordReleasedTime;
    }

    public String getRecordRegion() {
        return recordRegion;
    }

    public void setRecordRegion(String recordRegion) {
        this.recordRegion = recordRegion;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordMain() {
        return recordMain;
    }

    public void setRecordMain(String recordMain) {
        this.recordMain = recordMain;
    }

    public ArrayList<String> getRecordImages() {
        return recordImages;
    }

    public void setRecordImages(ArrayList<String> recordImages) {
        this.recordImages = recordImages;
    }

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

    public ArrayList<FirstLevelEntity> getF1LevelComments() {
        return f1LevelComments;
    }

    public void setF1LevelComments(ArrayList<FirstLevelEntity> f1LevelComments) {
        this.f1LevelComments = f1LevelComments;
    }

    public int getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(int browseNum) {
        this.browseNum = browseNum;
    }
}
