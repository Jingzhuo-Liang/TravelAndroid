package com.example.travel.api;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public interface TtitCallback {

    void onSuccess(String res);
    void onFailure(Exception e);
}
