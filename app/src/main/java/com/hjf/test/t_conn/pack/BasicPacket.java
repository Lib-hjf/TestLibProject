package com.hjf.test.t_conn.pack;


import android.text.TextUtils;

import java.io.UnsupportedEncodingException;

/**
 * 基本数据包，用于App本地和Cloud服务器端数据传递和解析
 * TODO 直接将byte当int使用可以吗
 */
public class BasicPacket {

    private static final String CHARSET = "utf-8";

    private byte packetHead = (byte) 0xfa; // 包头固定
    protected byte packetType = (byte) 0x00; // 类型，默认0x00，表示不明包体数据
    private int packetSeq;  // 序列号
    private byte packetRet = (byte) 0x00;
    private byte packetVersion = (byte) 0x01; // 版本号

    protected String contentStr = "";

    BasicPacket() {
    }

    /**
     * 在构建Byte数组前的动作
     */
    protected void preBuildBytes() {
        contentStr = "";
    }

    /**
     * 构架byte数组
     */
    public byte[] toBytes() {
        preBuildBytes();
        byte[] headBytes = new byte[1024];
        // 消息头
        headBytes[0] = packetHead;
        headBytes[1] = (byte) 0x01;
        // 消息类型
        headBytes[2] = packetType;
        // 序列 sequence
        packetSeq = PacketSeq.get();
        byte[] seq_byte = PacketUtil.int2byte4net(packetSeq);
        System.arraycopy(seq_byte, 0, headBytes, 3, 4);

        headBytes[9] = packetRet;

        // 没有内容
        if (TextUtils.isEmpty(contentStr)) {
            byte[] packetDataBytes = new byte[10];
            System.arraycopy(headBytes, 0, packetDataBytes, 0, 10);
            return packetDataBytes;
        }
        // 内容转换
        try {
            byte[] contentByte = contentStr.getBytes(CHARSET);
            // 长度字段
            int len = contentByte.length;

            byte[] lenByte = PacketUtil.short2byte4net((short) len);
            System.arraycopy(lenByte, 0, headBytes, 7, 2);

            // 生成结果 byte[]
            byte[] packetDataBytes = new byte[len + 10];
            System.arraycopy(headBytes, 0, packetDataBytes, 0, 10);
            System.arraycopy(contentByte, 0, packetDataBytes, 10, len);
            return packetDataBytes;
        }
        // 内容转换发生异常
        catch (Exception e) {
            e.printStackTrace();
            byte[] packetDataBytes = new byte[10];
            System.arraycopy(packetDataBytes, 0, headBytes, 0, 10);
            return packetDataBytes;
        }
    }


    /**
     * 解析byte数组
     */
    public BasicPacket parseByte(byte[] bytes) {
        // 检查包长　
        if (bytes == null || bytes.length < 10) {
            return this;
        }
        // 检查包头第一个字节及包头版本号　
        if (bytes[0] != packetHead && bytes[1] != packetVersion) {
            return this;
        }

        try {
            // 检查 head
            packetType = bytes[2];

            byte[] seqByte = new byte[4];
            System.arraycopy(bytes, 3, seqByte, 0, 4);
            packetSeq = PacketUtil.byte2int4net(seqByte);

            byte[] lenByte = new byte[4];
            System.arraycopy(bytes, 7, lenByte, 0, 2);
            int contentLength = PacketUtil.byte2short4net(lenByte);

            // 返回码
            packetRet = bytes[9];

            // 获取 json 字符串的内容数据
            if (contentLength > 0) {
                byte[] contentBytes = new byte[contentLength];
                System.arraycopy(bytes, 10, contentBytes, 0, contentLength);
                contentStr = new String(contentBytes, CHARSET);
            }
        } catch (UnsupportedEncodingException e) {
            contentStr = e.getMessage();
        }
        return this;
    }

    public byte getPacketType() {
        return this.packetType;
    }

    public String getContentStr() {
        return contentStr;
    }

    /**
     * 根据数据创建 pack 对象
     */
    public static BasicPacket create() {
        return new BasicPacket();
    }

}
