package com.hjf.test.conn.nio;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 常连接重新连接线程
 */
final class ConnectThread extends Thread {

    /**
     * 主机地址和端口
     */
    private String host;
    private int port;
    /**
     * 线程运行状态
     */
    boolean isRunning = false;
    private boolean needDestroy = false;
    private int tryNum = 1;

    /**
     * 线程阻塞工具
     */
    private OnTcpConnectStatueChangedListener onTcpConnectStatueChangedListener;

    public ConnectThread(@NonNull OnTcpConnectStatueChangedListener onTcpConnectStatueChangedListener) {
        this.onTcpConnectStatueChangedListener = onTcpConnectStatueChangedListener;
    }

    @Override
    @Deprecated
    public void run() {
        super.run();

        isRunning = true;

        try {
            Selector selector = Selector.open(); // 通道管理器
            SocketChannel socketChannel = SocketChannel.open();   // Socket通道
            socketChannel.configureBlocking(false); // 设置非阻塞
            // 将通道channel注册到管理器selector上，并告诉selector关注该chanel的连接事件
            // Channel、Selector一起使用时，Channel必须是非阻塞模式
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            // 客户端发起连接
            // 该方法执行后并没有实现连接，需要调用channel.finishConnect()才能完成连接
            socketChannel.connect(new InetSocketAddress(host, port));

            // 阻塞线程，直到至少有一个Channel在注册事件上准备就绪
            selector.select();

            // 遍历通道管理器中各就绪通道中的就绪事件
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                // 建立连接就绪，完成连接建立
                if (key.isConnectable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    // 如果Socket通道正在连接，则完成连接操作
                    if (sc.isConnectionPending()) {
                        sc.finishConnect();
                        // 连接建立成功
                        onTcpConnectStatueChangedListener.onConnected(selector, socketChannel);
                    }

                    socketChannel.register(selector, SelectionKey.OP_READ);
                }

                // 删除正在处理的Key
                keys.remove(key);
            }
        } catch (IOException e) {
            onTcpConnectStatueChangedListener.onError();
        }
    }

    /**
     * 开启任务
     */
    @Override
    public synchronized void start() {
        if (isRunning) {
            return;
        }
        super.start();
    }

    @Override
    public void destroy() {
        needDestroy = true;
    }

    /**
     * 重置重新连接尝试时间间隔
     */
    void resetReconnectTime() {
        tryNum = 1;
    }

    /**
     * 获取线程睡眠时间
     *
     * @param tryNum 尝试次数
     * @return 线程睡眠时间
     */
    private int getThreadSleepTimeNum(int tryNum) {
        if (tryNum < 5) {
            return 10 * 1000;
        } else if (tryNum < 10) {
            return 20 * 1000;
        } else if (tryNum < 20) {
            return 30 * 1000;
        }
        return 60 * 1000;
    }

    /**
     * 设置连接信息
     *
     * @param host 服务器主机地址
     * @param port 端口
     */
    public void setConnectInfo(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
