package com.gmail.wq423.weather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 13521838583@163.com on 2018-4-22.
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String addr, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(addr).build();
        client.newCall(request).enqueue(callback);
    }
}
