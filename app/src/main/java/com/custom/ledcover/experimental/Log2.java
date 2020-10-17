package com.custom.ledcover.experimental;

import android.util.Log;

/* renamed from: com.samsung.android.app.ledbackcover.a.c */
public class Log2 {

    /* renamed from: a */
    public static final String f625a = ("[LED_BACK][" + Log2.class.getSimpleName() + "]");

    /* renamed from: a */
    public static void m825a(String str, String str2) {
        m826a(str, str2, 2);
    }

    /* renamed from: a */
    private static void m826a(String str, String str2, int i) {
            switch (i) {
                case 1:
                    Log.e(str, str2);
                    return;
                case 2:
                    Log.d(str, str2);
                    return;
                case 3:
                    Log.i(str, str2);
                    return;
                case 4:
                    Log.v(str, str2);
                    return;
                default:
                    return;
            }
    }
}
