package com.erc.log.http;

import com.erc.log.containers.LOG;

import java.util.ArrayList;

public class LogResource extends Resource<LOG> {

    private static String route = "log";

    public LogResource() {
        super(route);
    }

    @Override
    public ArrayList<LOG> getAll() {
        return super.getAll();
    }

    @Override
    public LOG post(LOG log) {
        return super.post(log);
    }
}
