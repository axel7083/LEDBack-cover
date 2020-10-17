package com.custom.ledcover.ledback.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import com.custom.ledcover.ledback.ILedBackSdkService;
import com.custom.ledcover.ledback.sdk.util.SLog;
import com.custom.ledcover.ledback.sdk.cover.ScoverManager;

public class LedBackManager {
    private static final String TAG = "LedBackManager";
    boolean isCameraCancelSaved;
    boolean isCameraOrientationSaved;
    boolean isCameraPreviewSaved;
    boolean isDataCoverAppSaved;
    boolean isDavinciDataCoverAppSaved;
    boolean isPreviewSaved;
    boolean isPreviewSettingsSaved;
    boolean isRecordingModeSaved;
    int mCameraOrientation;
    boolean mCameraPreview;
    int mCameraTimer;
    private Context mContext;
    int mDavinciCameraEmoticon;
    boolean mDavinciCameraTimer;
    int mDavinciDLightingTimeOut;
    int mDavinciLightingStyle;
    boolean mDavinciTurnOver;
    private ILedBackSdkService mILedBackSdkService;
    private boolean mLedCoverServiceBound;
    private ServiceConnection mLedCoverServiceConnection = new ServiceConnection() {
        /* class com.sec.android.cover.ledback.sdk.LedBackManager.ServiceConnectionC24701 */

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SLog.m663d(LedBackManager.TAG, "onServiceConnected");
            LedBackManager.this.mILedBackSdkService = ILedBackSdkService.Stub.asInterface(iBinder);
            LedBackManager ledBackManager = LedBackManager.this;
            ledBackManager.mServiceConnected = true;
            if (ledBackManager.isCameraOrientationSaved) {
                LedBackManager ledBackManager2 = LedBackManager.this;
                ledBackManager2.setCameraOrientation(ledBackManager2.mCameraOrientation);
                LedBackManager.this.isCameraOrientationSaved = false;
            }
            if (LedBackManager.this.isCameraPreviewSaved) {
                LedBackManager ledBackManager3 = LedBackManager.this;
                ledBackManager3.setRearPreview(ledBackManager3.mCameraPreview);
                LedBackManager.this.isCameraPreviewSaved = false;
            }
            if (LedBackManager.this.isCameraCancelSaved) {
                LedBackManager.this.cancelCameraEvent();
                LedBackManager.this.isCameraCancelSaved = false;
            }
            if (LedBackManager.this.isPreviewSaved) {
                LedBackManager ledBackManager4 = LedBackManager.this;
                ledBackManager4.setPreview(ledBackManager4.mPreview);
                LedBackManager.this.isPreviewSaved = false;
            }
            if (LedBackManager.this.isPreviewSettingsSaved) {
                LedBackManager ledBackManager5 = LedBackManager.this;
                ledBackManager5.setPreviewSettings(ledBackManager5.mPreviewSettings, LedBackManager.this.mNFCStatus, LedBackManager.this.mRecoverNFC);
                LedBackManager.this.isPreviewSettingsSaved = false;
            }
            if (LedBackManager.this.isRecordingModeSaved) {
                LedBackManager ledBackManager6 = LedBackManager.this;
                ledBackManager6.setCameraRecordingMode(ledBackManager6.mRecordingMode);
                LedBackManager.this.isRecordingModeSaved = false;
            }
            if (LedBackManager.this.isDataCoverAppSaved) {
                LedBackManager ledBackManager7 = LedBackManager.this;
                ledBackManager7.notifyCoverAppDataChanged(ledBackManager7.mMoodLight, LedBackManager.this.mPictureCue, LedBackManager.this.mCameraTimer);
                LedBackManager.this.isDataCoverAppSaved = false;
            }
            if (LedBackManager.this.isDavinciDataCoverAppSaved) {
                LedBackManager ledBackManager8 = LedBackManager.this;
                ledBackManager8.davinciNotifyCoverAppDataChanged(ledBackManager8.mDavinciLightingStyle, LedBackManager.this.mDavinciTurnOver, LedBackManager.this.mDavinciDLightingTimeOut, LedBackManager.this.mDavinciCameraEmoticon, LedBackManager.this.mDavinciCameraTimer);
                LedBackManager.this.isDavinciDataCoverAppSaved = false;
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            SLog.m664e(LedBackManager.TAG, "onServiceDisconnected");
            LedBackManager.this.mILedBackSdkService = null;
            LedBackManager.this.mServiceConnected = false;
        }
    };
    int mMoodLight;
    int mNFCStatus;
    int mPictureCue;
    int mPreview;
    int mPreviewSettings;
    boolean mRecordingMode;
    boolean mRecoverNFC;
    boolean mServiceConnected;
    ScoverManager scoverManager;

    public LedBackManager(Context context) {
        this.mContext = context;
    }

    public ScoverManager getScoverManager()
    {
        return scoverManager;
    }

    public boolean start() {
        SLog.m663d(TAG, "start");

        scoverManager = new ScoverManager(this.mContext);

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.sec.android.cover.ledcover", "com.sec.android.cover.ledback.LedBackSdkService"));
        this.mLedCoverServiceBound = this.mContext.bindService(intent, this.mLedCoverServiceConnection, 1);

        SLog.m663d(TAG, "start, mLedCoverServiceBound=" + this.mLedCoverServiceBound);

        return true;
    }

    public boolean setCameraTimer(int i, long j) {
        String str = TAG;
        SLog.m663d(str, "setCameraTimer countDownTime: " + i + " beginTime: " + j);
        ILedBackSdkService iLedBackSdkService = this.mILedBackSdkService;
        if (iLedBackSdkService == null || !this.mLedCoverServiceBound) {
            String str2 = TAG;
            SLog.m667w(str2, "setState service not bound; mLedCoverServiceBound=" + this.mLedCoverServiceBound + ", mILedBackSdkService=" + this.mILedBackSdkService);
        } else {
            try {
                iLedBackSdkService.setCameraTimer(i, j);
            } catch (RemoteException e) {
                SLog.m665e(TAG, "setState Error", e);
            }
        }
        return this.mLedCoverServiceBound;
    }

    public boolean setCameraPendingTakeShot() {
        SLog.m663d(TAG, "setCameraPendingTakeShot");
        ILedBackSdkService iLedBackSdkService = this.mILedBackSdkService;
        if (iLedBackSdkService == null || !this.mLedCoverServiceBound) {
            String str = TAG;
            SLog.m667w(str, "setState service not bound; mLedCoverServiceBound=" + this.mLedCoverServiceBound + ", mILedBackSdkService=" + this.mILedBackSdkService);
        } else {
            try {
                iLedBackSdkService.setCameraPendingTakeShot();
            } catch (RemoteException e) {
                SLog.m665e(TAG, "setState Error", e);
            }
        }
        return this.mLedCoverServiceBound;
    }

    public boolean cancelCameraEvent() {
        SLog.m663d(TAG, "cancelCameraEvent");
        ILedBackSdkService iLedBackSdkService = this.mILedBackSdkService;
        if (iLedBackSdkService == null || !this.mLedCoverServiceBound) {
            if (!this.mServiceConnected) {
                this.isCameraCancelSaved = true;
            }
            String str = TAG;
            SLog.m667w(str, "setState service not bound; mLedCoverServiceBound=" + this.mLedCoverServiceBound + ", mILedBackSdkService=" + this.mILedBackSdkService);
        } else {
            try {
                iLedBackSdkService.cancelCameraEvent();
            } catch (RemoteException e) {
                SLog.m665e(TAG, "setState Error", e);
            }
        }
        return this.mLedCoverServiceBound;
    }

    public boolean setCameraOrientation(int i) {
        SLog.m663d(TAG, "setCameraOrientation");
        ILedBackSdkService iLedBackSdkService = this.mILedBackSdkService;
        if (iLedBackSdkService == null || !this.mLedCoverServiceBound) {
            if (!this.mServiceConnected) {
                this.mCameraOrientation = i;
                this.isCameraOrientationSaved = true;
            }
            String str = TAG;
            SLog.m667w(str, "setState service not bound; mLedCoverServiceBound=" + this.mLedCoverServiceBound + ", mILedBackSdkService=" + this.mILedBackSdkService);
        } else {
            try {
                iLedBackSdkService.setCameraOrientation(i);
            } catch (RemoteException e) {
                SLog.m665e(TAG, "setState Error", e);
            }
        }
        return this.mLedCoverServiceBound;
    }

    public boolean setPreview(int i) {
        SLog.m663d(TAG, "setPreview");
        ILedBackSdkService iLedBackSdkService = this.mILedBackSdkService;

        if (iLedBackSdkService == null || !this.mLedCoverServiceBound) {
            if (!this.mServiceConnected) {
                this.mPreview = i;
                this.isPreviewSaved = true;
            }
            String str = TAG;
            SLog.m667w(str, "setState service not bound; mLedCoverServiceBound=" + this.mLedCoverServiceBound + ", mILedBackSdkService=" + this.mILedBackSdkService);
        } else {
            try {
                iLedBackSdkService.setPreview(i);
            } catch (RemoteException e) {
                SLog.m665e(TAG, "setState Error", e);
            }
        }
        return this.mLedCoverServiceBound;
    }

    public boolean setPreviewSettings(int i, int i2, boolean z) {
        String str = TAG;
        SLog.m663d(str, "setPreviewSettings: predefineId = " + i + " nfcStatus = " + i2 + " recoverNFC = " + z);
        ILedBackSdkService iLedBackSdkService = this.mILedBackSdkService;
        if (iLedBackSdkService == null || !this.mLedCoverServiceBound) {
            if (!this.mServiceConnected) {
                this.mPreviewSettings = i;
                this.mNFCStatus = i2;
                this.mRecoverNFC = z;
                this.isPreviewSettingsSaved = true;
            }
            String str2 = TAG;
            SLog.m667w(str2, "setState service not bound; mLedCoverServiceBound=" + this.mLedCoverServiceBound + ", mILedBackSdkService=" + this.mILedBackSdkService);
        } else {
            try {
                iLedBackSdkService.setPreviewSettings(i, i2, z);
            } catch (RemoteException e) {
                SLog.m665e(TAG, "setState Error", e);
            }
        }
        return this.mLedCoverServiceBound;
    }

    public boolean setRearPreview(boolean z) {
        String str = TAG;
        SLog.m663d(str, "setRearPreview: " + z);
        ILedBackSdkService iLedBackSdkService = this.mILedBackSdkService;
        if (iLedBackSdkService == null || !this.mLedCoverServiceBound) {
            if (!this.mServiceConnected) {
                this.mCameraPreview = z;
                this.isCameraPreviewSaved = true;
            }
            String str2 = TAG;
            SLog.m667w(str2, "setState service not bound; mLedCoverServiceBound=" + this.mLedCoverServiceBound + ", mILedBackSdkService=" + this.mILedBackSdkService);
        } else {
            try {
                iLedBackSdkService.setRearPreview(z);
            } catch (RemoteException e) {
                SLog.m665e(TAG, "setState Error", e);
            }
        }
        return this.mLedCoverServiceBound;
    }

    public boolean startLEDVideoRecording() {
        SLog.m663d(TAG, "startLEDVideoRecording");
        ILedBackSdkService iLedBackSdkService = this.mILedBackSdkService;
        if (iLedBackSdkService == null || !this.mLedCoverServiceBound) {
            String str = TAG;
            SLog.m667w(str, "setState service not bound; mLedCoverServiceBound=" + this.mLedCoverServiceBound + ", mILedBackSdkService=" + this.mILedBackSdkService);
        } else {
            try {
                iLedBackSdkService.startLEDVideoRecording();
            } catch (RemoteException e) {
                SLog.m665e(TAG, "setState Error", e);
            }
        }
        return this.mLedCoverServiceBound;
    }

    public boolean stopLEDVideoRecording() {
        SLog.m663d(TAG, "stopLEDVideoRecording");
        ILedBackSdkService iLedBackSdkService = this.mILedBackSdkService;
        if (iLedBackSdkService == null || !this.mLedCoverServiceBound) {
            String str = TAG;
            SLog.m667w(str, "setState service not bound; mLedCoverServiceBound=" + this.mLedCoverServiceBound + ", mILedBackSdkService=" + this.mILedBackSdkService);
        } else {
            try {
                iLedBackSdkService.stopLEDVideoRecording();
            } catch (RemoteException e) {
                SLog.m665e(TAG, "setState Error", e);
            }
        }
        return this.mLedCoverServiceBound;
    }

    public boolean setCameraRecordingMode(boolean z) {
        String str = TAG;
        SLog.m663d(str, "cameraModeChange mode = " + z);
        ILedBackSdkService iLedBackSdkService = this.mILedBackSdkService;
        if (iLedBackSdkService == null || !this.mLedCoverServiceBound) {
            if (!this.isRecordingModeSaved) {
                this.mRecordingMode = z;
                this.isRecordingModeSaved = true;
            }
            String str2 = TAG;
            SLog.m667w(str2, "setState service not bound; mLedCoverServiceBound=" + this.mLedCoverServiceBound + ", mILedBackSdkService=" + this.mILedBackSdkService);
        } else {
            try {
                iLedBackSdkService.setCameraRecordingMode(z);
            } catch (RemoteException e) {
                SLog.m665e(TAG, "setState Error", e);
            }
        }
        return this.mLedCoverServiceBound;
    }

    public boolean notifyCoverAppDataChanged(int i, int i2, int i3) {
        String str = TAG;
        SLog.m663d(str, "notifyCoverAppDataChanged: " + i + ", " + i2 + ", " + i3);
        ILedBackSdkService iLedBackSdkService = this.mILedBackSdkService;
        if (iLedBackSdkService == null || !this.mLedCoverServiceBound) {
            if (!this.mServiceConnected) {
                this.mMoodLight = i;
                this.mPictureCue = i2;
                this.mCameraTimer = i3;
                this.isDataCoverAppSaved = true;
            }
            String str2 = TAG;
            SLog.m667w(str2, "setState service not bound; mLedCoverServiceBound=" + this.mLedCoverServiceBound + ", mILedBackSdkService=" + this.mILedBackSdkService);
        } else {
            try {
                iLedBackSdkService.notifyCoverAppDataChanged(i, i2, i3);
            } catch (RemoteException e) {
                SLog.m665e(TAG, "setState Error", e);
            }
        }
        return this.mLedCoverServiceBound;
    }

    public boolean davinciNotifyCoverAppDataChanged(int i, boolean z, int i2, int i3, boolean z2) {
        String str = TAG;
        SLog.m663d(str, "davinciNotifyCoverAppDataChanged: lightingStyle = " + i + ", turnOver = " + z + ", lightingTimeOut = " + i2 + ", cameraEmoticon = " + i3 + ", cameraTimer = " + z2);
        ILedBackSdkService iLedBackSdkService = this.mILedBackSdkService;
        if (iLedBackSdkService == null || !this.mLedCoverServiceBound) {
            if (!this.mServiceConnected) {
                this.mDavinciLightingStyle = i;
                this.mDavinciTurnOver = z;
                this.mDavinciDLightingTimeOut = i2;
                this.mDavinciCameraEmoticon = i3;
                this.mDavinciCameraTimer = z2;
                this.isDavinciDataCoverAppSaved = true;
            }
            String str2 = TAG;
            SLog.m667w(str2, "setState service not bound; mLedCoverServiceBound=" + this.mLedCoverServiceBound + ", mILedBackSdkService=" + this.mILedBackSdkService);
        } else {
            try {
                iLedBackSdkService.davinciNotifyCoverAppDataChanged(i, z, i2, i3, z2);
            } catch (RemoteException e) {
                SLog.m665e(TAG, "setState Error", e);
            }
        }
        return this.mLedCoverServiceBound;
    }

    public void end() {
        SLog.m663d(TAG, "end");
        if (this.mILedBackSdkService == null || !this.mLedCoverServiceBound) {
            String str = TAG;
            SLog.m667w(str, "end service not bound; mLedCoverServiceBound=" + this.mLedCoverServiceBound + ", mILedBackSdkService=" + this.mILedBackSdkService);
            return;
        }
        this.mContext.unbindService(this.mLedCoverServiceConnection);
        this.mLedCoverServiceBound = false;
        this.isCameraOrientationSaved = false;
        this.isCameraCancelSaved = false;
        this.isCameraPreviewSaved = false;
        this.isPreviewSaved = false;
        this.isPreviewSettingsSaved = false;
        this.isRecordingModeSaved = false;
        this.isDataCoverAppSaved = false;
        this.isDavinciDataCoverAppSaved = false;
    }
}