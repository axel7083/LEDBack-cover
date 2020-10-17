package com.custom.ledcover.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.custom.ledcover.experimental.AsyncTaskC0945a;
import com.custom.ledcover.experimental.utils;
import com.custom.ledcover.experimental.DoInBackground;
import com.custom.ledcover.R;

import java.io.File;
import java.util.ArrayList;

public class AdvancedActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //Layout
    private ConstraintLayout downloadFirmware;
    private ConstraintLayout flashFirmware;
    private ConstraintLayout flashEraseCL;

    //Progress indicator
    private ProgressBar downloadIndicator;
    private ProgressBar flashIndicator;

    //Text information
    private TextView flashEraseTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);


        if(!checkAdvancedPermission())
        {
            Toast.makeText(getApplicationContext(),"WRITE_SECURE_SETTINGS NOT GRANTED",Toast.LENGTH_SHORT).show();

            CardView alertCVAdvanced = findViewById(R.id.alertCVAdvanced);
            CardView advancedContainer = findViewById(R.id.advancedContainer);
            TextView alertDescriptionAdvanced = findViewById(R.id.alertDescriptionAdvanced);

            alertCVAdvanced.setVisibility(View.VISIBLE);
            alertDescriptionAdvanced.setText(getString(R.string.warningAdvanced));

            advancedContainer.setVisibility(View.GONE);

            return;
        }
        else
            Toast.makeText(getApplicationContext(),"WRITE_SECURE_SETTINGS GRANTED",Toast.LENGTH_SHORT).show();

        downloadIndicator = findViewById(R.id.downloadIndicator);
        flashIndicator = findViewById(R.id.flashIndicator);

        if(checkFirmwareAvailable())
            downloadIndicator.setProgress(100);
        else
            downloadIndicator.setProgress(0);

        downloadFirmware = findViewById(R.id.downloadFirmware);
        downloadFirmware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DoInBackground(downloadIndicator,AdvancedActivity.this).execute();
            }
        });

        flashFirmware = findViewById(R.id.flashFirmware);
        flashFirmware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = buildAlert();
                alertDialog.show();
            }
        });

        flashEraseCL = findViewById(R.id.flashEraseCL);
        flashEraseTV = findViewById(R.id.flashEraseTV);
        flashEraseCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NfcAdapter nfcAdapter = NfcAdapter.getNfcAdapter(getApplicationContext());

                if(utils.prepareMCU(nfcAdapter))
                    flashEraseTV.setTextColor(Color.GREEN);
                else
                    flashEraseTV.setTextColor(Color.RED);

                nfcAdapter.semStopLedCoverMode();
            }
        });



    }

    private boolean checkFirmwareAvailable()
    {
        File file = new File("/data/user/0/com.custom.ledcover/app_test/A31T414_BACKCOVER_V0E_181211_51D8.hex");
        return (file.length()>0);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    public boolean checkAdvancedPermission()
    {
        int permission = ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_SECURE_SETTINGS);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private AlertDialog buildAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Flash LedCover firmware");
        alertDialogBuilder.setMessage("Are you sure you want to flash the firmware ? It can breaks your case.");
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),"Flashing will start...",Toast.LENGTH_SHORT).show();
                        //new DoInBackground(downloadIndicator,AdvancedActivity.this).execute();
                        ArrayList<String> strings = new ArrayList<>();
                        strings.add("/data/user/0/com.custom.ledcover/app_test/A31T414_BACKCOVER_V0E_181211_51D8.hex");
                        strings.add("/data/user/0/com.custom.ledcover/app_test/Accessory_FlipCover_DREAM_TESTFW_ver0006_4F88H.hex");

                        //Reset "Flash Erase" color
                        flashEraseTV.setTextColor(Color.BLACK);

                        new AsyncTaskC0945a(strings,getApplicationContext(),flashIndicator).execute();
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),"Cancel.",Toast.LENGTH_SHORT).show();
                    }
                });
        return(alertDialogBuilder.create());
    }
}