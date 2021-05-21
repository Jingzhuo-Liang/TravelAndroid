package com.example.travel.api;

import java.util.DoubleSummaryStatistics;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class ApiConfig {
    //public static final String BASE_URL = "http://192.168.31.206:8666";
    public static final String DEFAULT_PORTRAIT_URL = "http://114.115.173.237:8000/static/avatar/default.jpg";
    public static final String BASE_URL = "http://114.115.173.237:8000";
    public static final int PAGE_SIZE = 4;

    public static final String LOGIN = "/travel/login";
    public static final String REGISTER = "/travel/register";
    public static final String UPDATE_USER = "/travel/updateUserInfo";
    public static final String UPDATE_USER_PORTRAIT = "/travel/updateUserPortrait";
    public static final String EXIT_LOGIN = "/travel/exitLogin";
    public static final String RELEASE_TRAVEL_RECORD = "/travel/releaseTravelRecord";
    public static final String GET_TRAVEL_RECORD_NO_RECOMMEND = "/travel/getTravelRecordNoRecommend";
    public static final String GET_TRAVEL_RECORD_RECOMMEND = "/travel/getTravelRecordRecommend";
    public static final String GET_TRAVEL_RECORD_NO_FOCUS = "/travel/getTravelRecordNoFocus";
    public static final String GET_TRAVEL_RECORD_FOCUS = "/travel/getTravelRecordFocus";
    public static final String GET_MY_TRAVEL_RECORD = "/travel/getMyTravelRecord";
    public static final String GET_RECORD_DETAIL =  "/travel/getRecordDetail";
    public static final String ADD_FIRST_LEVEL_COMMENT = "/travel/addFirstLevelComment";
    public static final String ADD_SECOND_LEVEL_COMMENT = "/travel/addSecondLevelComment";
    public static final String FOCUS_AUTHOR = "/travel/focusAuthor";
    public static final String LIKE_RECORD = "/travel/likeRecord";
    public static final String DELETE_MY_TRAVEL_RECORD = "/travel/deleteMyTravelRecord";
    public static final String GET_MAP = "/travel/getMap";
    public static final String SEARCH_TRAVEL_RECORD = "/travel/searchTravelRecord";
    public static final String GET_USER_RELATED_INFO = "/travel/getUserRelatedInfo";
    public static final String GET_OTHER_USER_INFO = "/travel/getOtherUserInfo";
    public static final String GET_OTHER_USER_TRAVEL_RECORD = "/travel/getOtherUserTravelRecord";
    public static final String BROWSE_TRAVEL_RECORD = "/travel/browseTravelRecord";
    public static final String MODIFY_MY_TRAVEL_RECORD_STEP1 = "/travel/modifyTravelRecord/getRecord";
    public static final String MODIFY_MY_TRAVEL_RECORD_STEP2 = "/travel/modifyTravelRecord/postRecord";
    public static final String USER_ACCESS_AD_LINK = "/travel/advertisement/access";
    public static final String USER_INTEREST_AD = "/travel/advertisement/interest";
    public static final String USER_UNINTEREST_AD = "/travel/advertisement/unInterest";
    public static final String GET_ALL_SYSTEM_MESSAGE = "/travel/systemMessage/getAll";
    public static final String DELETE_SINGLE_SYSTEM_MESSAGE = "/travel/systemMessage/deleteSingle";
    public static final String DELETE_ALL_SYSTEM_MESSAGE = "/travel/systemMessage/deleteAll";
    public static final String READ_SYSTEM_MESSAGE = "/travel/systemMessage/readMessage";

    //常量
    public static final int HOMEPAGE_RECOMMEND = 1;
    public static final int HOME_FOCUS = 0;
    public static final int USER_NAME_MAX_LENGTH = 15;
    public static final int USER_SIGNATURE_MAX_LENGTH = 30;

    //处于主页状态
    public static final int IN_NO_HOME = 0;
    public static final int IN_MY_HOME = 1;
    public static final int IN_OTHER_HOME = 2;
}
