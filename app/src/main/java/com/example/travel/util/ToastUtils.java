package com.example.travel.util;

import android.content.Context;
import android.widget.Toast;

// 没啥用
/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class ToastUtils{
    private Toast mToast;

    public void showShort(Context context, String  string) {
        mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT); //下面用setText不用makeText，为了取消小米手机自带的Toast应用名
        mToast.setText(string);
        mToast.show();
    }
}
