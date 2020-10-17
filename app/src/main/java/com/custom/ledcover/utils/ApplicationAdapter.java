package com.custom.ledcover.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.custom.ledcover.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> implements CustomDialogClass.Callback {

    public static final String MY_PREFS_NAME = "packageNotifications";


    private List<PInfo> mApplications;
    private LayoutInflater mInflater;
    private Context mContext;
    private Activity mActivity;

    private SharedPreferences prefs;

    // data is passed into the constructor
    public ApplicationAdapter(Context context,Activity activity, ArrayList<PInfo> data) {
        this.mContext = context;
        this.mActivity = activity;
        this.mInflater = LayoutInflater.from(context);
        this.mApplications = data;

        prefs = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    }

    public void updateData(ArrayList<PInfo> data)
    {
        mApplications = data;
        notifyDataSetChanged();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.application_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.applicationName.setText(mApplications.get(position).appname);
        holder.applicationIcon.setImageDrawable(mApplications.get(position).icon);

        final int notifVal = prefs.getInt(mApplications.get(position).pname,-1);

        holder.applicationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,mApplications.get(position).appname  + " selected",Toast.LENGTH_SHORT).show();


                CustomDialogClass cdd=new CustomDialogClass(notifVal,mActivity,mApplications.get(position),ApplicationAdapter.this);

                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                cdd.show();
                Window window = cdd.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mApplications.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView applicationName ;
        ImageView applicationIcon;
        LinearLayout applicationLayout;

        ViewHolder(View itemView) {
            super(itemView);
            applicationLayout = itemView.findViewById(R.id.applicationLayout);
            applicationName = itemView.findViewById(R.id.applicationName);
            applicationIcon = itemView.findViewById(R.id.applicationIcon);
        }
    }

    @Override
    public void onResult(PInfo pInfo) {
        if(pInfo == null)
            return;

        if(pInfo.notification == -1)
            return;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(pInfo.pname, pInfo.notification);
        editor.apply();

        //Update data here
        prefs = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        Intent intent = new  Intent("com.custom.moretests.activities.MainActivity");
        intent.putExtra("updateType", 1);
        mActivity.sendBroadcast(intent);

    }



}