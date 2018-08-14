package com.hjf.test.t_conn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hjf.MyApp;
import com.hjf.test.R;
import com.hjf.test.t_conn.message.MessageType;
import com.hjf.test.t_conn.pack.HeartbeatPacket;
import com.hjf.test.t_conn.pack.RegisterPacket;
import com.hjf.test.t_conn.server.ConnServiceByNio;
import com.hjf.test.t_conn.server.PushDataDispatcher;
import com.hjf.util.NotifyUtil;

import org.hjf.activity.BaseActivity;
import org.hjf.annotation.apt.Router;
import org.hjf.kaconnect.nio.NioClient;
import org.hjf.log.LogUtil;
import org.hjf.util.MD5Util;
import org.json.JSONObject;

import java.nio.ByteBuffer;

@Router()
public class DemoConnActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_demo_conn_nio);

        // 登录包处理
        PushDataDispatcher.getInstance().registerListener(MessageType.MOBILE_REGISTER_RESP, new PushDataDispatcher.OnReceiveListener() {
            @Override
            public void onReceive(JSONObject jsonObject) {
                String s = jsonObject.toString();
                NotifyUtil.toast(s);
            }
        });
        // 心跳包处理
        PushDataDispatcher.getInstance().registerListener(MessageType.HEART_BEAT_RESP, new PushDataDispatcher.OnReceiveListener() {
            @Override
            public void onReceive(JSONObject jsonObject) {
                NotifyUtil.toast("收到心跳回应包");
            }
        });


        // 开启长连接任务
        startService(new Intent(this, ConnServiceByNio.class));
    }

    @Override
    public void onContentChanged() {
        findViewById(R.id.btn_send_login_packet).setOnClickListener(this);
        findViewById(R.id.btn_send_heart_packet).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_login_packet:
                sendRegisterPack();
                break;
            case R.id.btn_send_heart_packet:
                sendHeartThread();
                break;
        }
    }


    /**
     * 发送注册/登录包
     */
    private void sendRegisterPack() {
        LogUtil.d("发送登录数据包");
        RegisterPacket registerPacket = new RegisterPacket(MyApp.getContext(), NioClient.getInstance().getLocalIpAddress());
        registerPacket.setUserMobile("2256423");
        registerPacket.setPassword(MD5Util.getMd5Pwd("2256423"));
        NioClient.getInstance().pushData(ByteBuffer.wrap(registerPacket.toBytes()));
    }

    /**
     * 发送给心跳包
     */
    private void sendHeartThread() {
        HeartbeatPacket heartbeatPacket = new HeartbeatPacket();
        NioClient.getInstance().pushData(ByteBuffer.wrap(heartbeatPacket.toBytes()));
    }

}
