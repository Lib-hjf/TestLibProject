package com.hjf.test.conn.pack;


public class EmptyPacket extends BasicPacket {

    public EmptyPacket(@PacketType int packetType) {
        super.packetType = (byte) packetType;
    }
}
