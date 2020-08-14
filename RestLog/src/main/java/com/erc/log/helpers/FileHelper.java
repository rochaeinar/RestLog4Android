package com.erc.log.helpers;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHelper {
    public static void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();
        } catch (Exception e) {
        }

    }

    public static boolean deleteFile(String fullPath) {
        try {
            return new File(fullPath).delete();
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean deleteFile(String inputPath, String inputFile) {
        try {
            return new File(inputPath + inputFile).delete();
        } catch (Exception e) {
        }
        return false;
    }

    public static void copyFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        } catch (Exception e) {
        }
    }

    public static void renameFile(String path, String oldName, String newName) {
        try {
            File from = new File(path, oldName);
            File to = new File(path, newName);
            from.renameTo(to);
        } catch (Exception e) {
        }
    }

    public static void copyRawFileToSdCard(int idFileRaw, String fullPath, Context context) {
        try {
            InputStream in = context.getResources().openRawResource(idFileRaw);
            File file = new File(fullPath);
            File folder = new File(file.getParent());
            if (!folder.exists()) {
                folder.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(fullPath);
            byte[] buff = new byte[65536];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {

                in.close();
                out.close();
            }
        } catch (Exception e) {
            Log.e("copyFileRawToSdCard", fullPath);
        }
    }

    public static boolean exist(String fullPath) {
        File file = new File(fullPath);
        return file.exists();
    }
}
