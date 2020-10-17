package com.custom.ledcover.ledback;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ILedBackSdkService extends IInterface {

    void cancelCameraEvent() throws RemoteException;

    void test();

    void davinciNotifyCoverAppDataChanged(int i, boolean z, int i2, int i3, boolean z2) throws RemoteException;

    void notifyCoverAppDataChanged(int i, int i2, int i3) throws RemoteException;

    void setCameraOrientation(int i) throws RemoteException;

    void setCameraPendingTakeShot() throws RemoteException;

    void setCameraRecordingMode(boolean z) throws RemoteException;

    void setCameraTimer(int i, long j) throws RemoteException;

    void setPreview(int i) throws RemoteException;

    void setPreviewSettings(int i, int i2, boolean z) throws RemoteException;

    void setRearPreview(boolean z) throws RemoteException;

    void startLEDVideoRecording() throws RemoteException;

    void stopLEDVideoRecording() throws RemoteException;

    public static abstract class Stub extends Binder implements ILedBackSdkService {
        private static final String DESCRIPTOR = "com.sec.android.cover.ledback.ILedBackSdkService";
        static final int TRANSACTION_cancelCameraEvent = 3;
        static final int TRANSACTION_davinciNotifyCoverAppDataChanged = 12;
        static final int TRANSACTION_notifyCoverAppDataChanged = 11;
        static final int TRANSACTION_setCameraOrientation = 4;
        static final int TRANSACTION_setCameraPendingTakeShot = 2;
        static final int TRANSACTION_setCameraRecordingMode = 10;
        static final int TRANSACTION_setCameraTimer = 1;
        static final int TRANSACTION_setPreview = 5;
        static final int TRANSACTION_setPreviewSettings = 6;
        static final int TRANSACTION_setRearPreview = 7;
        static final int TRANSACTION_startLEDVideoRecording = 8;
        static final int TRANSACTION_stopLEDVideoRecording = 9;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ILedBackSdkService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ILedBackSdkService)) {
                return new Proxy(iBinder);
            }
            return (ILedBackSdkService) queryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1598968902) {
                boolean z = false;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(DESCRIPTOR);
                        setCameraTimer(parcel.readInt(), parcel.readLong());
                        parcel2.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(DESCRIPTOR);
                        setCameraPendingTakeShot();
                        parcel2.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(DESCRIPTOR);
                        cancelCameraEvent();
                        parcel2.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(DESCRIPTOR);
                        setCameraOrientation(parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(DESCRIPTOR);
                        setPreview(parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(DESCRIPTOR);
                        int readInt = parcel.readInt();
                        int readInt2 = parcel.readInt();
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        setPreviewSettings(readInt, readInt2, z);
                        parcel2.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        setRearPreview(z);
                        parcel2.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(DESCRIPTOR);
                        startLEDVideoRecording();
                        parcel2.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(DESCRIPTOR);
                        stopLEDVideoRecording();
                        parcel2.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(DESCRIPTOR);
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        setCameraRecordingMode(z);
                        parcel2.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(DESCRIPTOR);
                        notifyCoverAppDataChanged(parcel.readInt(), parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(DESCRIPTOR);
                        davinciNotifyCoverAppDataChanged(parcel.readInt(), parcel.readInt() != 0, parcel.readInt(), parcel.readInt(), parcel.readInt() != 0);
                        parcel2.writeNoException();
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        /* access modifiers changed from: private */
        public static class Proxy implements ILedBackSdkService {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override
            public void test() {

            }

            @Override // com.sec.android.cover.ledback.ILedBackSdkService
            public void setCameraTimer(int i, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.sec.android.cover.ledback.ILedBackSdkService
            public void setCameraPendingTakeShot() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.sec.android.cover.ledback.ILedBackSdkService
            public void cancelCameraEvent() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.sec.android.cover.ledback.ILedBackSdkService
            public void setCameraOrientation(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.sec.android.cover.ledback.ILedBackSdkService
            public void setPreview(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.sec.android.cover.ledback.ILedBackSdkService
            public void setPreviewSettings(int i, int i2, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.sec.android.cover.ledback.ILedBackSdkService
            public void setRearPreview(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.sec.android.cover.ledback.ILedBackSdkService
            public void startLEDVideoRecording() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.sec.android.cover.ledback.ILedBackSdkService
            public void stopLEDVideoRecording() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.sec.android.cover.ledback.ILedBackSdkService
            public void setCameraRecordingMode(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.sec.android.cover.ledback.ILedBackSdkService
            public void notifyCoverAppDataChanged(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.sec.android.cover.ledback.ILedBackSdkService
            public void davinciNotifyCoverAppDataChanged(int i, boolean z, int i2, int i3, boolean z2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    int i4 = 1;
                    obtain.writeInt(z ? 1 : 0);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    if (!z2) {
                        i4 = 0;
                    }
                    obtain.writeInt(i4);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}