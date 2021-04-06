package com.example.travel.util;

public class StringUtils {

    public static boolean isEmpty(String s) {
        if (s == null || s.length() <= 0) {
            return true;
        }
        return false;
    }


}
