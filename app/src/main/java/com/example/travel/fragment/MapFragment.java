package com.example.travel.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.travel.R;
import com.example.travel.entity.MapPointEntity;
import com.example.travel.util.LoginUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MapFragment extends BaseFragment {

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    ArrayList<MapPointEntity> list;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }


    @Override
    protected int initLayout() {
        SDKInitializer.initialize(getContext().getApplicationContext());
        //SDKInitializer.initialize(getActivity().getApplicationContext());
        return R.layout.fragment_map;
    }

    @Override
    protected void initView() {
        mMapView = (MapView) mRootView.findViewById(R.id.map_view);
    }

    @Override
    protected void initData() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        getMapPoints();
        showData();
    }

    private void showData(){
        //TODO:获取数据解析经纬度
        //create_map_point(39.91923, 116.387428);
        //create_map_point(49.91923, 110.387428);
        //create_map_point(19.91923, 16.387428);
        for (MapPointEntity mpe: list) {
            create_map_point(mpe.getLongitude(),mpe.getLatitude());
        }
        showInfoWindow();
    }

    private void getMapPoints() {
        list = new ArrayList<>();
        MapPointEntity mapPoint = new MapPointEntity();
        mapPoint.setRecordId("1");
        mapPoint.setLongitude(116.387428);
        mapPoint.setLatitude(39.91923);
        mapPoint.setRecordName("111111");
        mapPoint.setLikeNum("111");
        mapPoint.setRecordCoverImage("http://114.115.173.237:8000/static/picture/picture_3793ed3c665641a3b68cafa2e5cadf22.png");
        list.add(mapPoint);
        mapPoint = new MapPointEntity();
        mapPoint.setRecordId("222");
        mapPoint.setLongitude(110.387428);
        mapPoint.setLatitude(49.91923);
        mapPoint.setRecordName("222222");
        mapPoint.setLikeNum("222");
        mapPoint.setRecordCoverImage("http://114.115.173.237:8000/static/picture/picture_4d10391eb55a425882211d1952782ce4_0.png");
        list.add(mapPoint);
        mapPoint = new MapPointEntity();
        mapPoint.setRecordId("333");
        mapPoint.setLongitude(113.387428);
        mapPoint.setLatitude(40.91923);
        mapPoint.setRecordName("33333");
        mapPoint.setLikeNum("333");
        mapPoint.setRecordCoverImage("http://114.115.173.237:8000/static/picture/picture_f8978682184642719ea69886f340cd71_0.png");
        list.add(mapPoint);
    }

    private void showInfoWindow(){
        /*
        显示详情
         */
        BaiduMap.OnMarkerClickListener marker_Listener = new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //在显示新信息窗之前，先关闭已经在显示的信息窗
                mBaiduMap.hideInfoWindow();
                //获取maker此时的经纬度
                //Double latitude = marker.getPosition().latitude;
                //Double longitude = marker.getPosition().longitude;
                MapPointEntity mapPointEntity = list.get(getMarkerIndex(marker.getPosition().longitude,marker.getPosition().latitude));
                //Log.e("positionIndex",);
                //TODO:通过经纬度动态加载图片到xml
                //显示信息窗
                //Log.e("markClick",mapPointEntity.getRecordId());
                //Log.e("longitude",String.valueOf(marker.getPosition().longitude));
                //Log.e("latitude",String.valueOf(marker.getPosition().latitude));
                View infoWindow_view = getLayoutInflater().inflate(R.layout.map_pop_window, null);

                ImageView recordCoverImage = infoWindow_view.findViewById(R.id.map_cover_image);
                TextView recordLikeNum = infoWindow_view.findViewById(R.id.map_record_likeNum);
                TextView recordName = infoWindow_view.findViewById(R.id.map_record_name);
                Picasso.with(getActivity())
                        .load(mapPointEntity.getRecordCoverImage())
                        .into(recordCoverImage);
                recordLikeNum.setText(mapPointEntity.getLikeNum());
                recordName.setText(mapPointEntity.getRecordName());

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
                InfoWindow.OnInfoWindowClickListener infoWindow_ClickListener = new InfoWindow.OnInfoWindowClickListener() {

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
        LatLng point = new LatLng( altitude, longtitude);
        final View markerView = LayoutInflater.from(getContext()).inflate(R.layout.map_loc,null);
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
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private int getMarkerIndex(double longitude, double latitude) {
        int index = 0;
        for (int i = 0;i < list.size();i++) {
            if (list.get(i).getLatitude() == latitude && list.get(i).getLongitude() == longitude) {
                index  = i;
                break;
            }
        }
        //Log.e("mapPoing",String.valueOf(index));
        return index;
    }

}