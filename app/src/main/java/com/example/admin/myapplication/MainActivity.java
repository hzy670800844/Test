package com.example.admin.myapplication;

import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.huawei.android.pushagent.api.PushManager;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private String MIPUSH_APPID = "2882303761517786571";
    private String MIPUSH_APPKEY = "5891778664571";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("version",TIMManager.getInstance().getVersion());
    }
    private void iniyTecentIM() {
        Log.e("version", TIMManager.getInstance().getVersion());
        if (MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify) {
                        //消息被设置为需要提醒
                        notification.doNotify(getApplicationContext(), R.mipmap.ic_launcher);
                        //注册推送服务
                        registerPush();
                    }
                }
            });

        }
        //初始化 SDK 基本配置
        TIMSdkConfig config = new TIMSdkConfig(1400165588)
                .enableLogPrint(true)
                .setLogLevel(TIMLogLevel.DEBUG)
                .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/justfortest/")
                .enableCrashReport(false);

        //初始化 SDK
        TIMManager.getInstance().init(getApplicationContext(), config);
    }

    private void registerPush() {
        String vendor= Build.MANUFACTURER;
        if (vendor.toLowerCase(Locale.ENGLISH).contains("xiaomi")){
            //注册小米推送服务
            MiPushClient.registerPush(this,MIPUSH_APPID,MIPUSH_APPKEY);
        }else if (vendor.toLowerCase(Locale.ENGLISH).contains("huawei")){
            //请求华为的设备token
            PushManager.requestToken(this);
        }
    }
}
