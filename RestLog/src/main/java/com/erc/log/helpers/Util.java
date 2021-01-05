package com.erc.log.helpers;

import android.content.Context;

import com.erc.log.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by einar on 9/5/2015.
 */
public class Util {

    public static String getAppPath(Context context) {
        return context.getFilesDir() + "/";
    }
}
