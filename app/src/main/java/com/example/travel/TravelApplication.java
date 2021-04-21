package com.example.travel;

import android.app.Application;

/**
 * @author ganhuanhui
 * 时间：2019/12/17 0017
 * 描述：
 */
public class TravelApplication extends Application {

    public static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
