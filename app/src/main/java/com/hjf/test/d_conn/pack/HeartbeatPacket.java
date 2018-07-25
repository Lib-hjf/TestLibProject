package com.hjf.test.d_conn.pack;

public class HeartbeatPacket extends BasicPacket {

    public HeartbeatPacket() {
        super.packetType = PacketType.HEART_BEAT_REQ;
    }
}
