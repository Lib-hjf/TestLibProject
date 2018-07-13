package com.hjf.test.conn.nio;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Vector;

public class SendThread extends Thread{

    private SocketChannel socketChannel;
    private Vector<ByteBuffer> sendQueue = new Vector<>();

    public SendThread(@NonNull SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        super.run();

        Iterator<ByteBuffer> sendQueueIterator = sendQueue.iterator();
        while (sendQueueIterator.hasNext()) {
            ByteBuffer buffer = sendQueueIterator.next();
            sendQueueIterator.remove();
            while (buffer.hasRemaining()) {
                try {
                    socketChannel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
