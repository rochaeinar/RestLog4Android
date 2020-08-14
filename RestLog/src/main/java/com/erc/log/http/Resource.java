package com.erc.log.http;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class Resource<T> implements CrudOperation<T> {
    private String route;
    private Gson gson;
    private Class<T> classType;
    public static String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public Resource(String route) {
        this.gson = new GsonBuilder().setDateFormat(dateFormat).create();
        this.route = route;
        this.classType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public ArrayList<T> getAll() {
        ArrayList<T> items = null;
        try {
            String uri = Uri.parse(this.route)
                    .buildUpon()
                    .build().toString();

            JSONObject jsonObject = HttpRequest.get(uri);
            if (jsonObject != null) {
                items = gson.fromJson(
                        jsonObject.getJSONArray("data").toString(),
                        new ListOfSomething<T>(this.classType)
                );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public T post(T item) {
        T res = null;
        try {
            String uri = Uri.parse(this.route)
                    .buildUpon()
                    .build().toString();

            Gson gson = new Gson();
            String body = gson.toJson(item);
            JSONObject jsonObject = HttpRequest.post(uri, body);
            if (jsonObject != null) {
                res = gson.fromJson(jsonObject.getJSONObject("data").toString(), classType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    private static class ListOfSomething<X> implements ParameterizedType {

        private Class<?> wrapped;

        public ListOfSomething(Class<X> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{wrapped};
        }

        @Override
        public Type getRawType() {
            return ArrayList.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

    }
}
