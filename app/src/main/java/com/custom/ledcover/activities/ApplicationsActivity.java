package com.custom.ledcover.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.custom.ledcover.R;
import com.custom.ledcover.utils.ApplicationAdapter;
import com.custom.ledcover.utils.PInfo;

import java.util.ArrayList;
import java.util.List;

public class ApplicationsActivity extends AppCompatActivity {

    //Ui elements
    private ProgressBar LoadingIndicator;
    private RecyclerView applications;
    private ApplicationAdapter applicationAdapter;

    private ImageView applicationsSearchIcon;
    private TextView applicationsTitle;
    private EditText applicationsSearch;
    private ImageView onBackApplications;

    //Applications list
    private ArrayList<PInfo> apps;

    private boolean isSearchOpen = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);

        // Setup the loading bar (To indicate something is running in background)
        LoadingIndicator = findViewById(R.id.LoadingIndicator);
        LoadingIndicator.setIndeterminate(true);

        // Setup the RecyclerView
        applications = findViewById(R.id.applications);
        applications.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        applicationsSearchIcon = findViewById(R.id.applicationsSearchIcon);
        applicationsTitle = findViewById(R.id.applicationsTitle);
        applicationsSearch = findViewById(R.id.applicationsSearch);
        onBackApplications = findViewById(R.id.onBackApplications);

        onBackApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        applicationsSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchOpen = true;
                applicationsSearchIcon.setVisibility(View.GONE);
                applicationsTitle.setVisibility(View.GONE);
                applicationsSearch.setVisibility(View.VISIBLE);
                showKeyboard(applicationsSearch,getApplicationContext());
            }
        });

        //Setup SearchBar
        applicationsSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().length() == 0)
                {
                    applicationAdapter.updateData(apps);
                    return;
                }

                if(s.toString().length() < 4)
                    return;

                ArrayList<PInfo> subList = new ArrayList<>();
                for(PInfo app : apps)
                {
                    if(app.appname.toLowerCase().contains(s.toString().toLowerCase()))
                        subList.add(app);
                }

                applicationAdapter.updateData(subList);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        //TODO: improve this.
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                apps = getInstalledApps(false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        applicationAdapter = new ApplicationAdapter(getApplicationContext(),ApplicationsActivity.this, apps);
                        applications.setAdapter(applicationAdapter);
                        LoadingIndicator.setIndeterminate(false);
                        LoadingIndicator.setVisibility(View.GONE);
                    }
                });
            }
        });
        thread.start();
    }

    //Thanks https://stackoverflow.com/a/8052835/10160890
    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            PInfo newInfo = new PInfo();
            newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
            res.add(newInfo);
        }
        return res;
    }

    @Override
    public void onBackPressed() {
        if(isSearchOpen)
        {
            isSearchOpen = false;
            applicationsSearchIcon.setVisibility(View.VISIBLE);
            applicationsTitle.setVisibility(View.VISIBLE);
            applicationsSearch.setVisibility(View.GONE);
            hideSoftKeyboard(applicationsSearch,getApplicationContext());
        }
        else
            super.onBackPressed();
    }

    //Thanks https://stackoverflow.com/a/34306465/10160890
    public static void showKeyboard(EditText mEtSearch, Context context) {
        mEtSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    //Thanks https://stackoverflow.com/a/34306465/10160890
    public static void hideSoftKeyboard(EditText mEtSearch, Context context) {
        mEtSearch.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
    }
}