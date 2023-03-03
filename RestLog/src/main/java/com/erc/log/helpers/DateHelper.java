package com.erc.log.helpers;
import android.util.Log;

import com.erc.dal.Util;
import com.erc.log.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by einar on 10/11/2015.
 */
public class DateHelper {
    public static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT = "yyyy-MM-dd' 'HH:mm:ss";
    public static final String FORMAT_DATE = "yyyy-MM-dd";

    public static Date getDateFromFormat(String dateInFormat, String format) {

        if (!Util.isNullOrEmpty(dateInFormat)) {
            try {
                DateFormat df1 = new SimpleDateFormat(format);
                Date result1 = df1.parse(dateInFormat);
                return result1;
            } catch (Exception e) {
                Log.e(Constants.TAG, "Error on getDateFromFormat", e);
            }
        }
        return null;
    }

    public static String getDateWithFormat(Long date, String format) {
        return getDateWithFormat(new Date(date), format);
    }

    public static String getDateWithFormat(Date date, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        } catch (Exception e) {
            Log.e(Constants.TAG,"Error on getDateWithFormat", e);
        }
        return null;
    }

    public static Calendar getEmptyTodaysCalendar() {
        return setTime(new Date(), 0, 0, 0, 0);
    }

    public static Calendar setTime(final Date date, final int hourOfDay, final int minute, final int second, final int ms) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
        now.set(Calendar.MINUTE, minute);
        now.set(Calendar.SECOND, second);
        now.set(Calendar.MILLISECOND, ms);

        return now;
    }
}
