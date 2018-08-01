package com.hjf;

import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import org.hjf.util.EnvironUtils;
import org.hjf.util.log.LogMgr;


public class MyApp extends MultiDexApplication {

    private static MyApp myApp;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        // 答应日志工具设置
        String storageDir = Environment.getExternalStorageDirectory().toString();
        String logPath = storageDir + "/" + EnvironUtils.getApplicationID(getApplicationContext());
        LogMgr.init(getApplicationContext(), logPath);
        LogMgr.getInstance().openHJFDebug();
        LogMgr.getInstance().openCrashLog();
        LogMgr.getInstance().openDiskLog(Log.DEBUG);
        LogMgr.getInstance().openLogcat(Log.DEBUG);
    }


    /**
     * 获取单利对象
     */
    public static Context getContent() {
        return MyApp.myApp.getApplicationContext();
    }

}
