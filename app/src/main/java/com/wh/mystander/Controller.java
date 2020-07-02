package com.wh.mystander;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;

@RestController
public class Controller {
    String TAG= "WH_"+ Controller.class.getSimpleName();
    public static final String ACTION_FROM_NOTIFY_CONTROLLER = "ACTION_FROM_NOTIFY_CONTROLLER";
    public static final String INTENT_BUNDLE_EXTRA_NAME = "INTENT_BUNDLE_EXTRA_NAME";
    public static final String INTENT_BUNDLE_EXTRA_NAME_DEVICE = "INTENT_BUNDLE_EXTRA_NAME_DEVICE";
    public static final String INTENT_BUNDLE_EXTRA_NAME_TITLE = "INTENT_BUNDLE_EXTRA_NAME_TITLE";
    public static final String INTENT_BUNDLE_EXTRA_NAME_NOTIFY = "INTENT_BUNDLE_EXTRA_NAME_NOTIFY";


    @GetMapping("/notify")
    public String notify_new(
            final Context context,
            @RequestParam(value = "device",defaultValue = "default device",required = false) final String DEVICE,
            @RequestParam(value = "title",defaultValue = "default title",required = false) final String TITLE,
            @RequestParam(value = "notify",defaultValue = "blank notify") final String NOTIFY){
        Log.d(TAG, "notify_new: new notify");


        new Thread(){
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putString(INTENT_BUNDLE_EXTRA_NAME_DEVICE,DEVICE);
                bundle.putString(INTENT_BUNDLE_EXTRA_NAME_TITLE,TITLE);
                bundle.putString(INTENT_BUNDLE_EXTRA_NAME_NOTIFY,NOTIFY);


                Intent intent = new Intent();
                intent.setAction(ACTION_FROM_NOTIFY_CONTROLLER);
                intent.putExtra(INTENT_BUNDLE_EXTRA_NAME,bundle);
                context.sendBroadcast(intent);
            }
        }.start();

        return "notify recvd";
    }

    @GetMapping("/getAddress")
    public String getIp(){
        return "http:/"+NetUtils.getLocalIPAddress()+":8080";
    }
}