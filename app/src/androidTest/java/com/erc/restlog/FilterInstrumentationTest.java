package com.erc.restlog;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.erc.log.DataBase;
import com.erc.log.Log;
import com.erc.log.configuration.Filter;
import com.erc.log.configuration.FilterOperator;
import com.erc.log.configuration.Level;
import com.erc.log.configuration.LogConfiguration;
import com.erc.log.containers.LOG;
import com.erc.log.helpers.Preferences;
import com.erc.log.model.LogModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static com.erc.log.model.LogModel.*;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class FilterInstrumentationTest {


    @Before
    @After
    public void clear() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SharedPreferences sharedPreferences =
                appContext.getSharedPreferences(Preferences.SETTINGS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        LogModel.deleteAll();
        LogConfiguration.clear();
    }

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.erc.log", appContext.getPackageName());
    }

    @Test
    public void filter_valid_string() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Filter filter = new Filter();
        filter.setFilterOperator(FilterOperator.EQUALS);
        filter.setField("message");
        filter.setValue("test");

        LogConfiguration.getInstance(appContext).addFilter(filter);

        Log.w("RestLog", "test");

        assertEquals(1, getDailyRecordsCount());
    }

    @Test
    public void filter_invalid() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Filter filter = new Filter();
        filter.setFilterOperator(FilterOperator.EQUALS);
        filter.setField("message");
        filter.setValue("test1");

        LogConfiguration.getInstance(appContext).addFilter(filter);

        Log.w("RestLog", "test");

        LOG log = new LOG();
        log.message = "test1";
        log.tag = "RestLog";
        log.level = Level.WARN.value();

        assertEquals(0, getDailyRecordsCount());
    }

    @Test
    public void filter_valid_int() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Filter filter = new Filter();
        filter.setFilterOperator(FilterOperator.GREATER_THAN);
        filter.setField("count");
        filter.setValue("0");

        LogConfiguration.getInstance(appContext).addFilter(filter);

        Log.w("RestLog", "test");

        assertEquals(1, getDailyRecordsCount());
    }

    @Test
    public void level_valid() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        LogConfiguration.getInstance(appContext).setFilters(new ArrayList<Filter>());
        Log.w("RestLog", "test");

        assertEquals(1, getDailyRecordsCount());
    }

    @Test
    public void level_invalid() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        LogConfiguration.getInstance(appContext).setLevel(Level.ERROR);
        Log.w("RestLog", "test");

        assertEquals(0, getDailyRecordsCount());
    }


}
