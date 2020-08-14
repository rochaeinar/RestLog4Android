package com.erc.log.helpers;

public enum DisplayStatus {
    ON("on"),
    OFF("off");

    private final String code;

    DisplayStatus(String code) {
        this.code = code;
    }

    public String value() {
        return code;
    }
}