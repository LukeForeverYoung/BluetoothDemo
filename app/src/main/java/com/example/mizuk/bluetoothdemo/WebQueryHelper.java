package com.example.mizuk.bluetoothdemo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebQueryHelper {
    static String domain="139.224.131.102";
    static String queryUrl="http://"+domain+"/api/values/Query";
    static Position query(String id, Handler handler)
    {
        String strUrlPath =queryUrl;
        JSONObject params = new JSONObject();
        try {
            params.put("key",id);
            Thread postThread=new Thread(()->{
                String strResult=HttpUtils.submitPostData(strUrlPath,params, "utf-8");
                try {
                    JSONObject rec=new JSONObject(strResult);
                    Message msg=new Message();
                    msg.obj=rec;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
            postThread.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
class Position
{
    public String guid;
    public Double latitude;
    public Double longitude;
    public Double temperature;
    public Double humidity;//湿度
    public Position(String Key,Double latitude,Double longitude,Double temperature,Double humidity)
    {
        this.guid = Key;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
        this.humidity = humidity;
    }
    public Position() {; }
}
