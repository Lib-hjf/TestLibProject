package com.hjf.test.d_conn.pack;

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
}
