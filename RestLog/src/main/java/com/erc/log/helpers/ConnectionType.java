package com.erc.log.helpers;

public enum ConnectionType {
    NONE("none"),
    CELLULAR("cellular"),
    WIFI("wifi");

    private final String code;

    ConnectionType(String code) {
        this.code = code;
    }

    public String value() {
        return code;
    }
}