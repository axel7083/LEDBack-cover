package com.custom.ledcover.ledback.sdk.cover;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.android.cover.INfcLedCoverTouchListenerCallback;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class LegacyLedSystemEventListenerDelegate extends INfcLedCoverTouchListenerCallback.Stub {
    private static final int MSG_SYSTEM_COVER_EVENT = 0;
    private static final String SYSTEM_EVENT_LED_OFF_COMMAND = "led_off_command";
    /* access modifiers changed from: private */
    public static final String TAG = LegacyLedSystemEventListenerDelegate.class.getSimpleName();
    private ListenerDelegateHandler mHandler;
    private Object mListener;

    @Override
    public void onCoverTapLeft() throws RemoteException {

    }

    @Override
    public void onCoverTapMid() throws RemoteException {

    }

    @Override
    public void onCoverTapRight() throws RemoteException {

    }

    public void onCoverTouchAccept() throws RemoteException {
    }

    public void onCoverTouchReject() throws RemoteException {
    }

    @Override
    public void onSystemCoverEvent(int i, Bundle bundle) throws RemoteException {

    }

    LegacyLedSystemEventListenerDelegate(Object obj, Handler handler, Context context) {
        this.mListener = obj;
        this.mHandler = new ListenerDelegateHandler(handler == null ? context.getMainLooper() : handler.getLooper(), this.mListener);
    }

    public Object getListener() {
        return this.mListener;
    }

    public void onSystemCoverEvent(int i, int[] iArr) throws RemoteException {
        Message obtainMessage = this.mHandler.obtainMessage(0);
        obtainMessage.arg1 = i;
        obtainMessage.obj = iArr;
        obtainMessage.sendToTarget();
    }

    private static class ListenerDelegateHandler extends Handler {
        private final Object mListener;

        public ListenerDelegateHandler(Looper looper, Object obj) {
            super(looper);
            this.mListener = obj;
        }

        public void handleMessage(Message message) {
            if (this.mListener != null && message.what == 0) {
                int[] iArr = (int[]) message.obj;
                Method method = null;
                try {
                    method = this.mListener.getClass().getMethod("onSystemCoverEvent", new Class[]{Integer.TYPE, Bundle.class});
                } catch (SecurityException e) {
                    Log.e(LegacyLedSystemEventListenerDelegate.TAG, "Error getting onSystemCoverEvent method", e);
                } catch (NoSuchMethodException e2) {
                    Log.e(LegacyLedSystemEventListenerDelegate.TAG, "Error getting onSystemCoverEvent method", e2);
                }
                if (method != null) {
                    if (iArr != null) {
                        try {
                            if (iArr.length >= 1) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(LegacyLedSystemEventListenerDelegate.SYSTEM_EVENT_LED_OFF_COMMAND, iArr[0]);
                                method.invoke(this.mListener, new Object[]{Integer.valueOf(message.arg1), bundle});
                                return;
                            }
                        } catch (IllegalAccessException e3) {
                            String access$000 = LegacyLedSystemEventListenerDelegate.TAG;
                            Log.e(access$000, "Error invoking " + method.getName(), e3);
                            return;
                        } catch (IllegalArgumentException e4) {
                            String access$0002 = LegacyLedSystemEventListenerDelegate.TAG;
                            Log.e(access$0002, "Error invoking " + method.getName(), e4);
                            return;
                        } catch (InvocationTargetException e5) {
                            String access$0003 = LegacyLedSystemEventListenerDelegate.TAG;
                            Log.e(access$0003, "Error invoking " + method.getName(), e5);
                            return;
                        }
                    }
                    String access$0004 = LegacyLedSystemEventListenerDelegate.TAG;
                    Log.e(access$0004, "Error: system event args empty: " + iArr);
                }
            }
        }
    }
}
