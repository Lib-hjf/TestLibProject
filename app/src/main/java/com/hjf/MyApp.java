package com.hjf;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import org.hjf.util.EnvironUtils;
import org.hjf.log.LogMgr;

import java.util.Stack;


public class MyApp extends Application {

    private static MyApp myApp;

    // Activity 监控管理
    private Stack<Activity> activityStack = new Stack<>();
    private ActivityLifecycleListener activityLifecycleListener = new ActivityLifecycleListener();

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        // 打印日志工具设置
        String storageDir = Environment.getExternalStorageDirectory().toString();
        String logPath = storageDir + "/" + EnvironUtils.getApplicationID(getApplicationContext());
        LogMgr.init(getApplicationContext(), logPath);
        LogMgr.getInstance().openHJFDebug();
        LogMgr.getInstance().openCrashLog();
        LogMgr.getInstance().openDiskLog(Log.DEBUG);
        LogMgr.getInstance().openLogcat(Log.DEBUG);
        // 监听 Activity 创建和销毁，进行管理
        registerActivityLifecycleCallbacks(activityLifecycleListener);
    }


    /**
     * 获取单例对象
     */
    public static MyApp getInstance() {
        return MyApp.myApp;
    }

    /**
     * 获取全局上下文对象
     */
    public static Context getContent() {
        return MyApp.myApp.getApplicationContext();
    }

    /**
     * 获取当前显示的Activity
     */
    public static Activity getTopActivity() {
        return MyApp.myApp.activityStack.lastElement();
    }


    /**
     * Activity 声明周期管理
     */
    private static class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            MyApp.getInstance().activityStack.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            MyApp.getInstance().activityStack.remove(activity);
        }
    }

}
