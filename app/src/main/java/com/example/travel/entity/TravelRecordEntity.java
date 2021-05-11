package com.example.travel.entity;

import java.io.Serializable;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class TravelRecordEntity implements Serializable {

    private int type; //0:record 1:ad

    private String adId;
    private String adCoverImage;
    private String adName;
    private String adPortrait;
    private String adUrl;


    private String recordId;
    private String recordCoverImage;
    private String authorName;
    private String authorId;
    private String authorPortrait;
    private String recordRegion;
    private String recordName;
    private int likeNum;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRecordCoverImage() {
        return recordCoverImage;
    }

    public void setRecordCoverImage(String recordCoverImage) {
        this.recordCoverImage = recordCoverImage;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorPortrait() {
        return authorPortrait;
    }

    public void setAuthorPortrait(String authorPortrait) {
        this.authorPortrait = authorPortrait;
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

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getAdCoverImage() {
        return adCoverImage;
    }

    public void setAdCoverImage(String adCoverImage) {
        this.adCoverImage = adCoverImage;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdPortrait() {
        return adPortrait;
    }

    public void setAdPortrait(String adPortrait) {
        this.adPortrait = adPortrait;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }
}
