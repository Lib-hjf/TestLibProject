package com.hjf.test.t_conn.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hjf.MyApp;
import com.hjf.test.t_conn.pack.HeartbeatPacket;
import com.hjf.test.t_conn.pack.RegisterPacket;

import org.hjf.kaconnect.OnConnectStatusChangedListener;
import org.hjf.kaconnect.nio.NioClient;
import org.hjf.log.LogUtil;
import org.hjf.util.MD5Util;

import java.nio.ByteBuffer;

/**
 * 长连接服务
 */
public class ConnServiceByNio extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LogUtil.d("开始建立长连接");
        NioClient.getInstance()
                // 设置推送数据分发器
                .setOnReceiveDataListener(PushDataDispatcher.getInstance())
                // 注册长连接建立成功监听
                .setOnConnectStatusChangedListener(new OnConnectStatusChangedListener() {
                    @Override
                    public void onConnected() {
                        startHeartThread();
                        sendRegisterPack();
                    }

                    @Override
                    public void onDisconnected() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                })
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
        NioClient.getInstance().disconnect();
    }


    /**
     * 发送注册/登录包
     */
    private static void sendRegisterPack() {
        LogUtil.d("发送登录数据包");
        RegisterPacket registerPacket = new RegisterPacket(MyApp.getContent(), NioClient.getInstance().getLocalIpAddress());
        registerPacket.setUserMobile("2256423");
        registerPacket.setPassword(MD5Util.getMd5Pwd("2256423"));
        NioClient.getInstance().pushData(ByteBuffer.wrap(registerPacket.toBytes()));
    }

    /**
     * 开启发送给心跳包线程
     */
    private static void startHeartThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    HeartbeatPacket heartbeatPacket = new HeartbeatPacket();
                    NioClient.getInstance().pushData(ByteBuffer.wrap(heartbeatPacket.toBytes()));
                    // 发送心跳报时间间隔，服务器超时：330秒，发送间隔：240秒
                    try {
                        Thread.sleep(240 * 1000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        })/*.start()*/;
    }

}
