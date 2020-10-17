package com.custom.ledcover.utils;

import android.graphics.drawable.Drawable;

//Thanks https://stackoverflow.com/a/8052835/10160890
public class PInfo {
    public String appname = "";
    public String pname = "";
    public String versionName = "";
    public int versionCode = 0;
    public Drawable icon;
    public void prettyPrint() {
        System.out.println(appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
    }


    public int notification = -1;
}
