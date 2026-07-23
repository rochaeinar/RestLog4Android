package com.erc.log;

import com.erc.log.async.AsyncLogDispatcher;
import com.erc.log.configuration.FilterValidator;
import com.erc.log.configuration.Level;
import com.erc.log.configuration.LogConfiguration;
import com.erc.log.containers.LOG;
import com.erc.log.helpers.FileHelper;
import com.erc.log.helpers.StringUtil;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Log {

    public static void e(String tag, String message) {
        addLog(Level.ERROR, message, tag);
    }

    public static void w(String tag, String message) {
        addLog(Level.WARN, message, tag);
    }

    public static void i(String tag, String message) {
        addLog(Level.INFO, message, tag);
    }

    public static void d(String tag, String message) {
        addLog(Level.DEBUG, message, tag);
    }

    public static void v(String tag, String message) {
        addLog(Level.VERBOSE, message, tag);
    }

    public static void e(String tag, String message, Exception exception) {
        addLog(Level.ERROR, handleException(message, exception), tag);
    }

    public static void w(String tag, String message, Exception exception) {
        addLog(Level.WARN, handleException(message, exception), tag);
    }

    public static void i(String tag, String message, Exception exception) {
        addLog(Level.INFO, handleException(message, exception), tag);
    }

    public static void d(String tag, String message, Exception exception) {
        addLog(Level.DEBUG, handleException(message, exception), tag);
    }

    public static void v(String tag, String message, Exception exception) {
        addLog(Level.VERBOSE, handleException(message, exception), tag);
    }

    public static void e(String message) {
        addLog(Level.ERROR, message);
    }

    public static void w(String message) {
        addLog(Level.WARN, message);
    }

    public static void i(String message) {
        addLog(Level.INFO, message);
    }

    public static void d(String message) {
        addLog(Level.DEBUG, message);
    }

    public static void v(String message) {
        addLog(Level.VERBOSE, message);
    }

    public static void e(String message, Exception exception) {
        addLog(Level.ERROR, handleException(message, exception));
    }

    public static void w(String message, Exception exception) {
        addLog(Level.WARN, handleException(message, exception));
    }

    public static void i(String message, Exception exception) {
        addLog(Level.INFO, handleException(message, exception));
    }

    public static void d(String message, Exception exception) {
        addLog(Level.DEBUG, handleException(message, exception));
    }

    public static void v(String message, Exception exception) {
        addLog(Level.VERBOSE, handleException(message, exception));
    }

    public static void e(String tag, String message, Object... parameters) {
        addLog(Level.ERROR, StringUtil.format(message, parameters), tag);
    }

    public static void w(String tag, String message, Object... parameters) {
        addLog(Level.WARN, StringUtil.format(message, parameters), tag);
    }

    public static void i(String tag, String message, Object... parameters) {
        addLog(Level.INFO, StringUtil.format(message, parameters), tag);
    }

    public static void d(String tag, String message, Object... parameters) {
        addLog(Level.DEBUG, StringUtil.format(message, parameters), tag);
    }

    public static void v(String tag, String message, Object... parameters) {
        addLog(Level.VERBOSE, StringUtil.format(message, parameters), tag);
    }

    private static void addLog(Level level, String message, String... tag) {
        LogConfiguration configuration = LogConfiguration.getInstance(AppContext.getContext());

        // Cheap, in-memory checks run on the caller thread so we never build/queue logs
        // that would be discarded anyway.
        if (!configuration.isEnabled() || !isValidLevel(level)) {
            return;
        }

        String tag_ = tag.length > 0 ? tag[0] : getTag();
        LOG log = new LOG(level, tag_, message);

        if (!FilterValidator.isValidFilter(log)) {
            return;
        }

        // Everything that touches the database (daily-record cap, de-duplication and the
        // actual write) happens off the caller/UI thread inside the dispatcher. Under
        // extreme load the dispatcher drops the oldest pending logs instead of blocking,
        // so the host app can never be stalled or killed by a logging storm.
        AsyncLogDispatcher.getInstance().enqueue(log);
    }

    private static String getTag() {
        String tagFromConfiguration = LogConfiguration.getInstance(AppContext.getContext()).getTag();
        if (StringUtil.isNullOrEmpty(tagFromConfiguration)) {
            return FileHelper.getExtension(AppContext.getContext().getPackageName());
        } else {
            return tagFromConfiguration;
        }
    }

    private static boolean isValidLevel(Level level) {
        int currentLevelConfiguration = LogConfiguration.getInstance(AppContext.getContext()).getLevel().value();
        return currentLevelConfiguration >= level.value();
    }

    public static String handleException(String message, Exception exception) {

        if (exception == null) {
            return message;
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);

        message = "[E]" + message +
                ",\tExceptionType:" + exception.getClass().getName() +
                ",\tMessage:" + exception.getMessage() +
                ",\tLocalizedMessage:" + exception.getLocalizedMessage() +
                ",\tToString:" + exception.toString() +
                ",\tStackTrace:" + sw.toString();
        return message;
    }
}
