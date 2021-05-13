package com.example.travel.entity;

import android.media.audiofx.AcousticEchoCanceler;

import java.util.ArrayList;

/**
 * @@author:ljz
 * @@date:2021/5/11,22:19
 * @@version:1.0
 * @@annotation:
 **/
public class ModifyTravelRecordEntity {

    private String recordRegion;
    private String recordName;
    private String recordMain;
    private ArrayList<String> recordImages;
    private int recordLimit;
    private double latitude;
    private double longitude;

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRecordLimit() {
        return recordLimit;
    }

    public void setRecordLimit(int recordLimit) {
        this.recordLimit = recordLimit;
    }
}
