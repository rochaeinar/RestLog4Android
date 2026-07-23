package com.erc.log.appenders;

import com.erc.log.containers.LOG;
import com.erc.log.format.LogFormatter;

public class ConsoleAppender extends BaseAppender {

    public ConsoleAppender() {
    }

    @Override
    public void append(LOG log) {
        System.out.println(LogFormatter.compactLine(log));
    }
}
