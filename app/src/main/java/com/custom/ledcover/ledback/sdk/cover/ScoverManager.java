package com.custom.ledcover.ledback.sdk.cover;

import android.content.ComponentName;
import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.view.Window;
import android.view.WindowManager;
import androidx.core.internal.view.SupportMenu;
import com.samsung.android.cover.CoverState;
import com.samsung.android.cover.ICoverManager;
import com.custom.ledcover.ledback.sdk.SsdkUnsupportedException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScoverManager {
    public static final int COVER_MODE_HIDE_SVIEW_ONCE = 2;
    public static final int COVER_MODE_NONE = 0;
    public static final int COVER_MODE_SVIEW = 1;
    private static final String FEATURE_COVER_CLEAR = "com.sec.feature.cover.clearcover";
    private static final String FEATURE_COVER_CLEAR_SIDE_VIEW = "com.sec.feature.cover.clearsideviewcover";
    private static final String FEATURE_COVER_FLIP = "com.sec.feature.cover.flip";
    private static final String FEATURE_COVER_LED_BACK = "com.sec.feature.cover.ledbackcover";
    private static final String FEATURE_COVER_MINI_SVIEW_WALLET = "com.sec.feature.cover.minisviewwalletcover";
    private static final String FEATURE_COVER_NEON = "com.sec.feature.cover.neoncover";
    private static final String FEATURE_COVER_NFCLED = "com.sec.feature.cover.nfcledcover";
    private static final String FEATURE_COVER_SVIEW = "com.sec.feature.cover.sview";
    static final int SDK_VERSION = 16842752;
    private static final String TAG = "ScoverManager";
    private static boolean sIsClearCoverSystemFeatureEnabled = false;
    private static boolean sIsClearSideViewCoverSystemFeatureEnabled = false;
    private static boolean sIsFilpCoverSystemFeatureEnabled = false;
    private static boolean sIsLEDBackCoverSystemFeatureEnabled = false;
    private static boolean sIsMiniSviewWalletCoverSysltemFeatureEnabled = false;
    private static boolean sIsNeonCoverSystemFeatureEnabled = false;
    private static boolean sIsNfcLedCoverSystemFeatureEnabled = false;
    private static boolean sIsSViewCoverSystemFeatureEnabled = false;
    private static boolean sIsSystemFeatureQueried = false;
    private static int sServiceVersion = 16777216;
    private Context mContext;
    private final CopyOnWriteArrayList<CoverPowerKeyListenerDelegate> mCoverPowerKeyListenerDelegates = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<CoverStateListenerDelegate> mCoverStateListenerDelegates = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<CoverListenerDelegate> mLcdOffDisableDelegates = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<LedSystemEventListenerDelegate> mLedSystemEventListenerDelegates = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<LegacyLedSystemEventListenerDelegate> mLegacyLedSystemEventListenerDelegates = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<CoverListenerDelegate> mListenerDelegates = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<NfcLedCoverTouchListenerDelegate> mNfcLedCoverTouchListenerDelegates = new CopyOnWriteArrayList<>();
    private ICoverManager mService;
    private IBinder mToken = new Binder();

    public static class CoverPowerKeyListener {
        private static final int EVENT_TYPE_POWER_KEY = 10;

        public void onPowerKeyPress() {
        }
    }

    public static class CoverStateListener {
        public void onCoverAttachStateChanged(boolean z) {
        }

        public void onCoverSwitchStateChanged(boolean z) {
        }
    }

    public static class LedSystemEventListener {
        private static final int EVENT_TYPE_SYSTEM = 4;

        public void onSystemCoverEvent(int i, Bundle bundle) {
        }
    }

    public static class NfcLedCoverTouchListener {
        public static final int EVENT_TYPE_ALARM = 1;
        public static final int EVENT_TYPE_BIXBY = 9;
        public static final int EVENT_TYPE_CALL = 0;
        public static final int EVENT_TYPE_FACTORY = 5;
        public static final int EVENT_TYPE_REMINDER = 6;
        public static final int EVENT_TYPE_SCHEDULE = 3;
        public static final int EVENT_TYPE_TIMER = 2;
        public static final int EVENT_TYPE_VOICE_RECORDER = 7;
        public static final int EVENT_TYPE_VOLUME_CONTROLLER = 8;

        public void onCoverTapLeft() {
        }

        public void onCoverTapMid() {
        }

        public void onCoverTapRight() {
        }

        public void onCoverTouchAccept() {
        }

        public void onCoverTouchReject() {
        }
    }

    @Deprecated
    public interface ScoverStateListener {
        void onCoverStateChanged(ScoverState scoverState);
    }

    public static class StateListener {
        public void onCoverStateChanged(ScoverState scoverState) {
        }
    }

    public ScoverManager(Context context) {
        this.mContext = context;
        initSystemFeature();
    }

    private void initSystemFeature() {
        if (!sIsSystemFeatureQueried) {
            sIsFilpCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature(FEATURE_COVER_FLIP);
            sIsSViewCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature(FEATURE_COVER_SVIEW);
            sIsNfcLedCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature(FEATURE_COVER_NFCLED);
            sIsClearCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature(FEATURE_COVER_CLEAR);
            sIsNeonCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature(FEATURE_COVER_NEON);
            sIsClearSideViewCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature(FEATURE_COVER_CLEAR_SIDE_VIEW);
            sIsLEDBackCoverSystemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature(FEATURE_COVER_LED_BACK);
            sIsMiniSviewWalletCoverSysltemFeatureEnabled = this.mContext.getPackageManager().hasSystemFeature(FEATURE_COVER_MINI_SVIEW_WALLET);
            sIsSystemFeatureQueried = true;
            sServiceVersion = getCoverManagerVersion();
        }
    }

    public boolean isSupportCover() {
        return sIsFilpCoverSystemFeatureEnabled || sIsSViewCoverSystemFeatureEnabled || sIsClearCoverSystemFeatureEnabled || sIsNeonCoverSystemFeatureEnabled || sIsClearSideViewCoverSystemFeatureEnabled || sIsNfcLedCoverSystemFeatureEnabled || sIsLEDBackCoverSystemFeatureEnabled || sIsMiniSviewWalletCoverSysltemFeatureEnabled;
    }

    /* access modifiers changed from: package-private */
    public boolean isSmartCover() {
        ScoverState coverState = getCoverState();
        return coverState != null && coverState.type == 255;
    }

    /* access modifiers changed from: package-private */
    public boolean isSupportFlipCover() {
        return sIsFilpCoverSystemFeatureEnabled;
    }

    /* access modifiers changed from: package-private */
    public boolean isSupportSViewCover() {
        return sIsSViewCoverSystemFeatureEnabled;
    }

    /* access modifiers changed from: package-private */
    public boolean isSupportClearCover() {
        return sIsClearCoverSystemFeatureEnabled;
    }

    /* access modifiers changed from: package-private */
    public boolean isSupportNfcLedCover() {
        return sIsNfcLedCoverSystemFeatureEnabled;
    }

    /* access modifiers changed from: package-private */
    public boolean isSupportNeonCover() {
        return sIsNeonCoverSystemFeatureEnabled;
    }

    /* access modifiers changed from: package-private */
    public boolean isSupportClearSideViewCover() {
        return sIsClearSideViewCoverSystemFeatureEnabled;
    }

    /* access modifiers changed from: package-private */
    public boolean isSupportLEDBackCover() {
        return sIsLEDBackCoverSystemFeatureEnabled;
    }

    /* access modifiers changed from: package-private */
    public boolean isSupportTypeOfCover(int i) {
        if (i == 0) {
            return sIsFilpCoverSystemFeatureEnabled;
        }
        if (i == 1 || i == 3) {
            return sIsSViewCoverSystemFeatureEnabled;
        }
        if (i == 11) {
            return sIsNeonCoverSystemFeatureEnabled;
        }
        if (i == 7) {
            return sIsNfcLedCoverSystemFeatureEnabled;
        }
        if (i == 8) {
            return sIsClearCoverSystemFeatureEnabled;
        }
        if (i == 14) {
            return sIsLEDBackCoverSystemFeatureEnabled;
        }
        if (i != 15) {
            return false;
        }
        return sIsClearSideViewCoverSystemFeatureEnabled;
    }

    /* access modifiers changed from: package-private */
    public int getCoverManagerVersion() {
        int i = 0;
        if (isSupportCover()) {
            try {
                i = ((Integer) ICoverManager.class.getMethod("getVersion", new Class[0]).invoke(getService(), new Object[0])).intValue();
            } catch (Exception e) {
                Log.w(TAG, "getVersion failed : " + e);
            }
            Log.d(TAG, "serviceVersion : " + i);
            return i;
        }
        i = 16777216;
        Log.d(TAG, "serviceVersion : " + i);
        return i;
    }

    public String getServiceVersionName() {
        int i = sServiceVersion;
        return String.format("%d.%d.%d", new Object[]{Integer.valueOf((i >> 24) & 255), Integer.valueOf((i >> 16) & 255), Integer.valueOf(i & SupportMenu.USER_MASK)});
    }

    static boolean isSupportableVersion(int i) {
        int i2 = (i >> 24) & 255;
        int i3 = (i >> 16) & 255;
        int i4 = i & SupportMenu.USER_MASK;
        int i5 = sServiceVersion;
        return ((i5 >> 24) & 255) >= i2 && ((i5 >> 16) & 255) >= i3 && (65535 & i5) >= i4;
    }

    public synchronized ICoverManager getService() {
        if (this.mService == null) {
            this.mService = ICoverManager.Stub.asInterface(ServiceManager.getService("cover"));
            if (this.mService == null) {
                Slog.w(TAG, "warning: no COVER_MANAGER_SERVICE");
            }
        }
        return this.mService;
    }

    public void setCoverModeToWindow(Window window, int i) {
        if (!isSupportSViewCover()) {
            Log.w(TAG, "setSViewCoverModeToWindow : This device is not supported s view cover");
            return;
        }
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (attributes != null) {
            //attributes.coverMode = i;
            window.setAttributes(attributes);
        }
    }

    public void setCoverModeToWindow(WindowManager.LayoutParams layoutParams, int i) {
        if (!isSupportSViewCover()) {
            Log.w(TAG, "setSViewCoverModeToWindow : This device is not supported s view cover");
        } else if (layoutParams != null) {
            //layoutParams.coverMode = i;
        }
    }

    public WindowManager.LayoutParams setCoverMode(WindowManager.LayoutParams layoutParams, int i) {
        if (!isSupportSViewCover()) {
            Log.w(TAG, "setSViewCoverModeToWindow : This device is not supported s view cover");
            return layoutParams;
        }
        if (layoutParams != null) {
            Log.d(TAG, "setCoverMode : " + i);
            //layoutParams.coverMode = i;
        }
        return layoutParams;
    }

    @Deprecated
    public void registerListener(ScoverStateListener scoverStateListener) {
        Log.d(TAG, "registerListener : Use deprecated API!! Change ScoverStateListener to StateListener");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: com.samsung.android.sdk.cover.CoverListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: com.samsung.android.sdk.cover.CoverListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v8, resolved type: android.os.IBinder} */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    public void registerListener(StateListener stateListener) {
        IBinder iBinder;
        Log.d(TAG, "registerListener");
        if (!isSupportCover()) {
            Log.w(TAG, "registerListener : This device is not supported cover");
        } else if (isSmartCover()) {
            Log.w(TAG, "registerListener : If cover is smart cover, it does not need to register listener of intenal App");
        } else if (stateListener == null) {
            Log.w(TAG, "registerListener : listener is null");
        } else {
            boolean z = false;
            Iterator<CoverListenerDelegate> it = this.mListenerDelegates.iterator();
            while (true) {
                if (!it.hasNext()) {
                    iBinder = null;
                    break;
                }
                CoverListenerDelegate next = it.next();
                if (next.getListener().equals(stateListener)) {
                    z = true;
                    iBinder = next;
                    break;
                }
            }
            if (iBinder == null) {
                iBinder = new CoverListenerDelegate(stateListener, (Handler) null, this.mContext);
            }
            try {
                ICoverManager service = getService();
                if (service != null) {
                    ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (iBinder != null) {
                        service.registerCallback(iBinder, componentName);
                        if (!z) {
                            this.mListenerDelegates.add((CoverListenerDelegate) iBinder);
                        }
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in registerListener: ", e);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v2, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v4, resolved type: com.samsung.android.sdk.cover.CoverStateListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v6, resolved type: com.samsung.android.sdk.cover.CoverStateListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v7, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: android.os.IBinder} */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    public void registerListener(CoverStateListener coverStateListener) throws SsdkUnsupportedException {
        IBinder iBinder;
        Log.d(TAG, "registerListener");
        if (!isSupportCover()) {
            Log.w(TAG, "registerListener : This device is not supported cover");
        } else if (isSmartCover()) {
            Log.w(TAG, "registerListener : If cover is smart cover, it does not need to register listener of intenal App");
        } else if (!isSupportableVersion(SDK_VERSION)) {
            throw new SsdkUnsupportedException("This device is not supported this function. Device is must higher then v1.1.0", 2);
        } else if (coverStateListener == null) {
            Log.w(TAG, "registerListener : listener is null");
        } else {
            boolean z = false;
            Iterator<CoverStateListenerDelegate> it = this.mCoverStateListenerDelegates.iterator();
            while (true) {
                if (!it.hasNext()) {
                    iBinder = null;
                    break;
                }
                CoverStateListenerDelegate next = it.next();
                if (next.getListener().equals(coverStateListener)) {
                    z = true;
                    iBinder = next;
                    break;
                }
            }
            if (iBinder == null) {
                iBinder = new CoverStateListenerDelegate(coverStateListener, (Handler) null, this.mContext);
            }
            try {
                ICoverManager service = getService();
                if (service != null) {
                    ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (iBinder != null) {
                        service.registerListenerCallback(iBinder, componentName, 2);
                        if (!z) {
                            this.mCoverStateListenerDelegates.add((CoverStateListenerDelegate) iBinder);
                        }
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in registerListener: ", e);
            }
        }
    }

    @Deprecated
    public void unregisterListener(ScoverStateListener scoverStateListener) {
        Log.d(TAG, "unregisterListener : Use deprecated API!! Change ScoverStateListener to StateListener");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: com.samsung.android.sdk.cover.CoverListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: android.os.IBinder} */
    /* JADX WARNING: Multi-variable type inference failed */
    public void unregisterListener(StateListener stateListener) {
        Log.d(TAG, "unregisterListener");
        if (!isSupportCover()) {
            Log.w(TAG, "unregisterListener : This device is not supported cover");
        } else if (isSmartCover()) {
            Log.w(TAG, "unregisterListener : If cover is smart cover, it does not need to unregister listener of intenal App");
        } else if (stateListener == null) {
            Log.w(TAG, "unregisterListener : listener is null");
        } else {
            IBinder iBinder = null;
            Iterator<CoverListenerDelegate> it = this.mListenerDelegates.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CoverListenerDelegate next = it.next();
                if (next.getListener().equals(stateListener)) {
                    iBinder = next;
                    break;
                }
            }
            if (iBinder != null) {
                try {
                    ICoverManager service = getService();
                    if (service != null && service.unregisterCallback(iBinder)) {
                        this.mListenerDelegates.remove(iBinder);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in unregisterListener: ", e);
                }
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: com.samsung.android.sdk.cover.CoverStateListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: android.os.IBinder} */
    /* JADX WARNING: Multi-variable type inference failed */
    public void unregisterListener(CoverStateListener coverStateListener) throws SsdkUnsupportedException {
        Log.d(TAG, "unregisterListener");
        if (!isSupportCover()) {
            Log.w(TAG, "unregisterListener : This device is not supported cover");
        } else if (isSmartCover()) {
            Log.w(TAG, "unregisterListener : If cover is smart cover, it does not need to unregister listener of intenal App");
        } else if (!isSupportableVersion(SDK_VERSION)) {
            throw new SsdkUnsupportedException("This device is not supported this function. Device is must higher then v1.1.0", 2);
        } else if (coverStateListener == null) {
            Log.w(TAG, "unregisterListener : listener is null");
        } else {
            IBinder iBinder = null;
            Iterator<CoverStateListenerDelegate> it = this.mCoverStateListenerDelegates.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CoverStateListenerDelegate next = it.next();
                if (next.getListener().equals(coverStateListener)) {
                    iBinder = next;
                    break;
                }
            }
            if (iBinder != null) {
                try {
                    ICoverManager service = getService();
                    if (service != null && service.unregisterCallback(iBinder)) {
                        this.mCoverStateListenerDelegates.remove(iBinder);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in unregisterListener: ", e);
                }
            }
        }
    }

    public ScoverState getCoverState() {
        if (!isSupportCover()) {
            Log.w(TAG, "getCoverState : This device is not supported cover");
            return null;
        }
        try {
            ICoverManager service = getService();
            if (service != null) {
                CoverState coverState = service.getCoverState();
                if (coverState == null) {
                    Log.e(TAG, "getCoverState : coverState is null"); // ?? Here
                } else if (coverState.type == 255 && !coverState.switchState) {
                    Log.e(TAG, "getCoverState : type of cover is nfc smart cover and cover is closed");
                    return null;
                } else if (isSupportableVersion(17498112)) {
                    return new ScoverState(coverState.switchState, coverState.type, coverState.color, coverState.widthPixel, coverState.heightPixel, coverState.attached, coverState.model, coverState.fakeCover, coverState.fotaMode);
                } else {
                    if (isSupportableVersion(17235968)) {
                        return new ScoverState(coverState.switchState, coverState.type, coverState.color, coverState.widthPixel, coverState.heightPixel, coverState.attached, coverState.model, coverState.fakeCover);
                    }
                    if (isSupportableVersion(16908288)) {
                        return new ScoverState(coverState.switchState, coverState.type, coverState.color, coverState.widthPixel, coverState.heightPixel, coverState.attached, coverState.model);
                    }
                    if (isSupportableVersion(SDK_VERSION)) {
                        return new ScoverState(coverState.switchState, coverState.type, coverState.color, coverState.widthPixel, coverState.heightPixel, coverState.attached);
                    }
                    return new ScoverState(coverState.switchState, coverState.type, coverState.color, coverState.widthPixel, coverState.heightPixel);
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getCoverState: ", e);
        }
        return null;
    }

    public boolean checkValidPacakge(String str) {
        if (!isSupportCover()) {
            Log.w(TAG, "checkValidPacakge : This device is not supported cover");
            return false;
        } else if (str == null) {
            Log.w(TAG, "checkValidPacakge : pkg is null");
            return false;
        } else {
            try {
                ICoverManager service = getService();
                if (service != null) {
                    CoverState coverState = service.getCoverState();
                    if (coverState == null || !coverState.attached) {
                        Log.e(TAG, "checkValidPacakge : coverState is null or cover is detached");
                    } else {
                        String smartCoverAppUri = coverState.getSmartCoverAppUri();
                        if (TextUtils.isEmpty(smartCoverAppUri) || !str.equals(smartCoverAppUri.substring(1, smartCoverAppUri.length()))) {
                            return false;
                        }
                        return true;
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in checkCoverAppUri: ", e);
            }
            return false;
        }
    }

    public void sendDataToCover(int i, byte[] bArr) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "sendDataToCover : This device is not supported cover");
        } else if (isSmartCover()) {
            Log.w(TAG, "sendDataToCover : If cover is smart cover, it does not need to send the data to cover");
        } else if (isSupportableVersion(16908288)) {
            ICoverManager service = getService();
            if (service != null) {
                try {
                    service.sendDataToCover(i, bArr);
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in sendData : ", e);
                }
            }
        } else {
            throw new SsdkUnsupportedException("This device is not supported this function. Device is must higher then v1.2.0", 2);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: com.samsung.android.sdk.cover.NfcLedCoverTouchListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: com.samsung.android.sdk.cover.NfcLedCoverTouchListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v8, resolved type: android.os.IBinder} */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    public void registerNfcTouchListener(int i, NfcLedCoverTouchListener nfcLedCoverTouchListener) throws SsdkUnsupportedException {
        IBinder iBinder;
        if (!isSupportCover()) {
            Log.w(TAG, "registerNfcTouchListener : This device does not support cover");
            return;
        }
        Log.d(TAG, "registerNfcTouchListener");
        if (!isSupportNfcLedCover()) {
            Log.w(TAG, "registerNfcTouchListener : This device does not support NFC Led cover");
        } else if (!isSupportableVersion(16973824)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.3.0", 2);
        } else if (nfcLedCoverTouchListener == null) {
            Log.w(TAG, "registerNfcTouchListener : listener is null");
        } else {
            boolean z = false;
            Iterator<NfcLedCoverTouchListenerDelegate> it = this.mNfcLedCoverTouchListenerDelegates.iterator();
            while (true) {
                if (!it.hasNext()) {
                    iBinder = null;
                    break;
                }
                NfcLedCoverTouchListenerDelegate next = it.next();
                if (next.getListener().equals(nfcLedCoverTouchListener)) {
                    z = true;
                    iBinder = next;
                    break;
                }
            }
            if (iBinder == null) {
                iBinder = new NfcLedCoverTouchListenerDelegate(nfcLedCoverTouchListener, (Handler) null, this.mContext);
            }
            try {
                ICoverManager service = getService();
                if (service != null) {
                    ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (iBinder != null) {
                        service.registerNfcTouchListenerCallback(i, iBinder, componentName);
                        if (!z) {
                            this.mNfcLedCoverTouchListenerDelegates.add((NfcLedCoverTouchListenerDelegate) iBinder);
                        }
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in registerNfcTouchListener: ", e);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: com.samsung.android.sdk.cover.NfcLedCoverTouchListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: android.os.IBinder} */
    /* JADX WARNING: Multi-variable type inference failed */
    public void unregisterNfcTouchListener(NfcLedCoverTouchListener nfcLedCoverTouchListener) throws SsdkUnsupportedException {
        Log.d(TAG, "unregisterNfcTouchListener");
        if (!isSupportCover()) {
            Log.w(TAG, "unregisterNfcTouchListener : This device does not support cover");
        } else if (!isSupportNfcLedCover()) {
            Log.w(TAG, "unregisterNfcTouchListener : This device does not support NFC Led cover");
        } else if (!isSupportableVersion(16973824)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.3.0", 2);
        } else if (nfcLedCoverTouchListener == null) {
            Log.w(TAG, "unregisterNfcTouchListener : listener is null");
        } else {
            IBinder iBinder = null;
            Iterator<NfcLedCoverTouchListenerDelegate> it = this.mNfcLedCoverTouchListenerDelegates.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                NfcLedCoverTouchListenerDelegate next = it.next();
                if (next.getListener().equals(nfcLedCoverTouchListener)) {
                    iBinder = next;
                    break;
                }
            }
            if (iBinder != null) {
                try {
                    ICoverManager service = getService();
                    if (service != null && service.unregisterNfcTouchListenerCallback(iBinder)) {
                        this.mNfcLedCoverTouchListenerDelegates.remove(iBinder);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in unregisterNfcTouchListener: ", e);
                }
            }
        }
    }

    public void sendDataToNfcLedCover(int i, byte[] bArr) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "sendDataToNfcLedCover : This device does not support cover");
        } else if (!isSupportNfcLedCover()) {
            Log.w(TAG, "sendDataToNfcLedCover : This device does not support NFC Led cover");
        } else if (isSupportableVersion(16973824)) {
            ICoverManager service = getService();
            if (service != null) {
                try {
                    service.sendDataToNfcLedCover(i, bArr);
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in sendData to NFC : ", e);
                }
            }
        } else {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.3.0", 2);
        }
    }

    public void addLedNotification(Bundle bundle) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "addLedNotification : This device does not support cover");
        } else if (!isSupportNfcLedCover()) {
            Log.w(TAG, "addLedNotification : This device does not support NFC Led cover");
        } else if (!isSupportableVersion(17039360)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.4.0", 2);
        } else if (bundle == null) {
            Log.e(TAG, "addLedNotification : Null notification data!");
        } else {
            ICoverManager service = getService();
            if (service != null) {
                try {
                    service.addLedNotification(bundle);
                } catch (RemoteException e) {
                    Log.e(TAG, "addLedNotification in sendData to NFC : ", e);
                }
            }
        }
    }

    public void removeLedNotification(Bundle bundle) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "removeLedNotification : This device does not support cover");
        } else if (!isSupportNfcLedCover()) {
            Log.w(TAG, "removeLedNotification : This device does not support NFC Led cover");
        } else if (!isSupportableVersion(17039360)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.4.0", 2);
        } else if (bundle == null) {
            Log.e(TAG, "removeLedNotification : Null notification data!");
        } else {
            ICoverManager service = getService();
            if (service != null) {
                try {
                    service.removeLedNotification(bundle);
                } catch (RemoteException e) {
                    Log.e(TAG, "removeLedNotification in sendData to NFC : ", e);
                }
            }
        }
    }

    public void sendSystemEvent(Bundle bundle) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "sendSystemEvent : This device does not support cover");
        } else if (!isSupportNfcLedCover()) {
            Log.w(TAG, "sendSystemEvent : This device does not support NFC Led cover");
        } else if (!isSupportableVersion(17170432)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.6.0", 2);
        } else if (bundle == null) {
            Log.e(TAG, "sendSystemEvent : Null system event data!");
        } else {
            ICoverManager service = getService();
            if (service != null) {
                try {
                    service.sendSystemEvent(bundle);
                } catch (RemoteException e) {
                    Log.e(TAG, "sendSystemEvent in sendData to NFC : ", e);
                }
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: com.samsung.android.sdk.cover.LedSystemEventListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: com.samsung.android.sdk.cover.LedSystemEventListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v8, resolved type: android.os.IBinder} */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    public void registerLedSystemListener(LedSystemEventListener ledSystemEventListener) throws SsdkUnsupportedException {
        IBinder iBinder;
        if (!isSupportCover()) {
            Log.w(TAG, "registerLedSystemListener : This device does not support cover");
            return;
        }
        Log.d(TAG, "registerLedSystemListener");
        if (!isSupportNfcLedCover() && !isSupportNeonCover()) {
            Log.w(TAG, "registerLedSystemListener : This device does not support NFC Led cover or Neon Cover");
        } else if ((!isSupportNfcLedCover() || !isSupportableVersion(16973824)) && (!isSupportNeonCover() || !isSupportableVersion(17301504))) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.3.0 for NFC LED Cover and v1.8.0 for Neon cover", 2);
        } else if (ledSystemEventListener == null) {
            Log.w(TAG, "registerLedSystemListener : listener is null");
        } else if (!supportNewLedSystemEventListener()) {
            registerLegacyLedSystemListener(ledSystemEventListener);
        } else {
            boolean z = false;
            Iterator<LedSystemEventListenerDelegate> it = this.mLedSystemEventListenerDelegates.iterator();
            while (true) {
                if (!it.hasNext()) {
                    iBinder = null;
                    break;
                }
                LedSystemEventListenerDelegate next = it.next();
                if (next.getListener().equals(ledSystemEventListener)) {
                    z = true;
                    iBinder = next;
                    break;
                }
            }
            if (iBinder == null) {
                iBinder = new LedSystemEventListenerDelegate(ledSystemEventListener, (Handler) null, this.mContext);
            }
            try {
                ICoverManager service = getService();
                if (service != null) {
                    ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (iBinder != null) {
                        service.registerNfcTouchListenerCallback(4, iBinder, componentName);
                        if (!z) {
                            this.mLedSystemEventListenerDelegates.add((LedSystemEventListenerDelegate) iBinder);
                        }
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in registerLedSystemListener: ", e);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: com.samsung.android.sdk.cover.LedSystemEventListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: android.os.IBinder} */
    /* JADX WARNING: Multi-variable type inference failed */
    public void unregisterLedSystemEventListener(LedSystemEventListener ledSystemEventListener) throws SsdkUnsupportedException {
        Log.d(TAG, "unregisterLedSystemEventListener");
        if (!isSupportCover()) {
            Log.w(TAG, "unregisterLedSystemEventListener : This device does not support cover");
            return;
        }
        Log.d(TAG, "unregisterLedSystemEventListener");
        if (!isSupportNfcLedCover() && !isSupportNeonCover()) {
            Log.w(TAG, "unregisterLedSystemEventListener : This device does not support NFC Led cover or Neon Cover");
        } else if ((!isSupportNfcLedCover() || !isSupportableVersion(16973824)) && (!isSupportNeonCover() || !isSupportableVersion(17301504))) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.3.0 for NFC LED Cover and v1.8.0 for Neon cover", 2);
        } else if (ledSystemEventListener == null) {
            Log.w(TAG, "unregisterLedSystemEventListener : listener is null");
        } else if (!supportNewLedSystemEventListener()) {
            unregisterLegacyLedSystemEventListener(ledSystemEventListener);
        } else {
            IBinder iBinder = null;
            Iterator<LedSystemEventListenerDelegate> it = this.mLedSystemEventListenerDelegates.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                LedSystemEventListenerDelegate next = it.next();
                if (next.getListener().equals(ledSystemEventListener)) {
                    iBinder = next;
                    break;
                }
            }
            if (iBinder != null) {
                try {
                    ICoverManager service = getService();
                    if (service != null && service.unregisterNfcTouchListenerCallback(iBinder)) {
                        this.mLedSystemEventListenerDelegates.remove(iBinder);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in unregisterLedSystemEventListener: ", e);
                }
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: com.samsung.android.sdk.cover.CoverPowerKeyListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: com.samsung.android.sdk.cover.CoverPowerKeyListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v8, resolved type: android.os.IBinder} */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    public void registerCoverPowerKeyListener(CoverPowerKeyListener coverPowerKeyListener) throws SsdkUnsupportedException {
        IBinder iBinder;
        if (!isSupportCover()) {
            Log.w(TAG, "registerCoverPowerKeyListener : This device does not support cover");
            return;
        }
        Log.d(TAG, "registerCoverPowerKeyListener");
        if (!isSupportFlipCover() && !isSupportNeonCover()) {
            Log.w(TAG, "registerLedSystemListener : This device does not support Flip cover or Neon Cover");
        } else if (!isSupportableVersion(17432576)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.10.0 for Flip Cover and Neon cover", 2);
        } else if (coverPowerKeyListener == null) {
            Log.w(TAG, "registerCoverPowerKeyListener : listener is null");
        } else {
            boolean z = false;
            Iterator<CoverPowerKeyListenerDelegate> it = this.mCoverPowerKeyListenerDelegates.iterator();
            while (true) {
                if (!it.hasNext()) {
                    iBinder = null;
                    break;
                }
                CoverPowerKeyListenerDelegate next = it.next();
                if (next.getListener().equals(coverPowerKeyListener)) {
                    z = true;
                    iBinder = next;
                    break;
                }
            }
            if (iBinder == null) {
                iBinder = new CoverPowerKeyListenerDelegate(coverPowerKeyListener, (Handler) null, this.mContext);
            }
            try {
                ICoverManager service = getService();
                if (service != null) {
                    ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                    if (iBinder != null) {
                        service.registerNfcTouchListenerCallback(10, iBinder, componentName);
                        if (!z) {
                            this.mCoverPowerKeyListenerDelegates.add((CoverPowerKeyListenerDelegate) iBinder);
                        }
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in registerCoverPowerKeyListener: ", e);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: com.samsung.android.sdk.cover.CoverPowerKeyListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v9, resolved type: android.os.IBinder} */
    /* JADX WARNING: Multi-variable type inference failed */
    public void unregisterCoverPowerKeyListener(CoverPowerKeyListener coverPowerKeyListener) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "unregisterCoverPowerKeyListener : This device does not support cover");
            return;
        }
        Log.d(TAG, "unregisterCoverPowerKeyListener");
        if (!isSupportFlipCover() && !isSupportNeonCover()) {
            Log.w(TAG, "unregisterCoverPowerKeyListener : This device does not support Flip cover or Neon Cover");
        } else if (!isSupportableVersion(17432576)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.1.0 for Flip Cover Neon cover", 2);
        } else if (coverPowerKeyListener == null) {
            Log.w(TAG, "unregisterCoverPowerKeyListener : listener is null");
        } else {
            IBinder iBinder = null;
            Iterator<CoverPowerKeyListenerDelegate> it = this.mCoverPowerKeyListenerDelegates.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CoverPowerKeyListenerDelegate next = it.next();
                if (next.getListener().equals(coverPowerKeyListener)) {
                    iBinder = next;
                    break;
                }
            }
            if (iBinder != null) {
                try {
                    ICoverManager service = getService();
                    if (service != null && service.unregisterNfcTouchListenerCallback(iBinder)) {
                        this.mCoverPowerKeyListenerDelegates.remove(iBinder);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in unregisterCoverPowerKeyListener: ", e);
                }
            }
        }
    }

    private static boolean supportNewLedSystemEventListener() throws SsdkUnsupportedException {
        return isSupportableVersion(17104896);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v6, resolved type: com.samsung.android.sdk.cover.LegacyLedSystemEventListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v7, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: android.os.IBinder} */
    /* JADX WARNING: type inference failed for: r2v4, types: [com.samsung.android.sdk.cover.LegacyLedSystemEventListenerDelegate, java.lang.Object] */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    private void registerLegacyLedSystemListener(LedSystemEventListener ledSystemEventListener) throws SsdkUnsupportedException {
        IBinder iBinder;
        if (ledSystemEventListener == null) {
            Log.w(TAG, "registerLegacyLedSystemListener : listener is null");
            return;
        }
        Iterator<LegacyLedSystemEventListenerDelegate> it = this.mLegacyLedSystemEventListenerDelegates.iterator();
        while (true) {
            if (!it.hasNext()) {
                iBinder = null;
                break;
            }
            LegacyLedSystemEventListenerDelegate next = it.next();
            if (next.getListener().equals(ledSystemEventListener)) {
                iBinder = next;
                break;
            }
        }
        if (iBinder == null) {
            Log.e(TAG,"iBinder == null, impossible to uncompile.");
           /* ? legacyLedSystemEventListenerDelegate = new LegacyLedSystemEventListenerDelegate(ledSystemEventListener, (Handler) null, this.mContext);
            this.mLegacyLedSystemEventListenerDelegates.add(legacyLedSystemEventListenerDelegate);
            iBinder = legacyLedSystemEventListenerDelegate;*/
        }
        try {
            ICoverManager service = getService();
            if (service != null) {
                ComponentName componentName = new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName());
                if (iBinder != null) {
                    service.registerNfcTouchListenerCallback(4, iBinder, componentName);
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in registerLegacyLedSystemListener: ", e);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: com.samsung.android.sdk.cover.LegacyLedSystemEventListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: android.os.IBinder} */
    /* JADX WARNING: Multi-variable type inference failed */
    private void unregisterLegacyLedSystemEventListener(LedSystemEventListener ledSystemEventListener) throws SsdkUnsupportedException {
        Log.d(TAG, "unregisterLegacyLedSystemEventListener");
        if (ledSystemEventListener == null) {
            Log.w(TAG, "unregisterLegacyLedSystemEventListener : listener is null");
            return;
        }
        IBinder iBinder = null;
        Iterator<LegacyLedSystemEventListenerDelegate> it = this.mLegacyLedSystemEventListenerDelegates.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            LegacyLedSystemEventListenerDelegate next = it.next();
            if (next.getListener().equals(ledSystemEventListener)) {
                iBinder = next;
                break;
            }
        }
        if (iBinder != null) {
            try {
                ICoverManager service = getService();
                if (service != null && service.unregisterNfcTouchListenerCallback(iBinder)) {
                    this.mLegacyLedSystemEventListenerDelegates.remove(iBinder);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in unregisterLegacyLedSystemEventListener: ", e);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: com.samsung.android.sdk.cover.CoverListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: com.samsung.android.sdk.cover.CoverListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v8, resolved type: android.os.IBinder} */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    public boolean disableLcdOffByCover(StateListener stateListener) throws SsdkUnsupportedException {
        IBinder iBinder;
        if (!isSupportCover()) {
            Log.w(TAG, "disableLcdOffByCover : This device does not support cover");
            return false;
        } else if (!isSupportableVersion(17104896)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.5.0", 2);
        } else if (stateListener == null) {
            Log.w(TAG, "disableLcdOffByCover : listener cannot be null");
            return false;
        } else {
            Log.d(TAG, "disableLcdOffByCover");
            Iterator<CoverListenerDelegate> it = this.mLcdOffDisableDelegates.iterator();
            while (true) {
                if (!it.hasNext()) {
                    iBinder = null;
                    break;
                }
                CoverListenerDelegate next = it.next();
                if (next.getListener().equals(stateListener)) {
                    iBinder = next;
                    break;
                }
            }
            if (iBinder == null) {
                iBinder = new CoverListenerDelegate(stateListener, (Handler) null, this.mContext);
            }
            try {
                ICoverManager service = getService();
                if (service != null && service.disableLcdOffByCover(iBinder, new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName()))) {
                    this.mLcdOffDisableDelegates.add((CoverListenerDelegate) iBinder);
                    return true;
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in disableLcdOffByCover: ", e);
            }
            return false;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: android.os.IBinder} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v4, resolved type: com.samsung.android.sdk.cover.CoverListenerDelegate} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: android.os.IBinder} */
    /* JADX WARNING: Multi-variable type inference failed */
    public boolean enableLcdOffByCover(StateListener stateListener) throws SsdkUnsupportedException {
        if (!isSupportCover()) {
            Log.w(TAG, "enableLcdOffByCover : This device does not support cover");
            return false;
        } else if (!isSupportableVersion(17104896)) {
            throw new SsdkUnsupportedException("This device does not support this function. Device is must higher then v1.5.0", 2);
        } else if (stateListener == null) {
            Log.w(TAG, "enableLcdOffByCover : listener cannot be null");
            return false;
        } else {
            Log.d(TAG, "enableLcdOffByCover");
            IBinder iBinder = null;
            Iterator<CoverListenerDelegate> it = this.mLcdOffDisableDelegates.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CoverListenerDelegate next = it.next();
                if (next.getListener().equals(stateListener)) {
                    iBinder = next;
                    break;
                }
            }
            if (iBinder == null) {
                Log.e(TAG, "enableLcdOffByCover: Matching listener not found, cannot enable");
                return false;
            }
            try {
                ICoverManager service = getService();
                if (service != null && service.enableLcdOffByCover(iBinder, new ComponentName(this.mContext.getPackageName(), getClass().getCanonicalName()))) {
                    this.mLcdOffDisableDelegates.remove(iBinder);
                    return true;
                }
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in unregisterNfcTouchListener: ", e);
            }
            return false;
        }
    }

    public boolean isCoverManagerDisabled() {
        if (!isSupportCover()) {
            Log.w(TAG, "isCoverManagerDisabled : This device is not supported cover");
            return false;
        }
        try {
            ICoverManager service = getService();
            if (service != null) {
                return service.isCoverManagerDisabled();
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in isCoverManagerDisabled : ", e);
            return false;
        }
    }

    public void disableCoverManager(boolean z) {
        if (!isSupportCover()) {
            Log.w(TAG, "disableCoverManager : This device is not supported cover");
            return;
        }
        try {
            ICoverManager service = getService();
            if (service != null) {
                service.disableCoverManager(z, this.mToken, this.mContext.getPackageName());
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in disalbeCoverManager : ", e);
        }
    }
}
