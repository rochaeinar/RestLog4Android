package com.erc.log;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.erc.log.configuration.Filter;
import com.erc.log.configuration.FilterOperator;
import com.erc.log.helpers.Preferences;
import com.erc.log.model.LogModel;
import com.erc.log.configuration.LogConfiguration;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.erc.log.test", appContext.getPackageName());
    }
}
