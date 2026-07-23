package com.erc.log.format;

import com.erc.log.configuration.Level;
import com.erc.log.containers.LOG;
import com.erc.log.helpers.DateHelper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Single place that decides what part of a {@link LOG} is <b>constant</b> (device / app
 * context, written once) and what is <b>variable</b> (written on every line), and renders
 * both in a readable way for the text-based appenders (TXT, CSV, JSON, XML, console).
 * <p>
 * The goal is to stop repeating the ~15 unchanging device fields on every single log line.
 */
public final class LogFormatter {

    /** Column titles for the variable part, used by the CSV header row. */
    public static final String[] VARIABLE_COLUMNS = {
            "time", "level", "tag", "message", "count",
            "ramUsage", "storageFree", "battery", "charging",
            "networkType", "networkState", "screen", "orientation"
    };

    private LogFormatter() {
    }

    // ------------------------------------------------------------------
    // Constant context (written once per file / session)
    // ------------------------------------------------------------------

    /** Human-readable header block with the constant device/app info. Lines start with '#'. */
    public static String header(LOG log) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ---- RestLog · session ------------------------------\n");
        // The date is constant within a (daily) file, so it is written once here and each
        // log line below shows only the time.
        sb.append("# date          : ").append(dateOnly(log)).append('\n');
        sb.append("# device        : ").append(nn(log.brand)).append(' ').append(nn(log.model))
                .append(" (").append(nn(log.manufacturer)).append(")\n");
        sb.append("# android       : ").append(nn(log.androidVersion))
                .append("   rooted=").append(nn(log.rootedState)).append('\n');
        sb.append("# app           : ").append(nn(log.appName))
                .append(" [").append(nn(log.packageName)).append("] v").append(nn(log.version)).append('\n');
        sb.append("# deviceId      : ").append(nn(log.deviceId)).append('\n');
        sb.append("# screen        : ").append(nn(log.density)).append(' ').append(nn(log.dpi))
                .append(' ').append(nn(log.resolution)).append('\n');
        sb.append("# locale        : ").append(nn(log.locale)).append('\n');
        sb.append("# ram total     : ").append(nn(log.totalMemoryRam)).append('\n');
        sb.append("# storage total : ").append(nn(log.totalInternalMemory)).append('\n');
        sb.append("# line          : time [level] tag  message  [bat net scr ram free orientation]\n");
        sb.append("# -----------------------------------------------------");
        return sb.toString();
    }

    /** Constant info as an ordered map (for the JSON "device" object / XML block). */
    public static Map<String, Object> deviceMap(LOG log) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("date", dateOnly(log));
        m.put("deviceId", nn(log.deviceId));
        m.put("packageName", nn(log.packageName));
        m.put("appName", nn(log.appName));
        m.put("version", nn(log.version));
        m.put("manufacturer", nn(log.manufacturer));
        m.put("brand", nn(log.brand));
        m.put("model", nn(log.model));
        m.put("androidVersion", nn(log.androidVersion));
        m.put("rooted", nn(log.rootedState));
        m.put("locale", nn(log.locale));
        m.put("density", nn(log.density));
        m.put("dpi", nn(log.dpi));
        m.put("resolution", nn(log.resolution));
        m.put("totalMemoryRam", nn(log.totalMemoryRam));
        m.put("totalInternalMemory", nn(log.totalInternalMemory));
        return m;
    }

    // ------------------------------------------------------------------
    // Variable part (written per log)
    // ------------------------------------------------------------------

    /** Compact, readable one-liner for TXT / console output (time only; the date is in the header). */
    public static String compactLine(LOG log) {
        return time(log) +
                "  " + levelTag(log) +
                "  " + nn(log.tag) +
                "  " + nn(log.message) +
                "  " + contextSummary(log);
    }

    /**
     * Ultra-compact trailing context: battery (with '+' when charging), network type (with
     * its state only when not online), screen, ram usage, free storage and orientation.
     * e.g. [100%+ wifi off 59% 1,882MB portrait]  or  [80% wifi/offline on 60% 900MB landscape]
     */
    private static String contextSummary(LOG log) {
        StringBuilder sb = new StringBuilder("[");
        sb.append(nn(log.batteryLevel));
        if (isTrue(log.chargingState)) {
            sb.append('+');
        }
        sb.append(' ').append(nn(log.networkType));
        if (!isOnline(log.networkState)) {
            sb.append('/').append(nn(log.networkState));
        }
        sb.append(' ').append(nn(log.activeScreen));
        sb.append(' ').append(nn(log.ramMemoryUsage));
        sb.append(' ').append(nn(log.internalMemoryAvailable));
        sb.append(' ').append(nn(log.orientation));
        sb.append(']');
        return sb.toString();
    }

    /** CSV header row: variable column titles (written once). */
    public static String csvHeader() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < VARIABLE_COLUMNS.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(VARIABLE_COLUMNS[i]);
        }
        return sb.toString();
    }

    /** CSV data row for a log, with every field escaped (message commas/quotes are safe). */
    public static String csvRow(LOG log) {
        String[] values = {
                time(log), levelTag(log), nn(log.tag), nn(log.message), String.valueOf(log.count),
                nn(log.ramMemoryUsage), nn(log.internalMemoryAvailable), nn(log.batteryLevel), nn(log.chargingState),
                nn(log.networkType), nn(log.networkState), nn(log.activeScreen), nn(log.orientation)
        };
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(csvEscape(values[i]));
        }
        return sb.toString();
    }

    /** Variable info as an ordered map (for the JSON log entry / XML element). */
    public static Map<String, Object> logMap(LOG log) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("time", time(log));
        m.put("level", levelTag(log));
        m.put("tag", nn(log.tag));
        m.put("message", nn(log.message));
        m.put("count", log.count);
        m.put("ramUsage", nn(log.ramMemoryUsage));
        m.put("storageFree", nn(log.internalMemoryAvailable));
        m.put("battery", nn(log.batteryLevel));
        m.put("charging", nn(log.chargingState));
        m.put("networkType", nn(log.networkType));
        m.put("networkState", nn(log.networkState));
        m.put("screen", nn(log.activeScreen));
        m.put("orientation", nn(log.orientation));
        return m;
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    /** Escapes a value for CSV (RFC-4180): wraps in quotes and doubles inner quotes when needed. */
    public static String csvEscape(String value) {
        if (value == null) {
            return "";
        }
        boolean needsQuoting = value.indexOf(',') >= 0
                || value.indexOf('"') >= 0
                || value.indexOf('\n') >= 0
                || value.indexOf('\r') >= 0;
        if (!needsQuoting) {
            return value;
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private static String time(LOG log) {
        return DateHelper.getDateWithFormat(log.date, DateHelper.FORMAT_TIME);
    }

    private static String dateOnly(LOG log) {
        return DateHelper.getDateWithFormat(log.date, DateHelper.FORMAT_DATE);
    }

    /** Single-letter level in brackets: [E], [W], [I], [D], [V] (the level names start with a unique letter). */
    private static String levelTag(LOG log) {
        Level level = Level.fromValue(log.level);
        String name = level != null ? level.toString() : String.valueOf(log.level);
        return name.isEmpty() ? "[?]" : "[" + name.charAt(0) + "]";
    }

    private static boolean isTrue(String value) {
        return value != null && value.equalsIgnoreCase("true");
    }

    private static boolean isOnline(String networkState) {
        return networkState != null && networkState.equalsIgnoreCase("online");
    }

    private static String nn(String value) {
        return value == null ? "" : value;
    }
}
