package com.hjf.test.t_conn.pack;

public class PacketSeq {

    private static int seq = 1000;

    public static int get() {
        return ++PacketSeq.seq;
    }
}
