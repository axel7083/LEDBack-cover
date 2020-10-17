package com.custom.ledcover.utils;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.util.Log;


//In order to be allow to run those commands you need to granted "android.permission.WRITE_SECURE_SETTINGS" permission (Using adb or Root)
public class NfcCommands {

    private NfcAdapter mNfcAdapter;
    private Context mContext;

    private static final byte[] VERSION_CHECK_COMMAND = {0, -95, 0, 0, 4, 4, 113, 0, 0};

    public NfcCommands(Context mContext) {
        this.mContext = mContext;

        mNfcAdapter = NfcAdapter.getDefaultAdapter(mContext);
    }

    //Using adb privilege :
    //10-11 14:15:53.922  2462  2560 D LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 298] Started NFC LED Cover
    //10-11 14:15:53.922  2462  2560 D LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 499] handleSendDataToLedCover new state to transceive is: NEW_MISSED_EVENT
    //10-11 14:15:53.922  2462  2560 D LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 500] Firmware version: 0E 00 00 06
    //10-11 14:15:53.923  2462  2560 D LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 513] handleSendDataToLedCover : transceive data : 00 A1 00 00 05 05 09 00 02 00 //message notification
    public boolean transceiveVersionCheck() {
        boolean validResponse;
        byte[] response = this.mNfcAdapter.semTransceiveDataWithLedCover(VERSION_CHECK_COMMAND);
        Log.d("transceiveVersionCheck", "Version check response: " + getByteDataString(response));
        if (response == null || response.length < 5 || response[0] != VERSION_CHECK_COMMAND[6]) {
            validResponse = false;
        } else {
            validResponse = true;
        }
        if (validResponse) {
            System.out.println("mFirmwareVersion: " + String.format("%02X %02X %02X %02X", Byte.valueOf(response[1]), Byte.valueOf(response[2]), Byte.valueOf(response[3]), Byte.valueOf(response[4])));
        }
        return validResponse;
    }

    private String getByteDataString(byte[] data) {
        if (data == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(String.format("%02X", Byte.valueOf(data[i]))).append(" ");
        }
        return sb.toString();
    }

    public static byte[] getByte(String str)
    {
        String[] strs = str.split(" ");
        byte[] bytes = new byte[strs.length];
        for (int i  = 0; i < strs.length;i ++)
        {
            bytes[i] = (byte) Integer.parseInt(strs[i], 16);
        }
        return bytes;
    }


    public void sendDataToCase(byte[] data)
    {
        byte[] returnValue = null;
        try {
            returnValue = this.mNfcAdapter.semTransceiveDataWithLedCover(data);
            if (returnValue != null) {
                Log.e("MainActivity", "Response data: " + getByteDataString(returnValue));
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error in trancieve command", e);
        }
    }

    //Data extracted from logs:

    //SMS Notification icon
    //LEDCoverService: [LCS] CoverUpdateMonitor [Line : 118] handleMessage msg={ when=-1ms what=302 obj=status=2 / plugged=2 / level=74 / health=2 / batteryOnline=4 / highVoltage=false target=com.sec.android.cover.monitor.CoverUpdateMonitor$2 }
    //LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 513] handleSendDataToLedCover : transceive data : 00 A1 00 00 05 05 09 00 02 00


    //LEDCoverService: [LCS] CoverUpdateMonitor msg={ when=0 what=343 arg1=3 arg2=-1 obj=false target=com.sec.android.cover.monitor.CoverUpdateMonitor$2 } Smile
    //LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 513] handleSendDataToLedCover : transceive data : 00 A1 00 00 06 06 17 00 03 00 01

    //LEDCoverService: [LCS] CoverUpdateMonitor msg={ when=0 what=343 arg1=2 arg2=-1 obj=false target=com.sec.android.cover.monitor.CoverUpdateMonitor$2 } Coeur
    //LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 513] handleSendDataToLedCover : transceive data : 00 A1 00 00 06 06 17 00 02 00 01

    //LEDCoverService: [LCS] CoverUpdateMonitor msg={ when=0 what=343 arg1=1 arg2=-1 obj=false target=com.sec.android.cover.monitor.CoverUpdateMonitor$2 } Oiseau
    //LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 513] handleSendDataToLedCover : transceive data : 00 A1 00 00 06 06 17 00 01 00 01

    //LEDCoverService: [LCS] CoverUpdateMonitor msg={ when=0 what=343 arg1=4 arg2=-1 obj=false target=com.sec.android.cover.monitor.CoverUpdateMonitor$2 } Pouls
    //LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 513] handleSendDataToLedCover : transceive data : 00 A1 00 00 06 06 17 00 04 00 01

    //LEDCoverService: [LCS] CoverUpdateMonitor msg={ when=0 what=343 arg1=5 arg2=-1 obj=false target=com.sec.android.cover.monitor.CoverUpdateMonitor$2 } Infini
    //LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 513] handleSendDataToLedCover : transceive data : 00 A1 00 00 06 06 17 00 05 00 01

    //LEDCoverService: [LCS] CoverUpdateMonitor msg={ when=0 what=343 arg1=6 arg2=-1 obj=false target=com.sec.android.cover.monitor.CoverUpdateMonitor$2 } Petit Monstre
    //LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 513] handleSendDataToLedCover : transceive data : 00 A1 00 00 06 06 17 00 06 00 01

    //LEDCoverService: [LCS] CoverUpdateMonitor msg={ when=0 what=343 arg1=7 arg2=-1 obj=false target=com.sec.android.cover.monitor.CoverUpdateMonitor$2 } Chien
    //LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 513] handleSendDataToLedCover : transceive data : 00 A1 00 00 06 06 17 00 07 00 01

    //LEDCoverService: [LCS] CoverUpdateMonitor msg={ when=0 what=343 arg1=8 arg2=-1 obj=false target=com.sec.android.cover.monitor.CoverUpdateMonitor$2 } Elephan
    //LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 513] handleSendDataToLedCover : transceive data : 00 A1 00 00 06 06 17 00 08 00 01

    //LEDCoverService: [LCS] CoverUpdateMonitor msg={ when=0 what=343 arg1=9 arg2=-1 obj=false target=com.sec.android.cover.monitor.CoverUpdateMonitor$2 } Paquet cadeau
    //LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 513] handleSendDataToLedCover : transceive data : 00 A1 00 00 06 06 17 00 09 00 01

    //LEDCoverService: [LCS] CoverUpdateMonitor msg={ when=0 what=343 arg1=10 arg2=-1 obj=false target=com.sec.android.cover.monitor.CoverUpdateMonitor$2 } Chope de bière
    //LEDCoverService: [LCS] LedBackPowerOnOffStateController [Line : 513] handleSendDataToLedCover : transceive data : 00 A1 00 00 06 06 17 00 0A 00 01




    //More advanced privilege require to send "notification bundle" to case
    //10-11 14:43:52.351 28425 28425 D MainActivity: makeNotificationBundle
    //10-11 14:43:52.352  1031  4151 D SEP_UNION_CoverManager_CoverManagerWhiteLists: isAllowedToUse : cover manager white lists does not include this App : com.custom.moretests
    //10-11 14:43:52.352  1031  4151 W SEP_UNION_CoverManager_CoverManagerServiceImpl: addLedNotification : caller is invalid
}
