package com.example.travel.util;

import android.location.Location;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

/**
 * @@author:ljz
 * @@date:2021/5/8,15:45
 * @@version:1.0
 * @@annotation:
 **/
public class MyLocationListener extends BDAbstractLocationListener {

    private String province;
    private String city;
    private BDLocation location;
    private double longitude;
    private double latitude;
    private String region = "";

    @Override
    public void onReceiveLocation(BDLocation bdLocation){
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取地址相关的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
        String addr = bdLocation.getAddrStr();    //获取详细地址信息
        String country = bdLocation.getCountry();    //获取国家
        String district = bdLocation.getDistrict();    //获取区县
        String street = bdLocation.getStreet();    //获取街道信息
        String adcode = bdLocation.getAdCode();    //获取adcode
        String town = bdLocation.getTown();    //获取乡镇信息
        int errorCode = bdLocation.getLocType();
        province = bdLocation.getProvince();    //获取省份
        city = bdLocation.getCity();    //获取城市
        location = bdLocation;
        latitude = bdLocation.getLatitude();
        longitude = bdLocation.getLongitude();
        region = province + "-" + city;
        //Log.e("onReceiveLocation",province + "!!!!!" + city + " ???" + String.valueOf(bdLocation.getLongitude()));
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BDLocation getLocation() {
        return location;
    }

    public String getRegion() {
        return region;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}