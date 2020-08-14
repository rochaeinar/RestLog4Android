package com.erc.log.http;

public enum StatusCode {
    OK(200),
    CREATED(201);

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int value() {
        return code;
    }
}