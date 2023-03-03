package com.erc.log.model;

import com.erc.dal.ExpresionOperator;
import com.erc.dal.Options;
import com.erc.log.DataBase;
import com.erc.log.containers.FILE;
import com.erc.log.helpers.DateHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class FilesModel {


    public static void addFile(String fullPath) {
        FILE file = new FILE();
        file.fullPath = fullPath;
        file.date = System.currentTimeMillis();
        DataBase.getInstance().save(file);
    }

    public static void deleteFile(long id) {
        DataBase.getInstance().remove(FILE.class, id);
    }

    public static ArrayList<FILE> getFilesToMove() {
        Calendar calendar = DateHelper.getEmptyTodaysCalendar();
        calendar.add(Calendar.HOUR, 0);
        calendar.add(Calendar.MINUTE, 0);
        calendar.add(Calendar.SECOND, 0);
        calendar.add(Calendar.MILLISECOND, 0);
        Options options = new Options();
        options.and("date", calendar.getTimeInMillis(), ExpresionOperator.LESS_THAN);
        return DataBase.getInstance().getAll(FILE.class, options);
    }

}
