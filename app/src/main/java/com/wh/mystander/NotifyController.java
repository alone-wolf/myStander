package com.wh.mystander;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;

@RestController
public class NotifyController {
    String TAG= "WH_"+NotifyController.class.getSimpleName();
    public static final String ACTION_FROM_NOTIFY_CONTROLLER = "ACTION_FROM_NOTIFY_CONTROLLER";
    public static final String INTENT_BUNDLE_EXTRA_NAME = "INTENT_BUNDLE_EXTRA_NAME";
    public static final String INTENT_BUNDLE_EXTRA_NAME_DEVICE = "INTENT_BUNDLE_EXTRA_NAME_DEVICE";
    public static final String INTENT_BUNDLE_EXTRA_NAME_TITLE = "INTENT_BUNDLE_EXTRA_NAME_TITLE";
    public static final String INTENT_BUNDLE_EXTRA_NAME_NOTIFY = "INTENT_BUNDLE_EXTRA_NAME_NOTIFY";


    @GetMapping("/notify")
    public String notify_new(
            Context context,
            @RequestParam(value = "device",defaultValue = "default device",required = false)String DEVICE,
            @RequestParam(value = "title",defaultValue = "default title",required = false)String TITLE,
            @RequestParam(value = "notify",defaultValue = "blank notify")String NOTIFY){
        Log.d(TAG, "notify_new: new notify");
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_BUNDLE_EXTRA_NAME_DEVICE,DEVICE);
        bundle.putString(INTENT_BUNDLE_EXTRA_NAME_TITLE,TITLE);
        bundle.putString(INTENT_BUNDLE_EXTRA_NAME_NOTIFY,NOTIFY);


        Intent intent = new Intent();
        intent.setAction(ACTION_FROM_NOTIFY_CONTROLLER);
        intent.putExtra(INTENT_BUNDLE_EXTRA_NAME,bundle);
        context.sendBroadcast(intent);

        return "notify recvd";
    }

    @GetMapping("/getAddress")
    public String getIp(){
        return "http:/"+NetUtils.getLocalIPAddress()+":8080";
    }

//    @GetMapping("/")
//    public String index(){
//        String url =  "http:/"+NetUtils.getLocalIPAddress()+":8080/notify?device=本机&title=myStander&notify=启动成功";
//        return "<!DOCTYPE html>\n" +
//                "<html lang=\"en\">\n" +
//                "<head>\n" +
//                "    <meta charset=\"UTF-8\">\n" +
//                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
//                "    <title>myStander</title>\n" +
//                "</head>\n" +
//                "<body>\n" +
//                "    <a href=\""+url+"\">"+url+"</a>\n" +
//                "</body>\n" +
//                "</html>";
//    }
}