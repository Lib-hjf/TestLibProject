package com.hjf.test.conn.netty;

import android.support.annotation.NonNull;

import com.hjf.test.conn.OnReceiveDataListener;
import com.hjf.test.conn.pack.EmptyPacket;
import com.hjf.test.conn.pack.PacketType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 数据处理器
 */
final class PushDataHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 接受到消息后的通知对象
     */
    private OnReceiveDataListener onReceiveDateListener;
    private OnTcpConnectStatueChangedListener onTcpConnectStatueChangedListener;

    PushDataHandler(@NonNull OnTcpConnectStatueChangedListener onTcpConnectStatueChangedListener) {
        this.onTcpConnectStatueChangedListener = onTcpConnectStatueChangedListener;
    }

    /**
     * 连接建立成功，触发channelActive
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // 若把这一句注释掉将无法将event传递给下一个ClientHandler
        ctx.fireChannelActive();
        this.onTcpConnectStatueChangedListener.onConnected(ctx);
        // 回调连接建立成功信息
        if (onReceiveDateListener != null) {
            EmptyPacket connectSuccessPacket = new EmptyPacket(PacketType.CONNECT_SUCCESS);
            onReceiveDateListener.onReceiveData(connectSuccessPacket.toBytes());
        }
    }

    /**
     * 客户端收到消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (onReceiveDateListener != null) {
            onReceiveDateListener.onReceiveData(msg.array());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    /**
     * 利用写空闲发送心跳检测消息
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 断开连接触发channelInactive
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        this.onTcpConnectStatueChangedListener.onDisconnected();
    }

    /**
     * 异常回调,默认的exceptionCaught只会打出日志，不会关掉channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
        this.onTcpConnectStatueChangedListener.onError();
    }

    /**
     * 设置接受到消息后的通知对象
     */
    public void setOnReceiveDateListener(OnReceiveDataListener onReceiveDateListener) {
        this.onReceiveDateListener = onReceiveDateListener;
    }
}
