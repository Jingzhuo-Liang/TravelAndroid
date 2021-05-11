package com.example.travel.entity;

/**
 * @@author:ljz
 * @@date:2021/4/19,21:26
 * @@version:1.0
 * @@annotation:
 **/
public class MyTravelRecordEntity {

    private String recordId;
    private String recordCoverImage;
    private String recordName;
    private String recordReleasedTime;
    private String recordRegion;
    private int recordState; // 0-审核通过（发布） 1-待审核
    private int recordLimit = 0; // 游记权限 0-所有人可见 1-仅关注可见 2-仅自己可见
    private int likeNum;
    private int commitNum;
    private int browseNum;

    public int getRecordLimit() {
        return recordLimit;
    }

    public void setRecordLimit(int recordLimit) {
        this.recordLimit = recordLimit;
    }

    public String getRecordRegion() {
        return recordRegion;
    }

    public void setRecordRegion(String recordRegion) {
        this.recordRegion = recordRegion;
    }

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

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordReleasedTime() {
        return recordReleasedTime;
    }

    public void setRecordReleasedTime(String recordReleaseTime) {
        this.recordReleasedTime = recordReleaseTime;
    }

    public int getRecordState() {
        return recordState;
    }

    public void setRecordState(int recordState) {
        this.recordState = recordState;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getCommitNum() {
        return commitNum;
    }

    public void setCommitNum(int commitNum) {
        this.commitNum = commitNum;
    }

    public int getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(int browseNum) {
        this.browseNum = browseNum;
    }
}
