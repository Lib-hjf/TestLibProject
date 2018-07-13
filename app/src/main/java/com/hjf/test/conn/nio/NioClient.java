package com.hjf.test.conn.nio;

import android.text.TextUtils;

import com.hjf.test.Util.NotifyUtil;
import com.hjf.test.conn.OnReceiveDataListener;
import com.hjf.test.conn.pack.BasicPacket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 * 长连接客户端
 */
public class NioClient {


    private static NioClient client;

    /**
     * 客户端状态
     */
    private boolean isConnect = false;

    /**
     * 发起连接操作的对象，内部带有失败重试策略
     */
    private ConnectThread connectThread;

    /**
     * 主机地址和端口
     */
    private String host;
    private int port;

    /**
     * Nio 管理长连接必要对象
     */

    /**
     * 回调对象
     */
    private OnReceiveDataListener onReceiveDataListener;
    private OnTcpConnectStatueChangedListener onTcpConnectStatueChangedListener = new OnTcpConnectStatueChangedListener() {


        @Override
        public void onConnected(Selector selector, SocketChannel socketChannel) throws IOException {
            isConnect = true;
            NotifyUtil.toast("长连接成功");
            NioClient.this.stopConnectThread();

            // 接收数据线程
            new ReceiveThread(selector, NioClient.this.onReceiveDataListener).start();

            // 发送数据线程
            new SendThread(socketChannel).start();
        }

        @Override
        public void onDisconnected() {
            isConnect = false;
        }

        @Override
        public void onError() {
            startConnectThread();
        }
    };

    /**
     * 构造肥那个发
     */
    private NioClient() {

    }

    /**
     * 设置接受到消息后的通知对象
     */
    public NioClient setOnReceiveDataListener(OnReceiveDataListener onReceiveDataListener) {
        this.onReceiveDataListener = onReceiveDataListener;
        return this;
    }

    /**
     * 连接
     */
    public synchronized void connect(String host, int port) {
        this.host = host;
        this.port = port;
        this.startConnectThread();
    }

    /**
     * 开启长连接线程任务
     */
    private void startConnectThread() {
        // 丢失 主机地址和端口
        if (TextUtils.isEmpty(this.host) || this.port == 0) {
            return;
        }
        // 准备线程对象
        if (connectThread == null) {
            connectThread = new ConnectThread(onReceiveDataListener, onTcpConnectStatueChangedListener);
        }
        connectThread.setConnectInfo(this.host, this.port);
        // 长连接线程已经运行
        if (connectThread.isRunning) {
            connectThread.resetReconnectTime();
        }
        // 开始运行
        else {
            connectThread.start();
        }
    }

    /**
     * 断开连接
     */
    public synchronized void disconnect() {
        // 销毁连接线程
        this.stopConnectThread();
        // 关闭 channel 通道 TODO
        // 销毁Netty长连接线程池 TODO
        // 标记未连接状态
        isConnect = false;
    }

    /**
     * 销毁连接工具线程
     */
    private void stopConnectThread() {
        if (connectThread != null) {
            connectThread.destroy();
            connectThread = null;
        }
    }

    /**
     * 是否连接 TODO
     */
    public boolean isConnect() {
        return true;
    }

    /**
     * 发送信息 TODO
     */
    public void sendDataPack(BasicPacket packet) {
        if (this.isConnect()) {
            connectThread.sendData(packet);
        }
    }

    /**
     * 获取单例对象
     */
    public static NioClient getInstance() {
        if (client == null) {
            synchronized (NioClient.class) {
                if (client == null) {
                    client = new NioClient();
                }
            }
        }
        return NioClient.client;
    }


}
