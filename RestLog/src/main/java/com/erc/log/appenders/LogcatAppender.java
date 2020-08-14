package com.erc.log.appenders;

import android.util.Log;

import com.erc.log.configuration.Level;
import com.erc.log.containers.LOG;

import static com.erc.log.configuration.Level.*;

public class LogcatAppender extends BaseAppender {

    public LogcatAppender() {
    }

    @Override
    public void append(LOG log) {
        switch (Level.fromValue(log.level)) {
            case ERROR:
                Log.e(log.tag, log.message);
                break;
            case WARN:
                Log.w(log.tag, log.message);
                break;
            case INFO:
                Log.i(log.tag, log.message);
                break;
            case DEBUG:
                Log.d(log.tag, log.message);
                break;
            case VERBOSE:
                Log.v(log.tag, log.message);
                break;
        }
    }
}
