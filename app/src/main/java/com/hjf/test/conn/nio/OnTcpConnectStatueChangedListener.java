package com.hjf.test.conn.nio;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 长连接状态改变回调
 */
interface OnTcpConnectStatueChangedListener {

    void onConnected(Selector selector, SocketChannel socketChannel) throws IOException;

    void onDisconnected();

    void onError();
}
