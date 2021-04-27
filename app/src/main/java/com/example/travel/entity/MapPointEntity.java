package com.example.travel.entity;

/**
 * @@author:ljz
 * @@date:2021/4/27,19:48
 * @@version:1.0
 * @@annotation:
 **/
public class MapPointEntity {

    private String recordId;
    private double longitude;
    private double latitude;
    private String recordCoverImage;
    private String recordName;
    private String likeNum;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }
}
