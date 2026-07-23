package com.erc.log.appenders;

import com.erc.log.containers.LOG;

import java.util.List;

public class BaseAppender implements Appender {

    protected AppenderType type;

    public BaseAppender() {
    }

    @Override
    public void append(LOG log) {
    }

    /**
     * Default batch behaviour: append each log one by one. Appenders that can persist
     * many rows more efficiently (e.g. within a single DB transaction) should override
     * this method.
     */
    @Override
    public void append(List<LOG> logs) {
        for (LOG log : logs) {
            append(log);
        }
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
