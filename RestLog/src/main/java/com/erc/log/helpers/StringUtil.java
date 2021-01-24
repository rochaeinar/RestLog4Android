package com.erc.log.helpers;

public class StringUtil {
    public static boolean isNullOrEmpty(String text) {
        return (text == null || text.trim().equals("null") || text.trim().length() <= 0);
    }

    public static String format(String string, Object... params) {
        return String.format(string.replaceAll("\\{[0-9]\\}", "%s"), params);
    }

    public static String removeExtension(String text) {
        if (text.indexOf(".") > 0)
            text = text.substring(0, text.lastIndexOf("."));
        return text;
    }
}
