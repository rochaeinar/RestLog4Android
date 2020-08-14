package com.erc.log.appenders;

import com.erc.log.AppContext;
import com.erc.log.configuration.LogConfiguration;
import com.erc.log.containers.LOG;

import java.util.ArrayList;

public class AvailableAppenders {

    public void append(LOG log) {
        ArrayList<BaseAppender> appenders = LogConfiguration.getInstance(AppContext.getContext()).getAppenders();

        for (Appender appender : appenders) {
            appender.append(log);
        }
    }
}
