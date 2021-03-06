package com.wh.mystander;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, MainServerService.class));

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("使用简介")
                .setMessage("http:/" + NetUtils.getLocalIPAddress() + ":" + AppConfig.port + "/" + AppConfig.notifyRoute + "\n" +
                        "使用http的get请求方式发送Notify\n" +
                        "涉及的请求参数:\n" +
                        "\tdevice=你的设备名(非必须)\n" +
                        "\ttitle=Notify的标题(非必须)\n" +
                        "\tnotify=Notify内容")
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        MainActivity.this.finish();
                    }
                })
                .create();
        alertDialog.show();
    }
}