package com.hjf.test.conn.nio;

import android.support.annotation.NonNull;

import com.hjf.test.conn.OnReceiveDataListener;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ReceiveThread extends Thread{

    private Selector selector;
    private OnReceiveDataListener onReceiveDataListener;
    private ByteBuffer buff = ByteBuffer.allocate(2048);

    public ReceiveThread(@NonNull Selector selector, @NonNull OnReceiveDataListener onReceiveDataListener) {
        this.selector = selector;
        this.onReceiveDataListener = onReceiveDataListener;
    }

    @Override
    public void run() {
        super.run();
        // 遍历通道管理器中各就绪通道中的就绪事件
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();
            iterator.remove();

            // 2. 读取数据就绪，接收推送数据
            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                buff.clear();
                int count = 0;
                try {
                    count = sc.read(buff);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (count < 0) {
                    continue;
                }
                byte[] bytes = buff.array();
                // 接受数据并回传
                onReceiveDataListener.onReceiveData(bytes);
            }
            // 删除正在处理的Key
            keys.remove(key);
        }

    }
}
