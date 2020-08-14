package com.erc.log.configuration;

import java.util.HashMap;
import java.util.Map;

public enum Level {
    ERROR(2),
    WARN(3),
    INFO(4),
    DEBUG(5),
    VERBOSE(6);

    private int code;

    Level(int code) {
        this.code = code;
    }

    public int value() {
        return code;
    }

    private static final Map<Integer, Level> typesByValue = new HashMap<>();

    static {
        for (Level level : Level.values()) {
            typesByValue.put(level.code, level);
        }
    }

    public static Level fromValue(int value) {
        return typesByValue.get(value);
    }
}
