package com.erc.log.http;


import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

/**
 * Created by einar on 2/15/2019.
 */
public class HttpRequest {

    private static String TAG = "http";

    public static JSONObject get(String route) {
        return handleResponse(getResponse(route, MethodRequest.GET), MethodRequest.GET);
    }

    public static JSONObject post(String route, String body) {
        return handleResponse(getResponse(route, MethodRequest.POST, body), MethodRequest.POST);
    }

    private static Response getResponse(String route, MethodRequest methodRequest, String... body) {
        String body_ = body.length > 0 ? body[0] : null;
        Response response = null;
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = getRequest(methodRequest, route, body_);
            response = client.newCall(request).execute();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return response;
    }

    private static Request getRequest(MethodRequest methodRequest, String route, String... body) {
        String body_ = body.length > 0 ? body[0] : null;
        Request request = null;
        switch (methodRequest) {
            case GET:
                request = new Request.Builder()
                        .url(HostConfiguration.getHost() + route)
                        .build();
                break;
            case POST:
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody requestBody = RequestBody.create(JSON, body_);
                request = new Request.Builder()
                        .url(HostConfiguration.getHost() + route)
                        .post(requestBody)
                        .build();
                break;
        }
        return request;
    }

    private static JSONObject handleResponse(Response response, MethodRequest methodRequest) {
        JSONObject jsonObject = null;
        try {
            if (response.code() == StatusCode.OK.value() || response.code() == StatusCode.CREATED.value()) {
                String responseString = response.body().string();
                Log.i(TAG, responseString);
                jsonObject = new JSONObject(responseString);
            } else {
                Log.w(TAG, response.request().method() + ": REQUEST: " + response.request());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }
}
