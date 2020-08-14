package com.erc.log.helpers;

import java.text.MessageFormat;

public class StringUtil {
    public static boolean isNullOrEmpty(String text) {
        return text == null || text.equals("");
    }

    public static String format(String string, String...params) {
        return MessageFormat.format(string, params);
    }
}
