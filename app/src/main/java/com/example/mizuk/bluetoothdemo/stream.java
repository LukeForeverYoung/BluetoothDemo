package com.example.mizuk.bluetoothdemo;

import android.os.StrictMode;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

public class stream {

    static void urr() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        String temp = "http://123.207.85.113:5000/query/" + MainActivity.ed.getText();
        String t;
        try {
            URL u = new URL(temp);

            InputStream in = u.openStream();

            byte[] b = new byte[1000];
            while (in.read(b) != -1) ;
            t = new String(b);

            JSONObject jsonObj = new JSONObject(t);
            String lat = jsonObj.optString("lat");
            String user_key = jsonObj.optString("user_key");
            String lon = jsonObj.optString("lon");
            String tem = jsonObj.optString("tem");
            String wet = jsonObj.optString("wet");


            MainActivity.distanceTextView.setText("lat: " + lat);
            MainActivity.tvb.setText("user_key: " + user_key);
            MainActivity.tvc.setText("lon: " + lon);
            MainActivity.tvd.setText("tem: " + tem);
            MainActivity.tve.setText("wet: " + wet);

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class BlueTooth {
    }
}
