package com.hjf.test.conn.netty;

import android.text.TextUtils;

import com.hjf.test.Util.NotifyUtil;
import com.hjf.test.conn.OnReceiveDataListener;
import com.hjf.test.conn.pack.BasicPacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 长连接客户端
 */
public final class NettyClient {

    private static NettyClient client;

    private boolean isConnect = false;

    /**
     * 主机地址和端口
     */
    private String host;
    private int port;
    /**
     * 发起连接操作的对象，内部带有失败重试策略
     */
    private ConnectThread connectThread;

    /**
     * Netty 管理长连接必要对象
     */
    private EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;

    /**
     * 回调对象
     */
    private OnReceiveDataListener onReceiveDataListener;
    private OnTcpConnectStatueChangedListener onTcpConnectStatueChangedListener = new OnTcpConnectStatueChangedListener() {

        @Override
        public void onConnected(ChannelHandlerContext ctx) {
            NotifyUtil.toast("长连接成功");
            isConnect = true;
            channel = ctx.channel();
            NettyClient.this.stopConnectThread();
        }

        @Override
        public void onDisconnected() {
            isConnect = false;
            channel = null;
        }

        @Override
        public void onError() {
            startConnectThread();
        }
    };

    private NettyClient() {

    }

    /**
     * 设置接受到消息后的通知对象
     */
    public NettyClient setOnReceiveDataListener(OnReceiveDataListener onReceiveDataListener) {
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
            connectThread = new ConnectThread(this.group, this.onTcpConnectStatueChangedListener, this.onReceiveDataListener);
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
        // 关闭 channel 通道
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        // 销毁Netty长连接线程池
        group.shutdownGracefully();
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
     * 是否连接
     */
    public boolean isConnect() {
        return channel != null && this.isConnect;
    }

    /**
     * 发送信息
     */
    public void sendDataPack(BasicPacket packet) {
        if (this.isConnect()) {
            ByteBuf buf = Unpooled.copiedBuffer(packet.toBytes());
            channel.writeAndFlush(buf);
        }
    }

    public void sendDataPack(BasicPacket packet, ChannelFutureListener listener) {
        if (this.isConnect()) {
//            ByteBuffer buffer = ByteBuffer.wrap(packet.toBytes());
            ByteBuf buf = Unpooled.copiedBuffer(packet.toBytes());
            channel.writeAndFlush(buf).addListener(listener);
        }
        // 服务器同步连接断开时,这句代码才会往下执行
        // 也就是服务端执行完这一句:ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
//        channel.closeFuture().sync();
    }

    /**
     * 获取单例对象
     */
    public static NettyClient getInstance() {
        if (client == null) {
            synchronized (NettyClient.class) {
                if (client == null) {
                    client = new NettyClient();
                }
            }
        }
        return NettyClient.client;
    }

}
