package com.hjf.test.t_conn.message;


import com.hjf.test.t_conn.pack.PacketType;

public class MessageUtil {

    /**
     * 根据 {@link com.hjf.test.conn.pack.PacketType}
     * 获取 {@link MessageType}
     * 只负责转换 cloud -> mobile 的 response 数据包就可以了
     */
    @MessageType
    public static String getMessageType4PacketType(@PacketType int type) {
        if (type == PacketType.HEART_BEAT_RESP) {
            return MessageType.HEART_BEAT_RESP;
        } else if (type == PacketType.MOBILE_REGISTER_RESP) {
            return MessageType.MOBILE_REGISTER_RESP;
        } else if (type == PacketType.HUB_DEVICE_REGISTER_RESP) {
            return MessageType.HUB_DEVICE_REGISTER_RESP;
        } else if (type == PacketType.HUB_DEVICE_UNREGISTER_RESP) {
            return MessageType.HUB_DEVICE_UNREGISTER_RESP;
        } else if (type == PacketType.PUSH_CLOUD2MOBILE_RESP) {
            return MessageType.PUSH_CLOUD2MOBILE_RESP;
        } else if (type == PacketType.PUSH_MOBILE2DEVICE_RESP) {
            return MessageType.PUSH_MOBILE2DEVICE_RESP;
        }
        return MessageType.UNKNOWN_TYPE;
    }
}
