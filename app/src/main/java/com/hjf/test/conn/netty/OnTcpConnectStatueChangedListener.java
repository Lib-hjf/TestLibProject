package com.hjf.test.conn.netty;

import io.netty.channel.ChannelHandlerContext;

/**
 * 长连接建立成功回调
 */
interface OnTcpConnectStatueChangedListener {

    void onConnected(ChannelHandlerContext ctx);

    void onDisconnected();

    void onError();
}
