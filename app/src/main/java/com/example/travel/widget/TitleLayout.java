package com.example.travel.widget;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.travel.R;
import com.example.travel.activity.EditName;
import com.example.travel.util.ActivityCollector;


public class TitleLayout extends LinearLayout {
    private ImageView iv_backward;
    private TextView tv_title, tv_forward;
    private ImageView tv_myImage;

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LinearLayout bar_title = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.bar_title, this);
        iv_backward = (ImageView) bar_title.findViewById(R.id.iv_backward);
        tv_title = (TextView) bar_title.findViewById(R.id.tv_title);
        tv_forward = (TextView) bar_title.findViewById(R.id.tv_forward);
        tv_myImage = bar_title.findViewById(R.id.tv_my_image);
        /*
        if(ActivityCollector.getCurrentActivity().getClass().equals(PersonInfo.class)){
            tv_forward.setText("保存");
            tv_title.setText("编辑资料");
        }
        if(ActivityCollector.getCurrentActivity().getClass().equals(EditName.class)){
            tv_forward.setText("完成");
            tv_title.setText("编辑昵称");
        }

         */


    }
    public TextView getTextView_forward(){
        return tv_forward;
    }

    public ImageView getTextView_backward() {
        return iv_backward;
    }

    public void setTitle(String title) {
        this.tv_title.setText(title);
    }

    public ImageView getTv_myImage() {
        return this.tv_myImage;
    }
}
