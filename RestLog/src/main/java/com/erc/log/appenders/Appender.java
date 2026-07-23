package com.erc.log.appenders;

import com.erc.log.containers.LOG;

import java.util.List;

public interface Appender {
    void append(LOG log);

    /**
     * Appends a batch of logs at once. Appenders that support bulk writes (e.g. a
     * database appender) can override this to persist the whole batch in a single
     * transaction. The default implementation loops over {@link #append(LOG)}.
     */
    void append(List<LOG> logs);

    void setType(AppenderType type);

    AppenderType getType();
}
