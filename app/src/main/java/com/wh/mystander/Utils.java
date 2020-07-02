package com.wh.mystander;

import android.content.IntentFilter;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Utils {
    private static String TAG = "WH_"+Utils.class.getSimpleName();
    public static void runNotifyTest(String url) {
        sendHttpGetRequest(url);
    }

    public static void sendHttpGetRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            Log.d(TAG, "sendHttpGetRequest: "+response.body().string());
            response.body().close();
        } catch (IOException e) {
            Log.e(TAG, "sendHttpGetRequest: error occurred");
            Log.e(TAG, "sendHttpGetRequest: "+e.getMessage());
//            e.printStackTrace();
        }
    }

    public static String getCurrentTimeDate() {
        return getTimeDateFromMS(System.currentTimeMillis());
    }

    public static String getTimeDateFromMS(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }

    public static boolean itemInListInt(ArrayList<Integer> list, int item) {
        if(1==item){
            return true;
        }
        for (int i : list) {
            if (i == item) {
                return true;
            }
        }
        return false;
    }

    public static IntentFilter genBroadcastReceiverIntentFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Controller.ACTION_FROM_NOTIFY_CONTROLLER);
        intentFilter.addAction(MainServerService.ACTION_BROADCAST_STOP_SERVICE);
        intentFilter.addAction(MainServerService.ACTION_BROADCAST_REMOVE_NOTIFICATIONS);
        return intentFilter;
    }
}
