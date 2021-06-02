package com.example.travel.widget;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.travel.R;

/**
 * @@author:ljz
 * @@date:2021/6/2,15:22
 * @@version:1.0
 * @@annotation:
 **/
public class BaiduZoomControl extends LinearLayout {

    private ImageView refresh_icon;
    private ImageView zoomIn_icon;
    private ImageView zoomOut_icon;

    public BaiduZoomControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        LinearLayout baiduZoomControl = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.baidu_zoom_view, this);
        refresh_icon = baiduZoomControl.findViewById(R.id.baidu_zoom_refresh);
        zoomIn_icon = baiduZoomControl.findViewById(R.id.baidu_zoom_in);
        zoomOut_icon = baiduZoomControl.findViewById(R.id.baidu_zoom_out);
    }

    public ImageView getRefresh_icon() {
        return refresh_icon;
    }

    public ImageView getZoomIn_icon() {
        return zoomIn_icon;
    }

    public ImageView getZoomOut_icon() {
        return zoomOut_icon;
    }
}
