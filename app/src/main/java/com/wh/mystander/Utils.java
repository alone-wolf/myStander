package com.wh.mystander;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Utils {
    public static void runNotifyTest(String url) {
        sendHttpGetRequest(url);
    }

    public static void sendHttpGetRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
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
        intentFilter.addAction(NotifyController.ACTION_FROM_NOTIFY_CONTROLLER);
        intentFilter.addAction(MainServerService.ACTION_BROADCAST_STOP_SERVICE);
        intentFilter.addAction(MainServerService.ACTION_BROADCAST_REMOVE_NOTIFICATIONS);
        return intentFilter;
    }
}
