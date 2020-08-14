package com.erc.log.model;

import com.erc.dal.Aggregation;
import com.erc.dal.ExpresionOperator;
import com.erc.dal.Options;
import com.erc.log.AppContext;
import com.erc.log.DataBase;
import com.erc.log.configuration.LogConfiguration;
import com.erc.log.containers.FILE;
import com.erc.log.containers.LOG;
import com.erc.log.helpers.DateHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class LogModel {


    public static void addLog(LOG log) {
        DataBase.getInstance().save(log);
    }

    public static void delete(long id) {
        DataBase.getInstance().remove(LOG.class, id);
    }

    public static LOG getLog(LOG log) {
        Options options = new Options();
        options.and("level", log.level, ExpresionOperator.EQUALS);
        options.and("message", log.message, ExpresionOperator.EQUALS);
        options.and("tag", log.tag, ExpresionOperator.EQUALS);
        ArrayList<LOG> logs = DataBase.getInstance().getAll(LOG.class, options);
        if (logs.size() > 0) {
            return logs.get(0);
        }
        return null;
    }

    public static void deleteAll() {
        DataBase.getInstance().execSQL("DELETE FROM LOG");
    }

    public static ArrayList<LOG> getLogsToDelete() {
        int deleteAfter = LogConfiguration.getInstance(AppContext.getContext()).getDeleteAfter();
        if (deleteAfter > 0) {
            Calendar calendar = DateHelper.getEmptyTodaysCalendar();
            calendar.add(Calendar.DAY_OF_MONTH, deleteAfter * -1);

            Options options = new Options();
            options.and("date", calendar.getTimeInMillis(), ExpresionOperator.LESS_THAN);
            return DataBase.getInstance().getAll(LOG.class, options);
        }
        return new ArrayList<>();
    }

    public static long getDailyRecordsCount() {
        Options options = new Options();
        options.and("date", DateHelper.getEmptyTodaysCalendar().getTimeInMillis(), ExpresionOperator.GREATER_THAN_OR_EQUAL_TO);

        return DataBase.getInstance().calculate(LOG.class, Aggregation.count(), options);
    }
}
