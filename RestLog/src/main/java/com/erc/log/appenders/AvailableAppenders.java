package com.erc.log.appenders;

import com.erc.log.AppContext;
import com.erc.log.configuration.LogConfiguration;
import com.erc.log.containers.LOG;

import java.util.ArrayList;
import java.util.List;

public class AvailableAppenders {

    public void append(LOG log) {
        for (BaseAppender appender : resolveAppenders()) {
            appender.append(log);
        }
    }

    /**
     * Dispatches a whole batch to every configured appender. Appenders that support bulk
     * writes persist the batch in one shot; the rest fall back to per-log appends.
     */
    public void append(List<LOG> logs) {
        if (logs == null || logs.isEmpty()) {
            return;
        }
        for (BaseAppender appender : resolveAppenders()) {
            appender.append(logs);
        }
    }

    private ArrayList<BaseAppender> resolveAppenders() {
        ArrayList<BaseAppender> appenders = LogConfiguration.getInstance(AppContext.getContext()).getAppenders();
        if (appenders.size() == 0) {
            appenders = new ArrayList<>();
            appenders.add(new LogcatAppender());
        }
        return appenders;
    }
}
