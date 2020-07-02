package com.wh.mystander;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Random;


public class MainServerService extends Service {
    String TAG = "WH_" + MainServerService.class.getSimpleName();
    int port = AppConfig.port;
    String URL = "http:/" + NetUtils.getLocalIPAddress() + ":" + port;
    public static final String ACTION_BROADCAST_STOP_SERVICE = "ACTION_BROADCAST_STOP_SERVICE";
    public static final String ACTION_BROADCAST_REMOVE_NOTIFICATIONS = "ACTION_BROADCAST_REMOVE_NOTIFICATIONS";

    private static NotificationManager notificationManager;
    private static ArrayList<Integer> notificationIdList;
    private BroadcastReceiver broadcastReceiver;
    private MainServer mainServer;

    void startServer() {
        mainServer.startServer();
    }

    void stopServer() {
        mainServer.stopServer();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mainServer = new MainServer(this, port);

        notificationIdList = new ArrayList<>();
        MainServerService.notificationManager = getNotificationManager();

        // 创建通知
        NotificationCompat.Builder frontActivityNotificationBuilder = genForegroundNotification();
        // 启动前台通知
        startForeground(1, frontActivityNotificationBuilder.build());
        // 启动web服务器
        startServer();


        // 创建广播接收器
        broadcastReceiver = genBroadcastReceiver();
        registerReceiver(broadcastReceiver, Utils.genBroadcastReceiverIntentFilter());

        runTest();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    int genNewNotificationId() {
        int tmp = new Random().nextInt();
        if (Utils.itemInListInt(MainServerService.notificationIdList, tmp)) {
            return genNewNotificationId();
        } else {
            MainServerService.notificationIdList.add(tmp);
            return tmp;
        }
    }

    NotificationCompat.Builder genForegroundNotification() {
        // 创建通知中 退出 按钮
        Intent exitIntent = new Intent();
        exitIntent.setAction(MainServerService.ACTION_BROADCAST_STOP_SERVICE);
        PendingIntent exitPendingIntent = PendingIntent.getBroadcast(this, 0, exitIntent, 0);
        // 创建通知中 清除所有通知 按钮
        Intent removeNotificationIntent = new Intent();
        removeNotificationIntent.setAction(MainServerService.ACTION_BROADCAST_REMOVE_NOTIFICATIONS);
        PendingIntent removeNotificationPendingIntent = PendingIntent.getBroadcast(this, 0, removeNotificationIntent, 0);

        return new NotificationCompat.Builder(this, "serverService_Foreground")
                .setContentTitle("Running on " + URL)
                .setSmallIcon(R.drawable.ic_baseline_adb_24)
                .addAction(R.drawable.ic_baseline_adb_24, "停止服务", exitPendingIntent)
                .addAction(R.drawable.ic_baseline_adb_24, "清除全部通知", removeNotificationPendingIntent);
    }

    NotificationCompat.Builder genNotifyNotification(Bundle notify) {
        String DEVICE = notify.getString(Controller.INTENT_BUNDLE_EXTRA_NAME_DEVICE, "default device");
        String TITLE = notify.getString(Controller.INTENT_BUNDLE_EXTRA_NAME_TITLE, "default title");
        String NOTIFY = notify.getString(Controller.INTENT_BUNDLE_EXTRA_NAME_NOTIFY, "blank notify");

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(TITLE);
        inboxStyle.setSummaryText(DEVICE);
        inboxStyle.addLine(NOTIFY);
        inboxStyle.addLine(Utils.getCurrentTimeDate());


        // 创建接受Notify的通知
        return new NotificationCompat.Builder(this, "notify_normal")
                .setSmallIcon(R.drawable.ic_baseline_speaker_notes_24)
                .setContentTitle("收到新的Notify: " + TITLE)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(inboxStyle);
    }

    NotificationManager getNotificationManager() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建通知渠道
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel_server_service_foreground = new NotificationChannel(
                    "serverService_Foreground",
                    "服务器前台活动",
                    NotificationManager.IMPORTANCE_LOW);

            NotificationChannel channel_notify_normal = new NotificationChannel(
                    "notify_normal",
                    "收到Notify",
                    NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel_server_service_foreground);
                notificationManager.createNotificationChannel(channel_notify_normal);
            }
        }
        return notificationManager;
    }

    BroadcastReceiver genBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String ACTION = intent.getAction() + "";
                switch (ACTION) {
                    case Controller.ACTION_FROM_NOTIFY_CONTROLLER: {
                        Bundle notify = intent.getBundleExtra(Controller.INTENT_BUNDLE_EXTRA_NAME);
                        if (notify == null) {
                            return;
                        }
                        MainServerService.notificationManager.notify(genNewNotificationId(), genNotifyNotification(notify).build());
                        break;
                    }
                    case ACTION_BROADCAST_STOP_SERVICE: {
                        stopServer();
                        break;
                    }
                    case ACTION_BROADCAST_REMOVE_NOTIFICATIONS: {
                        for (int i : MainServerService.notificationIdList) {
                            MainServerService.notificationManager.cancel(i);
                        }
                        MainServerService.notificationIdList = new ArrayList<>();
                        break;
                    }
                    default: {
                        Log.d(TAG, "onReceive: recv wrong action> " + ACTION);
                    }
                }
            }
        };
    }

    void runTest() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    String url = URL + "/" + AppConfig.notifyRoute + "?device=本机&title=myStander&notify=恭喜你,myStander启动成功!";
                    Log.d(TAG, "run: " + url);
                    Utils.runNotifyTest(url);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
