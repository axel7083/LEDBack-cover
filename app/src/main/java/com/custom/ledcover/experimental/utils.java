package com.custom.ledcover.experimental;

import android.nfc.NfcAdapter;
import android.os.Build;

/* renamed from: com.samsung.android.app.ledbackcover.c.e */
public class utils {
    /* renamed from: a */
    public static String BAtoString(byte[] bArr) { //Byte array to String
        if (bArr == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02X", Byte.valueOf(bArr[i]))).append(" ");
        }
        return sb.toString();
    }

    /* renamed from: a */
    public static boolean stopNFCMode(NfcAdapter nfcAdapter, String str) {
        Log2.m825a("[LED_BACK]CoverFotaTest", "[stopNfcMode]");
        if (nfcAdapter == null) {
            return false;
        }
        byte[] b = m1038b(str);
        Log2.m825a("[LED_BACK]CoverFotaTest", "MCU CheckSum response: " + BAtoString(b));
        byte[] semTransceiveDataWithLedCover = nfcAdapter.semTransceiveDataWithLedCover(b);
        Log2.m825a("[LED_BACK]CoverFotaTest", "MCU CheckSum response: " + BAtoString(semTransceiveDataWithLedCover));
        if (semTransceiveDataWithLedCover[0] == -112) {
            byte[] semTransceiveDataWithLedCover2 = nfcAdapter.semTransceiveDataWithLedCover(CONST.f816f);
            byte[] f = stopMCUDownload(nfcAdapter);
            CoverRestart(nfcAdapter);
            wakeUpMCU(nfcAdapter);
            boolean semStopLedCoverMode = nfcAdapter.semStopLedCoverMode();
            Log2.m825a("[LED_BACK]CoverFotaTest", "MCU CheckSum Read response: " + BAtoString(semTransceiveDataWithLedCover2));
            Log2.m825a("[LED_BACK]CoverFotaTest", "Change MCU DOWNLOAD EXIT response: " + BAtoString(f));
            Log2.m825a("[LED_BACK]CoverFotaTest", "semStopLedCoverMode response: " + semStopLedCoverMode);
            //Log.i("[LED_BACK]","Sending smile request");
            //nfcAdapter.semTransceiveDataWithLedCover(NfcCommands.getByte("00 A1 00 00 06 06 17 00 03 00 01"));
            return true;
        }
        Log2.m825a("[LED_BACK]CoverFotaTest", "[mcuStopMode] Error Code");
        return false;
    }

    /* renamed from: a */
    public static byte[] checkVersion(NfcAdapter nfcAdapter) {
        byte[] semTransceiveDataWithLedCover = nfcAdapter.semTransceiveDataWithLedCover(CONST.versionCommand);
        Log2.m825a("[LED_BACK]CoverFotaTest", "Version check response: " + BAtoString(semTransceiveDataWithLedCover));
        return semTransceiveDataWithLedCover;
    }

    /* renamed from: a */
    public static byte[] toByteArray(String str) {
        if (str.length() == 0 || str.length() % 2 != 0) {
            return new byte[0];
        }
        byte[] bArr = new byte[(str.length() / 2)];
        int length = bArr.length;
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i);
            bArr[i2] = (byte) ((charAt > '9' ? charAt > 'Z' ? (charAt - 'a') + 10 : (charAt - 'A') + 10 : charAt - '0') << 4);
            int i3 = i + 1;
            char charAt2 = str.charAt(i3);
            bArr[i2] = (byte) (((byte) (charAt2 > '9' ? charAt2 > 'Z' ? (charAt2 - 'a') + 10 : (charAt2 - 'A') + 10 : charAt2 - '0')) | bArr[i2]);
            i = i3 + 1;
        }
        return bArr;
    }

    /* renamed from: a */
    //arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
    public static byte[] m1035a(String str, byte[] bArr) {
        byte[] a = toByteArray(str);
        byte[] bArr2 = new byte[(CONST.f818h.length + bArr.length + a.length)];
        System.arraycopy(CONST.f818h, 0, bArr2, 0, CONST.f818h.length);

        //bArr2 = CONST.f818h;

        System.arraycopy(a, 0, bArr2, CONST.f818h.length, a.length);

        //bArr2 = CONST.f818h + a

        System.arraycopy(bArr, 0, bArr2, a.length + CONST.f818h.length, bArr.length);

        //bArr2 = CONST.f818h + a + bArr

        return bArr2;
    }

    /* renamed from: b */
    public static boolean checkResponse(byte[] bArr) {
        if (bArr == null) {
            return true;
        }
        boolean z = false;
        for (int i = 0; i < CONST.f824n.length; i++) {
            if (bArr[0] == CONST.f824n[i]) {
                z = true;
            }
        }
        return z;
    }

    /* renamed from: b */
    public static byte[] CoverRestart(NfcAdapter nfcAdapter) {
        Log2.m825a("[LED_BACK]CoverFotaTest", "+++ chipReset Start +++");
        Log2.m825a("[LED_BACK]CoverFotaTest", "semStopLedCoverMode response:  " + nfcAdapter.semStopLedCoverMode());
        byte[] semStartLedCoverMode = nfcAdapter.semStartLedCoverMode();
        Log2.m825a("[LED_BACK]CoverFotaTest", "semStartLedCoverMode response : " + BAtoString(semStartLedCoverMode));
        return semStartLedCoverMode;
    }

    /* renamed from: b */
    public static byte[] m1038b(String str) {
        byte[] bArr = Build.MODEL.contains("G96") ? CONST.f815e : CONST.f814d; //My model: SM-G973F
        if (str != null && str.length() == 4) {
            bArr[8] = (byte) (Integer.parseInt(str.substring(0, 2), 16) & 255);
            bArr[9] = (byte) (Integer.parseInt(str.substring(2, 4), 16) & 255);
        }
        return bArr;
    }

    /* renamed from: c */
    public static void wakeUpMCU(NfcAdapter nfcAdapter) {
        Log2.m825a("[LED_BACK]CoverFotaTest", "Sending command twice to the device to wake up MCU");
        Log2.m825a("[LED_BACK]CoverFotaTest", "versionCheckCommand response : " + BAtoString(checkVersion(nfcAdapter)));
        try {
            Thread.currentThread();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log2.m825a("[LED_BACK]CoverFotaTest", "versionCheckCommand2 response : " + BAtoString(checkVersion(nfcAdapter)));
    }

    /* renamed from: d */
    public static boolean prepareMCU(NfcAdapter nfcAdapter) {
        Log2.m825a("[LED_BACK]CoverFotaTest", "[startNfcMode]");
        if (nfcAdapter == null) {
            return false;
        }
        byte[] semStartLedCoverMode = nfcAdapter.semStartLedCoverMode();
        Log2.m825a("[LED_BACK]CoverFotaTest", "semStartLedCoverMode response: " + BAtoString(semStartLedCoverMode));
        if (checkResponse(semStartLedCoverMode)) {
            return false;
        }
        if (semStartLedCoverMode[1] == 85) {
            byte[] e = startMCUDownload(nfcAdapter);
            if (e[0] != 33 && e[0] != 34 && e[0] != 65) {
                return false;
            }
            Log2.m825a("[LED_BACK]CoverFotaTest", "Entering MCU download mode");
            byte[] semTransceiveDataWithLedCover = nfcAdapter.semTransceiveDataWithLedCover(CONST.flashEraseCommand);
            Log2.m825a("[LED_BACK]CoverFotaTest", "Flash Erase response: " + BAtoString(semTransceiveDataWithLedCover));
            return semTransceiveDataWithLedCover != null && semTransceiveDataWithLedCover[0] == -112;
        } else if (semStartLedCoverMode[1] == 90) {
            Log2.m825a("[LED_BACK]CoverFotaTest", "It is not Auth F/W Mode");
            return false;
        } else {
            Log2.m825a("[LED_BACK]CoverFotaTest", "[mcuStartMode] Error Code");
            return false;
        }
    }

    /* renamed from: e */
    public static byte[] startMCUDownload(NfcAdapter nfcAdapter) {
        byte[] semTransceiveDataWithLedCover = nfcAdapter.semTransceiveDataWithLedCover(CONST.mcuDownloadCommand);
        Log2.m825a("[LED_BACK]CoverFotaTest", "[00A40001] Change MUC DOWNLOAD MODE response: " + BAtoString(semTransceiveDataWithLedCover));
        return semTransceiveDataWithLedCover;
    }

    /* renamed from: f */
    public static byte[] stopMCUDownload(NfcAdapter nfcAdapter) {
        byte[] semTransceiveDataWithLedCover = nfcAdapter.semTransceiveDataWithLedCover(CONST.mcuExitDownloadCommand);
        //Log2.m825a("[LED_BACK]CoverFotaTest", "[00A40001] Change MUC DOWNLOAD EXIT response: " + BAtoString(semTransceiveDataWithLedCover));
        return semTransceiveDataWithLedCover;
    }
}