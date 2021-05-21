package com.example.travel.util;

import java.util.Locale;

public class TimeUtils {

    public static String getRecentTimeSpanByNow(final long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 1000) {
            return "刚刚";
        } else if (span < TimeConstants.MIN) {
            return String.format(Locale.getDefault(), "%d秒前", span / TimeConstants.SEC);
        } else if (span < TimeConstants.HOUR) {
            return String.format(Locale.getDefault(), "%d分钟前", span / TimeConstants.MIN);
        } else if (span < TimeConstants.DAY) {
            return String.format(Locale.getDefault(), "%d小时前", span / TimeConstants.HOUR);
        } else if (span < TimeConstants.MONTH) {
            return String.format(Locale.getDefault(), "%d天前", span / TimeConstants.DAY);
        } else if (span < TimeConstants.YEAR) {
            return String.format(Locale.getDefault(), "%d月前", span / TimeConstants.MONTH);
        } else if (span > TimeConstants.YEAR) {
            return String.format(Locale.getDefault(), "%d年前", span / TimeConstants.YEAR);
        } else {
            return String.format("%tF", millis);
        }
    }

    private static long lastClickTime = 0;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClickWithin2Second() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
