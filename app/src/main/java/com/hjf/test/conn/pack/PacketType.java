package com.hjf.test.conn.pack;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        PacketType.UNKNOWN_PACKET_TYPE,
        PacketType.CONNECT_SUCCESS,
        PacketType.HEART_BEAT_REQ,
        PacketType.HEART_BEAT_RESP,
})
@Retention(RetentionPolicy.SOURCE)
public @interface PacketType {
    // 自定义的类型标识，注意被别冲突
    // 未知
    byte UNKNOWN_PACKET_TYPE = (byte) 0xaa;
    // 长连接成功
    byte CONNECT_SUCCESS = (byte) 0xa1;


    // #################################################
    // ###                  注意                      ###
    // ###             必须符合文档规范                 ###
    // ###     在mobile、cloud、device端通用类型标识     ###
    // #################################################
    // 心跳
    byte HEART_BEAT_REQ = 0x01;
    byte HEART_BEAT_RESP = (byte) 0x81;
    // mobile注册
    byte MOBILE_REGISTER_REQ = 0x02;
    byte MOBILE_REGISTER_RESP = (byte) 0x82;
    // 主机设备注册
    byte HUB_DEVICE_REGISTER_REQ = 0x06;
    byte HUB_DEVICE_REGISTER_RESP = (byte) 0x86;
    // 主机设备注销
    byte HUB_DEVICE_UNREGISTER_REQ = 0x03;
    byte HUB_DEVICE_UNREGISTER_RESP = (byte) 0x83;
    // 推送消息：云 --> mobile
    byte PUSH_CLOUD2MOBILE_REQ = 0x09;
    byte PUSH_CLOUD2MOBILE_RESP = (byte) 0x89;
    // 推送消息：mobile --> HubDevice
    byte PUSH_MOBILE2DEVICE_REQ = 0x0a;
    byte PUSH_MOBILE2DEVICE_RESP = (byte) 0x8a;


}
