package com.erc.log;

import com.erc.log.appenders.AvailableAppenders;
import com.erc.log.configuration.FilterValidator;
import com.erc.log.configuration.Level;
import com.erc.log.configuration.LogConfiguration;
import com.erc.log.containers.LOG;
import com.erc.log.helpers.StringUtil;
import com.erc.log.model.LogModel;
import com.erc.log.services.CleaningJobIntentService;

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

    public static void e(String tag, String message, String... parameters) {
        addLog(Level.ERROR, StringUtil.format(message, parameters), tag);
    }

    public static void w(String tag, String message, String... parameters) {
        addLog(Level.WARN, StringUtil.format(message, parameters), tag);
    }

    public static void i(String tag, String message, String... parameters) {
        addLog(Level.INFO, StringUtil.format(message, parameters), tag);
    }

    public static void d(String tag, String message, String... parameters) {
        addLog(Level.DEBUG, StringUtil.format(message, parameters), tag);
    }

    public static void v(String tag, String message, String... parameters) {
        addLog(Level.VERBOSE, StringUtil.format(message, parameters), tag);
    }

    private static void addLog(Level level, String message, String tag) {
        AvailableAppenders availableAppenders = new AvailableAppenders();
        LOG log = new LOG(level, tag, message);
        if (LogConfiguration.getInstance(AppContext.getContext()).isEnabled()
                && isValidLevel(level)
                && isValidNumberOfRecordsByDay()
                && FilterValidator.isValidFilter(log)) {

            if (LogConfiguration.getInstance(AppContext.getContext()).isAvoidDuplicated()) {
                LOG existentLog = LogModel.getLog(log);
                if (existentLog != null) {
                    existentLog.count++;
                    availableAppenders.append(existentLog);
                } else {
                    availableAppenders.append(log);
                }
            } else {
                availableAppenders.append(log);
            }
        }
        CleaningJobIntentService.clean(AppContext.getContext());
    }

    private static boolean isValidNumberOfRecordsByDay() {
        long maxRecordNumber = LogConfiguration.getInstance(AppContext.getContext()).getMaxRecordNumber();
        long currentRecords = LogModel.getDailyRecordsCount();
        return maxRecordNumber == 0 || (currentRecords < maxRecordNumber);
    }

    private static boolean isValidLevel(Level level) {
        int currentLevelConfiguration = LogConfiguration.getInstance(AppContext.getContext()).getLevel().value();
        return currentLevelConfiguration >= level.value();
    }
}
