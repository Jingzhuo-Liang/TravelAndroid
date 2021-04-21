package com.example.travel.api;

import android.util.Log;

import com.example.travel.util.StringUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @@author:ljz
 * @@date:2021/4/14,10:46
 * @@version:1.0
 * @@annotation:
 **/
public class Api {

    private static String requestUrl;
    private static HashMap<String, Object> mParams;
    private static OkHttpClient client;

    public static Api api = new Api();

    public Api() {}

    public static Api config(String url, HashMap<String, Object> params) {
        client = new OkHttpClient.Builder().build();
        requestUrl = ApiConfig.BASE_URL + url;
        mParams = params;
        return api;
    }

    public static void postRequest(TtitCallback ttitCallback) {
        JSONObject jsonObject = new JSONObject(mParams);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson = RequestBody.create(
                MediaType.parse("application/json;charset=utf-8"),jsonStr);

        //Log.e("PostRequest:",requestUrl);
        // 第三步 创建Request
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("contentType","application/json;charset=UTF-8")
                .post(requestBodyJson)
                .build();
        // 第四步 创建Call回调对象
        final Call call = client.newCall(request);
        // 第五步 发起请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure",e.getMessage());
                ttitCallback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result  = response.body().string();
               ttitCallback.onSuccess(result);
            }
        });
    }
    //private static int num = 0;
    public void getRequest(final TtitCallback callback) {
        //Log.e("getRequest",":" + num++);
        String url = getAppendUrl(requestUrl, mParams);
        //Log.e("getRequest:",url);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure",e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                //Log.e("onResponse",result);
                callback.onSuccess(result);
            }
        });
    }

    public String getAppendUrl(String url, Map<String , Object> map) {
        if (map != null && !map.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (StringUtils.isEmpty(buffer.toString())) {
                    buffer.append("?");
                }
                else {
                    buffer.append("&");
                }
                buffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
            return url + buffer.toString();
        }
        return url;
    }


}
