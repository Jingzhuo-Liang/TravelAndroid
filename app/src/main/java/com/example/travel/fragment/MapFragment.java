package com.example.travel.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.travel.R;
import com.example.travel.api.Api;
import com.example.travel.api.ApiConfig;
import com.example.travel.api.TtitCallback;
import com.example.travel.entity.MapPointEntity;
import com.example.travel.entity.MapPointResponse;
import com.example.travel.util.LoginUser;
import com.example.travel.util.TimeUtils;
import com.example.travel.widget.BaiduZoomControl;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapFragment extends BaseFragment implements View.OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    List<MapPointEntity> list;
    private String userId;
    private BaiduZoomControl baiduZoomControl;

    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:{
                    //?????????????????????
                    showData();
                    break;
                }
                default:{

                }
            }
        }
    };

    public static MapFragment newInstance(String userId) {
        MapFragment fragment = new MapFragment();
        fragment.userId = userId;
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
        mMapView.showZoomControls(false);
        baiduZoomControl = mRootView.findViewById(R.id.baidu_zoom_control);
    }


    @Override
    protected void initData() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        getMapPoints();
        baiduZoomControl.getRefresh_icon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtils.isFastDoubleClick()) {
                    return;
                }
                getMapPoints();
            }
        });
        baiduZoomControl.getZoomIn_icon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomIn());
            }
        });
        baiduZoomControl.getZoomOut_icon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomOut());
            }
        });
    }

    private void showData(){
        //TODO:???????????????????????????
        //create_map_point(39.91923, 116.387428);
        //create_map_point(49.91923, 110.387428);
        //create_map_point(19.91923, 16.387428);
        mBaiduMap.clear();
        for (MapPointEntity mpe: list) {
            //Log.e("mapPoints",mpe.getRecordId());
            create_map_point(mpe.getLongitude(),mpe.getLatitude());
        }
        showInfoWindow();
    }

    private void getMapPoints() {
        HashMap<String , Object> params = new HashMap<>();
        params.put("userId",userId);
        Api.config(ApiConfig.GET_MAP,params).getRequest(new TtitCallback() {
            @Override
            public void onSuccess(String res) {
                //Log.e("getMap",res);
                Gson gson = new Gson();
                MapPointResponse mr = gson.fromJson(res, MapPointResponse.class);
                if (mr.getCode() == 200) {
                    list = mr.getData();
                    //Log.e("mapPoints",String.valueOf(list.size()));
                    handler.sendEmptyMessage(0);
                } else {
                    //showToastSync("");
                }
            }

            @Override
            public void onFailure(Exception e) {
                //showToastSync("");
            }
        });

        /*
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

         */
    }

    private void showInfoWindow(){
        /*
        ????????????
         */
        BaiduMap.OnMarkerClickListener marker_Listener = new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //??????????????????????????????????????????????????????????????????
                mBaiduMap.hideInfoWindow();
                //??????maker??????????????????
                MapPointEntity mapPointEntity = list.get(getMarkerIndex(marker.getPosition().longitude,marker.getPosition().latitude));
                //Log.e("positionIndex",);
                //TODO:????????????????????????????????????xml
                //???????????????
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
                //???????????????????????????
                InfoWindow.OnInfoWindowClickListener infoWindow_ClickListener = new InfoWindow.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick() {
                        // TODO Auto-generated method stub
                        mBaiduMap.hideInfoWindow();

                    }
                };
                //???????????????
                InfoWindow infoWindow = new InfoWindow(infoWindow_bitmap,//???????????????
                        marker.getPosition(), //???????????????
                        100, //??????????????????????????????
                        //infoWindow?????????
                        infoWindow_ClickListener
                );
                //???????????????
                mBaiduMap.showInfoWindow(infoWindow);
                return true;
            }
        };
        mBaiduMap.setOnMarkerClickListener(marker_Listener);
    }

    private void create_map_point(double longtitude, double altitude){
        /*
        ???????????????????????????
         */
        //??????Maker?????????
        LatLng point = new LatLng( altitude, longtitude);
        final View markerView = LayoutInflater.from(getContext()).inflate(R.layout.map_loc,null);
        Bitmap bitmap = getViewBitmap(markerView);
        BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bitmap);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(point)//Marker?????????
                .icon(bd);
        //??????????????????Marker????????????
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
        //???activity??????onDestroy?????????mMapView.onDestroy()?????????????????????????????????
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //???activity??????onResume?????????mMapView. onResume ()?????????????????????????????????
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //???activity??????onPause?????????mMapView. onPause ()?????????????????????????????????
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default: {
                break;
            }
        }
    }
}