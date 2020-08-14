package com.erc.log.http;

import com.erc.log.AppContext;
import com.erc.log.appenders.AppenderType;
import com.erc.log.appenders.RestAppender;
import com.erc.log.configuration.LogConfiguration;

public class HostConfiguration {
    private static String host = null;

    public static String getHost() {
        if (host == null) {
            host = getHostName();
        }
        return host;
    }

    private HostConfiguration() {

    }

    private static String getHostName() {
        String host = ((RestAppender) LogConfiguration.getInstance(AppContext.getContext()).getAppender(AppenderType.REST)).getHost();
        if (!host.endsWith("/")) {
            host = host + "/";
        }
        return host;
    }


}
