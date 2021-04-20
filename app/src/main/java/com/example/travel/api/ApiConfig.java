package com.example.travel.api;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class ApiConfig {
    //public static final String BASE_URL = "http://192.168.31.206:8666";
    public static final int PAGE_SIZE = 4;

    public static final String BASE_URL = "http://114.115.173.237:8000";
    public static final String LOGIN = "/travel/login";
    public static final String REGISTER = "/travel/register";
    public static final String UPDATE_USER = "/travel/updateUserInfo";
    public static final String EXIT_LOGIN = "/travel/exitLogin";
    public static final String RELEASE_TRAVEL_RECORD = "/travel/releaseTravelRecord";
    public static final String GET_TRAVEL_RECORD_NO_RECOMMEND = "/travel/getTravelRecordNoRecommend";
    public static final String GET_TRAVEL_RECORD_RECOMMEND = "/travel/getTravelRecordRecommend";
    public static final String GET_TRAVEL_RECORD_NO_FOCUS = "/travel/getTravelRecordNoFocus";
    public static final String GET_TRAVEL_RECORD_FOCUS = "/travel/getTravelRecordFocus";
}
