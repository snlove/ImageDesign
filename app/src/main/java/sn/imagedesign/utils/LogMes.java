package sn.imagedesign.utils;

import android.util.Log;

/**
 * Created by zy on 2015/8/18.
 */
public class LogMes   {

    private static final int LOW_LEVEL = 2;
    private static final int VERBOSSE = 3;
    private static final int DEBUG = 4;
    private static final int INFO = 5;
    private static final int  WARN= 5;
    private static final int ASSET = 7;
    private static final int STOP_OUTPUT = 8;
    private static final int PRINT_CHOICE = LOW_LEVEL;

    private static  LogMes instance = null;

    public static  LogMes getInstance() {
        if (instance == null) {
            instance = new LogMes();
        }
            return  instance;

    }

    public static void v(String tag, String message) {
        if (PRINT_CHOICE<VERBOSSE) {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (PRINT_CHOICE < DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (PRINT_CHOICE < WARN) {
            Log.w(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (PRINT_CHOICE < INFO) {
            Log.i(tag, message);
        }
    }

    public static void a(String tag, String message) {
        if (PRINT_CHOICE < ASSET) {
            Log.i(tag, message);
        }
    }
}
