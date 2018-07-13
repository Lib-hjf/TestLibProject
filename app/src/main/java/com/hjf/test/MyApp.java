package com.hjf.test;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.hjf.test.Util.NotifyUtil;
import com.hjf.test.conn.ConnServiceByNetty;
import com.hjf.test.conn.ConnServiceByNio;
import com.hjf.test.conn.PushDataDispatcher;
import com.hjf.test.conn.netty.NettyClient;
import com.hjf.test.conn.nio.NioClient;
import com.hjf.test.conn.pack.PacketType;
import com.hjf.test.conn.pack.RegisterPacket;

import org.hjf.liblogx.LogMgr;
import org.hjf.util.EnvironUtils;
import org.hjf.util.MD5Util;
import org.json.JSONObject;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;


public class MyApp extends Application {

    private static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;

        LogMgr.init(getApplicationContext(), EnvironUtils.getApplicationID(getApplicationContext()));
//        LogMgr.getContent().openCrashLog();
//        LogMgr.getContent().openDiskLog(Log.DEBUG);


        // 注册长连接建立成功监听
        PushDataDispatcher.getInstance().registerListener(PacketType.CONNECT_SUCCESS, new PushDataDispatcher.OnReceiveListener() {
            @Override
            public void onReceive(JSONObject jsonObject) {
                sendRegisterPack();
            }
        });
        // 开启长连接任务
        startService(new Intent(this, ConnServiceByNio.class));
    }


    /**
     * 获取单利对象
     */
    public static Context getContent() {
        return MyApp.myApp.getApplicationContext();
    }

    /**
     * 发送注册/登录包
     */
    private static void sendRegisterPack() {
        NotifyUtil.toast("发送登录数据包");
        RegisterPacket registerPacket = new RegisterPacket(MyApp.getContent());
        registerPacket.setUserMobile("2256423");
        registerPacket.setPassword(MD5Util.getMd5Pwd("2256423"));
        NioClient.getInstance().sendDataPack(registerPacket);
    }
}
