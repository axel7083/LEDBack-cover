package com.custom.ledcover.experimental;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class AsyncTaskC0945a extends AsyncTask<Integer,Integer,Void> {
//2020-10-16 14:28:40.968 11276-21200/com.custom.ledcover I/doInBackground: path: /data/user/0/com.custom.ledcover/files/OFFICIAL_EF_KG97X_006_00E_181211_FW.zip
//2020-10-16 14:28:41.366 11276-21200/com.custom.ledcover I/[doInBackground]: [FILE] /data/user/0/com.custom.ledcover/app_test/A31T414_BACKCOVER_V0E_181211_51D8.hex
//2020-10-16 14:28:41.367 11276-21200/com.custom.ledcover I/[doInBackground]: [FILE] /data/user/0/com.custom.ledcover/app_test/Accessory_FlipCover_DREAM_TESTFW_ver0006_4F88H.hex

    private Context context;
    private ProgressBar flashIndicator;

    private boolean f892e = false; //isSemEnable
    private int f891d = -1; //semGetAdapterState
    private int f883l = -1; // Progress update
    int f917a = 0;
    private int f908v; //Writing progress
    private String checkSum;


    private String f911y; // A31T414_BACKCOVER_V0E_181211_51D8.hex
    private String f910x; // Accessory_FlipCover_DREAM_TESTFW_ver0006_4F88H.hex

    private boolean f912z;

    private String f902p;

    private byte[] f904r;
    private byte[] f909w;

    private int f903q;
    private long f899m;

    private int f901o;

    private NfcAdapter f905s;


    public AsyncTaskC0945a(ArrayList arrayList, Context context, ProgressBar flashIndicator) {

        this.context = context;
        this.flashIndicator = flashIndicator;

        this.f905s = NfcAdapter.getDefaultAdapter(context);
        m1096i();

        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str != null) {
                if (str.contains("Accessory")) {
                    this.f910x = str;
                } else if (str.contains("A31T414")) {
                    this.f911y = str;
                    File file = new File(str);
                    this.f899m = file.length() + this.f899m;
                }
            }
            if (this.f910x != null && this.f911y != null) {
                break;
            }
        }

    }

    /* renamed from: a */
    private void m1125a() throws IOException {
        Log.i("[LED_BACK]LCoverFOTAUpdate] ","Starting ");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(f911y)));
        f904r = new byte[64];
        f903q = 0;
        f901o = 0;

        boolean z = false;
        boolean z2 = true; //Stop condition

        while (z2) {
            String readLine = bufferedReader.readLine();
            Log.i("[LED_BACK]LCoverFOTAUpdate","readLineBEFORE "+readLine);
            //readLine = createValidString(readLine);
            //Log.i("[LED_BACK]LCoverFOTAUpdate","readLineAFTER "+readLine);

            if (readLine == null || !z2) { //Stop condition
                Log.i("[LED_BACK]LCoverFOTAUpdate]","Stopping");
                bufferedReader.close();
                z2 = false;
            } else {
                C0930b bVar = new C0930b(readLine);
                String c = bVar.mo9163c();
                bVar.mo9161a();
                int b = bVar.mo9162b();
                if (!z) {
                    checkSum = readLine.substring(25, 29); //CHECK SUM
                    Log.i("[LED_BACK]LCoverFOTAUpdate", "mcu checksum = " + checkSum);
                    z = true;
                }

                //Log.i("[LED_BACK]LCoverFOTAUpdate", "oneline = " + readLine + " address = " + c + " addValue = " + b);
                if (c.equals("1000")) {
                    this.f912z = true;
                }

                if (this.f912z && b != 0) {
                    if (f901o == 0) {
                        this.f902p = c;
                    }
                    f903q = this.mo9199a(bVar.mo9164d()) + this.f903q;
                    m1107s();
                    if (f901o == 4) {
                        byte[] a = utils.m1035a(this.f902p, this.f904r);

                        /*if(a.length>10)
                        for(int i = 5 ; i < a.length-3; i++)
                        {
                            a[i] = (byte) randInt(-64,64);
                        }*/

                        f901o = 0;
                        f903q = 0;
                        this.f904r = new byte[64];
                        this.f909w = this.f905s.semTransceiveDataWithLedCover(a);
                        if (utils.checkResponse(this.f909w)) {
                            Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "[MCU FOTA] Error Code : " + utils.BAtoString(this.f909w));
                            z2 = false;
                        }
                        Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "Write Flash Address : " + this.f902p);
                        Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "Write Flash oneLine : " + utils.BAtoString(a));
                        Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "Write Flash Res : " + utils.BAtoString(this.f909w));
                    }
                }
                this.f917a = readLine.getBytes().length + this.f917a + "\r\n".getBytes().length;
                this.f908v = (int) ((((double) this.f917a) / ((double) this.f899m)) * 100.0d);
                //Log.i("PROGRESS",this.f908v + "%");
                publishProgress(Integer.valueOf(this.f908v));
            }
        }
        bufferedReader.close();
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    @Override
    public Void doInBackground(Integer... numArr) {
        try {
            if (this.f911y == null) {
                return null;
            }
            if (utils.prepareMCU(this.f905s)) {
                Log.i("[LED_BACK]LCoverFOTAUpdate","Waiting");
                for (int i = 1; i <= 15; i++) {
                    Thread.sleep((long) 100);
                }

                this.f891d = this.f905s.semGetAdapterState();

                m1125a();
                return null;
            }

            //this.m1092g();
            f883l = -1;
            Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "Fail MCU FOTA");
            return null;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            f883l = -1;
            return null;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public void onPostExecute(Void r5) {
        Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "onPostExecute() mUpdateProgress = " + f883l);
        super.onPostExecute(r5);
        if (this.f911y != null && f883l > -1) {
            Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "Everything seems good.");
            utils.stopNFCMode(this.f905s, this.checkSum);
        }
        if (this.f891d == -1) {
                //LCoverFOTAUpdate.this.f887D.send(Message.obtain((Handler) null, 0));
                Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "onPostExecute() - stopSelf");
                //LCoverFOTAUpdate.this.stopSelf();
        }
        Log.i("PROGRESS","Value:"  + f883l);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void onProgressUpdate(Integer... numArr) {
        if (utils.checkResponse(this.f909w)) {
            Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "[onProgressUpdate] :  + Error Code");
        }
        Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "Update Flash process : " + numArr[0] + " updateprocess = " + f883l);
        if (f883l != ((numArr[0].intValue() * 90) / 100) + 10) {
            int unused = f883l = ((numArr[0].intValue() * 90) / 100) + 10;
            Log.i("PROGRESS",f883l + "%");
            flashIndicator.setProgress(f883l);
        }
    }

    /* access modifiers changed from: protected */
    public void onCancelled() {
        Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "[onCancelled]");
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        this.f908v = 0;
        if (this.f911y != null) {
            Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "[onPreExecute] : OK" );

        }
    }

    public int mo9199a(byte[] bArr) {
        int length = bArr.length;
        System.arraycopy(bArr, 0, this.f904r, this.f903q, bArr.length);
        return length;
    }

    /* renamed from: s */
    private /* synthetic */ int m1107s() {
        int i = f901o;
        f901o++;
        return i;
    }

    /* renamed from: i */
    private void m1096i() {
        Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "restoreNFCState mNFCPreviousState = " + this.f891d);
        if (this.f891d != -1) {
            this.f892e = true;
            mo9201a(context, true);
            if (this.f891d == 5) {
                NfcAdapter defaultAdapter = NfcAdapter.getDefaultAdapter(context);
                try {
                    Method declaredMethod = Class.forName("android.nfc.NfcAdapter").getDeclaredMethod("semDisableReader", new Class[0]);
                    declaredMethod.setAccessible(true);
                    declaredMethod.invoke(defaultAdapter, new Object[0]);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e2) {
                    e2.printStackTrace();
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                } catch (InvocationTargetException e4) {
                    e4.printStackTrace();
                }
            }
            this.f891d = -1;
        }
    }

    /* renamed from: a */
    public void mo9201a(Context context, boolean z) {
        NfcAdapter defaultAdapter = NfcAdapter.getDefaultAdapter(context);
        Log2.m825a("[LED_BACK]LCoverFOTAUpdate", "turnOnOffNFC isEnable = " + z);
        if (z) {
            defaultAdapter.semEnable();
        } else {
            defaultAdapter.semDisable();
        }
    }

    public static int randInt(int min, int max) {

        Random rand = new Random();;
        return rand.nextInt((max - min) + 1) + min;
    }


    private static String createValidString(String readLine)
    {
        if(readLine == null)
            return null;


        int lengthDecimal = Integer.parseInt(readLine.substring(1, 3), 16);
        StringBuilder readLineBuilder = new StringBuilder();

        for(int i = 0 ; i < lengthDecimal * 2;i++)
        {
            readLineBuilder.append("0");
        }

        while(readLineBuilder.length()<32)
        {
            readLineBuilder.append("0");
        }

        readLine = readLine.substring(0,9)+readLineBuilder.toString()+"00";


        String checkSum = Integer.toHexString(verifyCheckSum(readLine));
        if(checkSum.length() == 1)
            checkSum = "0" + checkSum;

        readLine = readLine.substring(0,readLine.length()-2)+checkSum;

        return readLine.toUpperCase();
    }

    private static int verifyCheckSum(String readLine) {
        int i = 0;
        int length = readLine.length();
        for (int i2 = 1; i2 != length - 2; i2 += 2) {
            i += Integer.parseInt(readLine.substring(i2, i2 + 2), 16);
        }
        return (256 - (i % 256)) % 256;
    }
}
