package com.custom.ledcover.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.custom.ledcover.R;


public class MainActivity extends AppCompatActivity {


    public static final String PREFS_SETTINGS = "settings";

    private static final String TAG = "MainActivity";
    private SharedPreferences prefs;

    private boolean deviceAlert = true;


    //Dialog to turn on/off the service
    private AlertDialog enableNotificationListenerAlertDialog;


    private DataBroadcastReceiver dataBroadcastReceiver;

    boolean isActivated;
    boolean usingAccelerometer;
    int animationTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //First get the settings
        loadSettings();

        if(!checkDevice() && deviceAlert)
        {
            final CardView alertCV = findViewById(R.id.alertCVMain);
            TextView alertDescription = findViewById(R.id.alertDescriptionMain);
            ImageView alertClose = findViewById(R.id.alertCloseMain);

            alertCV.setVisibility(View.VISIBLE);
            alertDescription.setText(String.format("This application has not been tested on your device (%s).\nYou can give feedback to improve compatibility.",Build.MODEL));

            alertClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertCV.setVisibility(View.GONE);
                    deviceAlert = false;

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("deviceAlert",deviceAlert);
                    editor.apply();
                    notifySettingsUpdate();
                }
            });
        }

        // Checking if the notification Listener service is enable
        if(isActivated && !isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }

        //Listener for opening applications activity
        LinearLayout openApplications = findViewById(R.id.openApplications);
        openApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ApplicationsActivity.class);
                startActivity(intent);
            }
        });

        //Listener for opening applications activity
        LinearLayout openAdvanced = findViewById(R.id.openAdvanced);
        openAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AdvancedActivity.class);
                startActivity(intent);
            }
        });


        //Listener for the NotificationListener service on/off
        SwitchCompat activatedSwitch = findViewById(R.id.activatedSwitch);
        activatedSwitch.setChecked(isActivated);
        activatedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isActivated",isChecked);
                editor.apply();
                notifySettingsUpdate();
            }
        });

        //Listener for the accelerometer settings
        SwitchCompat accelerometerSwitch = findViewById(R.id.accelerometerSwitch);
        accelerometerSwitch.setChecked(usingAccelerometer);
        accelerometerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("usingAccelerometer",isChecked);
                editor.apply();
                notifySettingsUpdate();
            }
        });

        //Listener for
        final EditText animationTimeET = findViewById(R.id.animationTime);
        animationTimeET.setHint(animationTime +  "");
        animationTimeET.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String txt = animationTimeET.getText().toString();
                    if(txt.length() == 0)
                        return true;

                    int val = Integer.parseInt(txt);
                    if(val < 1000)
                        Toast.makeText(getApplicationContext(),val + " is too short.",Toast.LENGTH_SHORT).show();
                    else if(val > 20000)
                        Toast.makeText(getApplicationContext(),val + " is too long.",Toast.LENGTH_SHORT).show();
                    else
                    {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("animationTime",val);
                        editor.apply();
                        notifySettingsUpdate();
                    }

                    return true;
                }
                return false;
            }
        });





        // Finally we register a receiver to tell the MainActivity when a notification has been received
        dataBroadcastReceiver = new DataBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.custom.ledcover.services.NotificationListener");
        registerReceiver(dataBroadcastReceiver,intentFilter);

    }

    private boolean checkDevice() {
       // return (Build.MODEL.contains("SM-G973"));
        return false;
    }

    //Notify the Service that the settings has changed
    private void notifySettingsUpdate()
    {
        Intent intent = new  Intent("com.custom.ledcover.activities.MainActivity");
        intent.putExtra("updateType", 0);
        sendBroadcast(intent);

    }

    private void loadSettings()
    {
        prefs = getSharedPreferences(PREFS_SETTINGS, MODE_PRIVATE);

        isActivated = prefs.getBoolean("isActivated",true);
        usingAccelerometer = prefs.getBoolean("usingAccelerometer",true);
        animationTime = prefs.getInt("animationTime",-1);
        deviceAlert = prefs.getBoolean("deviceAlert",true);

        if(animationTime == -1)
        {
            Log.i(TAG,"First boot detected. Init SharedPreferences");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isActivated",true);
            editor.putBoolean("usingAccelerometer",true);
            editor.putInt("animationTime",5000);
            animationTime = 5000;
            editor.apply();
        }
    }

    /*private void initTestingArea()
    {
        //get the spinner from the xml.
        Spinner iconsSpinner = findViewById(R.id.iconsSpinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"Smile", "Heart", "Bird","Pulse","Infinity","Pet blob","Dog","Elephant","Gift box","Beer mug"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.

        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        iconsSpinner.setAdapter(adapter);


        //get the spinner from the xml.
        Spinner cameraTimerSpinner = findViewById(R.id.cameraTimerSpinner);
        //create a list of items for the spinner.
        String[] items2 = new String[]{"  1 ", "  2 ", "  3 ","  4 ","  5 ","  6 ","  7 ","  8 ","  9 "," 10 "};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.

        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        //set the spinners adapter to the previously created one.
        cameraTimerSpinner.setAdapter(adapter2);
    }*/

    /**
     * Is Notification Service Enabled.
     * Verifies if the notification listener service is enabled.
     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
     * @return True if enabled, false otherwise.
     */
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Build Notification Listener Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Notification Listener Service on yet.
     * @return An alert dialog which leads to the notification enabling screen
     */
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return(alertDialogBuilder.create());
    }


    //Allow communication from service to activity
    public static class DataBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int receivedNotificationCode = intent.getIntExtra("Notification Code",-1);
            System.out.println("RECEIVE DATA FROM SERVICE " + receivedNotificationCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dataBroadcastReceiver);
    }

}