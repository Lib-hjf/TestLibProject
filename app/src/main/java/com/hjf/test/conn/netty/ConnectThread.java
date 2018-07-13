package com.hjf.test.conn.netty;

import com.hjf.test.conn.OnReceiveDataListener;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

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

    private Condition condition = new ReentrantLock().newCondition();

    /**
     * 进行连接的对象
     */
    private Bootstrap bootstrap;

    ConnectThread(EventLoopGroup group, final OnTcpConnectStatueChangedListener onTcpConnectStatueChangedListener, final OnReceiveDataListener onReceiveDataListener) {
        bootstrap = new Bootstrap().group(group)
                // 心跳TODO
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 禁用nagle算法 Nagle算法就是为了尽可能发送大块数据，避免网络中充斥着许多小数据块。
                .option(ChannelOption.TCP_NODELAY, true)
                // 5s未发送数据，回调 userEventTriggered
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                // 指定使用NioSocketChannel, 用NioSctpChannel会抛异常
                .channel(NioSocketChannel.class)
                // 对常连接信道进行初始化操作
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 超时设置： 5s未发送数据，回调userEventTriggered
                        ch.pipeline().addLast(new IdleStateHandler(0, 5, 0, TimeUnit.MINUTES));
                        // 解码器设置
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                        ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                        // 开启日志，可以设置日志等级
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        // 开启SSL
                        SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
                        ch.pipeline().addLast(sslCtx.newHandler(ch.alloc()));
                        // 数据处理Handler设置
                        PushDataHandler pushDataHandler = new PushDataHandler(onTcpConnectStatueChangedListener);
                        pushDataHandler.setOnReceiveDateListener(onReceiveDataListener);
                        ch.pipeline().addLast(pushDataHandler);
                    }
                });
    }

    @Override
    @Deprecated
    public void run() {
        super.run();

        isRunning = true;
        // 连接结果回调
        ChannelFutureListener channelFutureListener = new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                // 连接成功
                if (future.isSuccess()) {
                    needDestroy = true;
                }
                // 释放阻塞
                condition.signalAll();
            }
        };

        while (true) {

            // 线程终止执行
            if (needDestroy) {
                break;
            }

            // 进行连接
            try {
                bootstrap.connect(host, port).addListener(channelFutureListener).sync();
                // 阻塞线程
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 重试间隔
            try {
                Thread.sleep(getThreadSleepTimeNum(tryNum++));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
