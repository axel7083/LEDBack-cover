package com.custom.ledcover.ledback.sdk.util;

import android.util.Log;

public class SLog {
    private static final boolean DEBUG = true;
    private static final String tag = "[LCS] ";

    /* renamed from: d */
    public static void m663d(String str, String str2) {
        StackTraceElement stackTraceElement = new Exception().getStackTrace()[1];
        Log.d("LEDCoverService", String.format(tag + str + " [Line : %s] %s", Integer.valueOf(stackTraceElement.getLineNumber()), str2));
    }

    /* renamed from: e */
    public static void m664e(String str, String str2) {
        StackTraceElement stackTraceElement = new Exception().getStackTrace()[1];
        Log.d("LEDCoverService", String.format(tag + str + " [Line : %s] %s", Integer.valueOf(stackTraceElement.getLineNumber()), str2));
    }

    /* renamed from: i */
    public static void m666i(String str, String str2) {
        StackTraceElement stackTraceElement = new Exception().getStackTrace()[1];
        Log.d("LEDCoverService", String.format(tag + str + " [Line : %s] %s", Integer.valueOf(stackTraceElement.getLineNumber()), str2));
    }

    /* renamed from: w */
    public static void m667w(String str, String str2) {
        StackTraceElement stackTraceElement = new Exception().getStackTrace()[1];
        Log.d("LEDCoverService", String.format(tag + str + " [Line : %s] %s", Integer.valueOf(stackTraceElement.getLineNumber()), str2));
    }

    /* renamed from: e */
    public static void m665e(String str, String str2, Throwable th) {
        StackTraceElement stackTraceElement = new Exception().getStackTrace()[1];
        Log.e("LEDCoverService", String.format(tag + str + " [Line : %s] %s", Integer.valueOf(stackTraceElement.getLineNumber()), str2), th);
    }
}
