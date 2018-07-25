package com.hjf.test.d_conn.pack;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 云端和终端交互的数据包类型
 */
@IntDef({
        PacketType.HEART_BEAT_REQ,
        PacketType.HEART_BEAT_RESP,
        PacketType.MOBILE_REGISTER_REQ,
        PacketType.MOBILE_REGISTER_RESP,
})
@Retention(RetentionPolicy.SOURCE)
public @interface PacketType {
    byte UNKNOWN_TYPE = (byte) 0xaa;
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
