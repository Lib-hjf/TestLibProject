package com.hjf.test.conn.pack;

public class PacketUtil {

    /**
     * int 转 byte[]
     * 高位在前，低位在后
     */
    public static byte[] int2byte4net(int x) {
        byte[] bb = new byte[4];
        bb[0] = (byte) ((x >> 24) & 0xFF);
        bb[1] = (byte) ((x >> 16) & 0xFF);
        bb[2] = (byte) ((x >> 8) & 0xFF);
        bb[3] = (byte) ((x) & 0xFF);
        return bb;
    }

    /**
     * short 转 byte[]
     * 高位在前，低位在后
     */
    public static byte[] short2byte4net(short x) {
        byte[] bb = new byte[2];
        bb[0] = (byte) ((x >> 8) & 0xFF);
        bb[1] = (byte) (x & 0xFF);
        return bb;
    }

    /**
     * byte数组中取int数值
     * 高位在前，低位在后
     */
    public static int byte2int4net(byte[] src) {
        int value;
        value = (((src[0] & 0xFF) << 24)
                | ((src[1] & 0xFF) << 16)
                | ((src[2] & 0xFF) << 8)
                | (src[3] & 0xFF));
        return value;
    }

    /**
     * byte数组中取short数值
     * 高位在前，低位在后
     */
    public static short byte2short4net(byte[] bb) {
        int i = (((bb[0] & 0xFF) << 8)
                | ((bb[1] & 0xFF)));
        return (short) i;
    }

    // 返回命令包类型
    public static String getMsgType(@PacketType int type) {
        String str = "unknown";

        // 请求：手机APP  --> Server （心跳）
        if (type == PacketType.HEART_BEAT_REQ) {
            str = "heartbeat_req";
        }
        // 回应：手机APP  <-- Server （心跳）
        else if (type == PacketType.HEART_BEAT_RESP) {
            str = "heartbeat_resp";
        }
        // 请求：手机APP请求注册
        else if (type == PacketType.MOBILE_REGISTER_REQ) {
            str = "mobile_registe_req";
        }
        // 回应：手机APP请求注册
        else if (type == PacketType.MOBILE_REGISTER_RESP) {
            str = "mobile_registe_resp";
        }
        // 请求：主机设备请求注册
        else if (type == PacketType.HUB_DEVICE_REGISTER_REQ) {
            str = "box_registe_req";
        }
        // 回应：主机设备请求注册
        else if (type == PacketType.HUB_DEVICE_REGISTER_RESP) {
            str = "box_registe_resp";
        }
        // 请求：主机设备请求注销
        else if (type == PacketType.HUB_DEVICE_UNREGISTER_REQ) {
            str = "unregiste_req";
        }
        // 回应：主机设备请求注销
        else if (type == PacketType.HUB_DEVICE_UNREGISTER_RESP) {
            str = "unregiste_resp";
        }
        // 请求： server --> 手机
        else if (type == PacketType.PUSH_CLOUD2MOBILE_REQ) {
            str = "push_mobile_req";
        }
        // 回应： server <-- 手机
        else if (type == PacketType.PUSH_CLOUD2MOBILE_RESP) {
            str = "push_mobile_resp";
        }
        // 请求： server --> 智能设备
//        else if (type == (byte) 0x09) {
//            str = "push_dev_req";
//        }
        // 回应： server <-- 智能设备
//        else if (type == (byte) 0x89) {
//            str = "push_dev_resp";
//        }
        // 请求： 手机 --> 盒子客户端
        else if (type == PacketType.PUSH_MOBILE2DEVICE_REQ) {
            str = "mobile_cmd_req";
        }
        // 回应： 手机 <-- 盒子客户端
        else if (type == PacketType.PUSH_MOBILE2DEVICE_RESP) {
            str = "mobile_cmd_resp";
        }
        // TODO 长连接建立成功
        // 回应： 手机 <-- 盒子客户端
        else if (type == PacketType.CONNECT_SUCCESS) {
            str = "connect_success";
        }
        return str;
    }
}
