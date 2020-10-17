package com.custom.ledcover.experimental;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.custom.ledcover.activities.ApplicationsActivity;
import com.custom.ledcover.utils.ApplicationAdapter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DoInBackground extends AsyncTask<Void, Integer, String> {

    private static String URL_FIRMWARE = "https://www.samsungimaging.com/file/download?XmlIdx=466&file=OFFICIAL_EF_KG97X_006_00E_181211_FW.zip";
    private static String FILE_NAME = "OFFICIAL_EF_KG97X_006_00E_181211_FW.zip";

    private ProgressBar progressBar;
    private Activity activity;

    public DoInBackground(ProgressBar progressBar, Activity activity) {
        this.progressBar = progressBar;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(Void... params) {
        int count;
        String path = null;

        //Download

        try {
            URL url = new URL(URL_FIRMWARE);
            URLConnection conection = url.openConnection();
            conection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            final int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            path = activity.getFilesDir() + "/" + FILE_NAME;
            Log.i("doInBackground", "path: " + path);
            // Output stream
            OutputStream output = new FileOutputStream(path);

            byte[] data = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress((int) ((total * 100) / lenghtOfFile) / 2);

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("doInBackground Error: ", e.getMessage());
            path = null;
        }


        if (path == null)
            return null;

        // Unzip
        String extractPath = activity.getDir("test",Context.MODE_PRIVATE).getAbsolutePath();
        try {
            ArrayList<String> files = extractTo(path,extractPath);
            for(String str : files)
                Log.i("[doInBackground]",str);

            publishProgress(100);

        } catch (IOException e) {
            e.printStackTrace();
        }





        return path;
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(Integer... progress) {
        if(progress == null)
        {
            progressBar.getProgressDrawable().setColorFilter(
                    Color.argb(100,255,0,0), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        // setting progress percentage
        progressBar.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(activity.getApplicationContext(), "Saved " + result, Toast.LENGTH_LONG).show();
        System.out.println("Execution finished!");
    }


    public static ArrayList<String> extractTo(String archive, String destPath) throws FileNotFoundException, IOException {
        ArrayList<String> fileList = new ArrayList<String>();

        ZipInputStream zipInputStream = null;
        ZipEntry zipEntry = null;
        byte[] buffer = new byte[2048];

        zipInputStream = new ZipInputStream(new FileInputStream(archive));
        zipEntry = zipInputStream.getNextEntry();
        while (zipEntry != null) {

            String path = destPath + "/" + zipEntry.getName();
            FileOutputStream fileoutputstream = new FileOutputStream(path);
            int n;

            while ((n = zipInputStream.read(buffer, 0, 2048)) > -1) {
                fileoutputstream.write(buffer, 0, n);
            }

            fileoutputstream.close();
            zipInputStream.closeEntry();

            //String out = "[FILE] " + path;
            fileList.add(path);

            zipEntry = zipInputStream.getNextEntry();
        }

        return fileList;
    }
}


