package com.erc.log.appenders;

import com.erc.log.containers.LOG;

public interface Appender {
    void append(LOG log);

    void setType(AppenderType type);

    AppenderType getType();
}
