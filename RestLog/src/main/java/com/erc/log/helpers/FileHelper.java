package com.erc.log.helpers;

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

    public static void copyFile(String fullPath, String outputPath) {
        copyFile(getPath(fullPath), getFileName(fullPath), outputPath);
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


            in = new FileInputStream(inputPath + "/" + inputFile);
            out = new FileOutputStream(outputPath + "/" + inputFile);

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

    public static String getFileName(String path) {
        File file = new File(path);
        return file.getName();
    }

    public static String getPath(String path) {
        File file = new File(path);
        return file.getParent();
    }

    public static void renameFile(String path, String oldName, String newName) {
        try {
            File from = new File(path, oldName);
            File to = new File(path, newName);
            from.renameTo(to);
        } catch (Exception e) {
        }
    }

    public static boolean exist(String fullPath) {
        File file = new File(fullPath);
        return file.exists();
    }

    public static String removeExtension(String text) {
        if (text.indexOf(".") > 0)
            text = text.substring(0, text.lastIndexOf("."));
        return text;
    }

    public static String getExtension(String text) {
        if (text.indexOf(".") > 0)
            text = text.substring(text.lastIndexOf(".") + 1, text.length());
        return text;
    }

    public static int getSize(String fullPath) {
        File file = new File(fullPath);
        return Integer.parseInt(String.valueOf(file.length() / 1024));
    }
}
