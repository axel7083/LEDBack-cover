package com.custom.ledcover.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.custom.ledcover.R;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private Dialog d;
    private Button no;
    private RadioGroup radioDialog;

    private PInfo pInfo;

    private int selected;


    private Callback callback;

    public CustomDialogClass(int selected, Activity a, PInfo pInfo, Callback callback) {
        super(a);

        this.selected = selected;

        this.c = a;
        this.pInfo = pInfo;
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        if(selected != R.id.none && selected != -1)
            ((RadioButton) findViewById(selected)).setChecked(true);

        radioDialog = findViewById(R.id.radioDialog);
        radioDialog.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.btn_no)
                    callback.onResult(null);
                else {
                    pInfo.notification = checkedId;
                    callback.onResult(pInfo);
                }
                dismiss();
            }
        });

        ImageView dialogIcon = findViewById(R.id.dialogIcon);
        dialogIcon.setImageDrawable(pInfo.icon);

        TextView dialogTitle = findViewById(R.id.dialogTitle);
        dialogTitle.setText(String.format("Select an animation to play when you receive a notification from %s.", pInfo.appname));


        no = (Button) findViewById(R.id.btn_no);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_no) {
            callback.onResult(null);
            dismiss();
        }
        dismiss();
    }

    /*public int getIndex(int val)
    {
        switch (val)
        {
            case R.id.none:

                break;
            case R.id.smile:

                break;

            case R.id.heart:

                break;

            case R.id.bird:

                break;

            case R.id.pulse:

                break;

            case R.id.infinity:

                break;

            case R.id.pet_blob:

                break;

            case R.id.dog:

                break;

            case R.id.elephant:

                break;

            case R.id.gift_box:

                break;
            case R.id.beer_mug:

                break;
        }

    }*/

    interface Callback {
        void onResult(PInfo pInfo);
    }
}