package com.erc.log.helpers;

import android.util.Log;

import com.erc.log.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

public class TextFileHelper {
    public static void write(String fullPath, String text, boolean... append) {
        createParentFolder(fullPath);
        boolean append_ = append.length != 0 && append[0];
        File file = new File(fullPath);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file, append_));
            out.write(text);

        } catch (IOException e) {
            Log.e(Constants.TAG,"TextFileHelper.write()", e);
        }
        try {
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
            Log.e(Constants.TAG,"TextFileHelper.close", e);
        }
    }

    public static void createParentFolder(String fullPath) {
        File file = new File(fullPath);
        String parentFolder = file.getParent();
        if (parentFolder != null) {
            File directory = new File(parentFolder);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
    }

    public static String read(String fullPath) {
        String res = "";
        File file = new File(fullPath);

        try {

            String text = "";
            BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            boolean finished = false;

            if (file.exists())
                while (!finished) {
                    text = buffer.readLine();
                    if (text == null)
                        finished = true;
                    else {
                        res = res + text;
                    }
                }
            else
                Log.w(Constants.TAG, "File not exist: " + fullPath);

            buffer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void deleteLastLine(String fullPath) {
        try {
            RandomAccessFile f = new RandomAccessFile(fullPath, "rw");
            long length = 0;
            length = f.length() - 1;

            byte b;
            do {
                length -= 1;
                f.seek(length);
                b = f.readByte();
            } while (b != 10);


            f.setLength(length + 1);
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
