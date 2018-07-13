package com.hjf.test.conn;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hjf.test.conn.netty.NettyClient;

/**
 * 长连接服务 使用Netty
 */
public class ConnServiceByNetty extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NettyClient.getInstance()
                // 设置推送数据分发器
                .setOnReceiveDataListener(PushDataDispatcher.getInstance())
                .connect("119.29.82.81", 9700);

        // 如果系统在onStartCommand()返回后杀死了服务，不要重新创建这个service
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        NettyClient.getInstance().disconnect();
    }

}
