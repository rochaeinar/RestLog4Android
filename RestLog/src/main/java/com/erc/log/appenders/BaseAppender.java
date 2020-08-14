package com.erc.log.appenders;

import com.erc.log.containers.LOG;

public class BaseAppender implements Appender {

    protected AppenderType type;

    public BaseAppender() {
    }

    @Override
    public void append(LOG log) {
    }

    @Override
    public void setType(AppenderType type) {
        this.type = type;
    }

    @Override
    public AppenderType getType() {
        return this.type;
    }
}
