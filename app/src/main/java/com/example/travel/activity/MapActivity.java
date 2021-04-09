package com.example.travel.activity;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.Marker;
import com.example.travel.R;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends BaseActivity{
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    @Override
    protected int initLayout() {
        SDKInitializer.initialize(getApplicationContext());
        return R.layout.activity_map;
    }

    @Override
    protected void initView() {
        mMapView = (MapView) findViewById(R.id.map_view);
    }

    @Override
    protected void initData() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        showData();
    }

    protected void showData(){
        //TODO:获取数据解析经纬度
        create_map_point(39.91923, 116.387428);
        create_map_point(49.91923, 110.387428);
        create_map_point(19.91923, 16.387428);
        showInfoWindow();
    }

    private void showInfoWindow(){
        /*
        显示详情
         */
        OnMarkerClickListener marker_Listener = new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //在显示新信息窗之前，先关闭已经在显示的信息窗
                mBaiduMap.hideInfoWindow();
                //获取maker此时的经纬度
                Double latitude = marker.getPosition().latitude;
                Double longitude = marker.getPosition().longitude;
                //TODO:通过经纬度动态加载图片到xml
                //显示信息窗
                View infoWindow_view = getLayoutInflater().inflate(R.layout.map_pop_window, null);
                ImageView back_image_view = (ImageView) infoWindow_view.findViewById(R.id.back);
                back_image_view.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mBaiduMap.hideInfoWindow();
                            }
                        }
                );
                BitmapDescriptor infoWindow_bitmap = BitmapDescriptorFactory.fromView(infoWindow_view);
                //信息窗点击处理事件
                Log.d("tag", "infoWindow_view="+infoWindow_view);
                Log.d("tag", "infoWindow_bitmap="+infoWindow_bitmap);
                OnInfoWindowClickListener infoWindow_ClickListener = new OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick() {
                        // TODO Auto-generated method stub
                        mBaiduMap.hideInfoWindow();

                    }
                };
                //定义信息窗
                InfoWindow infoWindow = new InfoWindow(infoWindow_bitmap,//信息窗布局
                        marker.getPosition(), //信息窗的点
                        100, //信息窗与点的位置关系
                        //infoWindow监听器
                        infoWindow_ClickListener
                );
                //显示信息窗
                mBaiduMap.showInfoWindow(infoWindow);
                return true;
            }
        };
        mBaiduMap.setOnMarkerClickListener(marker_Listener);
    }
    private void create_map_point(double longtitude, double altitude){
        /*
        创建地图上坐标显示
         */
        //定义Maker坐标点
        LatLng point = new LatLng(longtitude, altitude);
        final View markerView = LayoutInflater.from(this).inflate(R.layout.map_loc,null);
        Bitmap bitmap = getViewBitmap(markerView);
        BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bitmap);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(point)//Marker经纬度
                .icon(bd);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(markerOptions);
    }


    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        return bitmap;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

}
