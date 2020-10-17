package com.custom.ledcover.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.custom.ledcover.R;
import com.custom.ledcover.ledback.sdk.LedBackManager;
import java.util.List;
import static com.custom.ledcover.activities.MainActivity.PREFS_SETTINGS;
import static com.custom.ledcover.utils.ApplicationAdapter.MY_PREFS_NAME;

public class NotificationListener extends NotificationListenerService {

    private static String TAG = "NotificationListener";

    private long previousTime = 0;
    private StatusBarNotification statusBarNotification;

    //We are using the Accelerometer Sensor to check if the screen is face down
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean accelerometerPresent;

    //We are using the LedBackManager to send command to the ledBack cover
    private LedBackManager ledBackManager;

    //We are storing the notifications icons which should appear depending on the package name in the SharedPreferences
    private SharedPreferences prefs;
    private SharedPreferences settings;
    private int iconID = -1;

    //To ensure communication between service and Activity we use a broadcast receiver
    private UpdateBroadcastReceiver updateBroadcastReceiver;

    //Settings
    boolean isActivated;
    boolean usingAccelerometer;
    int animationTime;
    private int interval_check = 10000;


    @Override
    public void onCreate() {
        Log.i(TAG,"onCreate");

        //Loading settings
        loadSettings();

        // We register a receiver
        updateBroadcastReceiver = new UpdateBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.custom.ledcover.activities.MainActivity");
        registerReceiver(updateBroadcastReceiver,intentFilter);

        //If not activated we do not init anything.
        if(!isActivated)
            return;

        if(usingAccelerometer)
            setupAccelerometer();

        //Get the preferences
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        //We init the LedBackManager
        ledBackManager = new LedBackManager(getApplicationContext());
        ledBackManager.start();

        super.onCreate();
    }

    private void setupAccelerometer()
    {
        //We register the sensorManager
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(sensorList.size() > 0){
            accelerometerPresent = true;
            accelerometerSensor = sensorList.get(0);
        }
        else{
            accelerometerPresent = false;
            Log.e(TAG,"No accelerometer present!");
        }
    }

    private void loadSettings() {
        settings = getSharedPreferences(PREFS_SETTINGS, MODE_PRIVATE);
        isActivated = settings.getBoolean("isActivated", true);
        usingAccelerometer = settings.getBoolean("usingAccelerometer", true);
        animationTime = settings.getInt("animationTime", -1);
        interval_check = animationTime*2;
    }

        @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        if(!isActivated)
            return;

        long currentTime = System.currentTimeMillis();

        //Instead of checking every notification if the device is facing down
        //we only check every notification with more than 5 seconds in between time to save some battery
        if(currentTime-previousTime > interval_check)
        {
            iconID = prefs.getInt(sbn.getPackageName(), -1); //-1 is the default value.
            statusBarNotification = sbn;

            if(usingAccelerometer && accelerometerPresent) {
                Log.i(TAG,"Activating sensor");
                sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            else {
                Log.i(TAG,"Sending notification");
                sendNotification();
            }
            //Resetting time
            previousTime = System.currentTimeMillis();
        }


    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){  }

    // This is our listener we attach when a notification arrive
    private SensorEventListener accelerometerListener = new SensorEventListener(){

        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) { }

        @Override
        public void onSensorChanged(SensorEvent arg0) {
            float z_value = arg0.values[2];
            if(z_value >= 0)
                return;

            if(statusBarNotification == null)
                return;

            sendNotification();

            //At the end we unregister the sensorListener because we only need one check.
            sensorManager.unregisterListener(accelerometerListener);
        }
    };


    private void sendNotification()
    {
        boolean ledCoverNotif = true;
        Log.i(TAG,"Sending icon to case");
        switch (iconID)
        {
            case R.id.smile:
                Log.i(TAG,"Sending smile");
                ledBackManager.setPreviewSettings(3,1,false);
                break;
            case R.id.heart:
                Log.i(TAG,"Sending heart");
                ledBackManager.setPreviewSettings(2,1,false);
                break;
            case R.id.bird:
                Log.i(TAG,"Sending bird");
                ledBackManager.setPreviewSettings(1,1,false);
                break;
            case R.id.pulse:
                Log.i(TAG,"Sending pulse");
                ledBackManager.setPreviewSettings(4,1,false);
                break;
            case R.id.infinity:
                Log.i(TAG,"Sending infinity");
                ledBackManager.setPreviewSettings(5,1,false);
                break;
            case R.id.pet_blob:
                Log.i(TAG,"Sending pet_blob");
                ledBackManager.setPreviewSettings(6,1,false);
                break;
            case R.id.dog:
                Log.i(TAG,"Sending dog");
                ledBackManager.setPreviewSettings(7,1,false);
                break;
            case R.id.elephant:
                Log.i(TAG,"Sending elephant");
                ledBackManager.setPreviewSettings(8,1,false);
                break;
            case R.id.gift_box:
                Log.i(TAG,"Sending gift_box");
                ledBackManager.setPreviewSettings(9,1,false);
                break;
            case R.id.beer_mug:
                Log.i(TAG,"Sending beer_mug");
                ledBackManager.setPreviewSettings(10,1,false);
                break;
            default:
                ledCoverNotif = false;
                Log.e(TAG,"switch iconID default value: " + iconID);
                break;
        }


        if(ledCoverNotif)
        {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Turning notification off after 3 seconds
                    Log.i(TAG,"Removing notification");
                    ledBackManager.setPreviewSettings(-7,1,false);
                }
            }, animationTime);
        }
    }


    public class UpdateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            int updateType = intent.getIntExtra("updateType",-1);
            switch (updateType)
            {
                case 0: //Update Settings
                    Log.i(TAG,"Updating settings");
                    loadSettings();

                    if(!isActivated)
                        return;

                    if(usingAccelerometer)
                        setupAccelerometer();

                    break;
                case 1: //Update Package/Notification
                    Log.i(TAG,"Updating packages");
                    prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    break;
                case 99:
                    //Testing:

                   // sendNotification();
                    break;
                default:
                    //TODO: This should never happen.
                    break;
            }
            /*int MainActivity = intent.getIntExtra("MainActivity",-1);

            System.out.println("RECEIVE DATA FROM MainActivity " + MainActivity);

            Intent intent2 = new  Intent("com.custom.ledcover.services.NotificationListener");
            intent2.putExtra("Notification Code", 55);
            sendBroadcast(intent2);*/
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateBroadcastReceiver);
    }
}
