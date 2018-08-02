package com.hjf.test.t_conn.server;

import com.hjf.test.t_conn.message.BasicMessage;
import com.hjf.test.t_conn.message.MessageType;
import com.hjf.test.t_conn.message.MessageUtil;
import com.hjf.test.t_conn.pack.BasicPacket;

import org.hjf.kaconnect.OnReceiveDataListener;
import org.hjf.log.LogUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 长连接推送数据分发者
 */
public class PushDataDispatcher implements OnReceiveDataListener {

    private static PushDataDispatcher dataDispatcher;

    private HashMap<String, List<OnReceiveListener>> receiveListenerCache = new HashMap<>();

    private PushDataDispatcher() {
    }

    public static PushDataDispatcher getInstance() {
        if (dataDispatcher == null) {
            synchronized (PushDataDispatcher.class) {
                if (dataDispatcher == null) {
                    dataDispatcher = new PushDataDispatcher();
                }
            }
        }
        return PushDataDispatcher.dataDispatcher;
    }

    /**
     * 云端push下来的数据
     * 1. 使用 {@link BasicPacket} 解析出数据
     * 2. 使用 {@link BasicMessage} 作为载具在App组件之间传递
     */
    @Override
    public void onReceiveData(byte[] data) {

        // 1. 解析推送数据
        BasicPacket basicPack = BasicPacket.create().parseByte(data);

        // 2. 构成 BasicMessage 传递
        @MessageType String type = MessageUtil.getMessageType4PacketType(basicPack.getPacketType());
        BasicMessage basicMessage = new BasicMessage(type);
        basicMessage.setContent(basicPack.getContentStr());
        this.sendMessage(basicMessage);
    }

    /**
     * 发送消息，将消息发送给所有关注MessageType的监听对象
     * BasicMessage：可以是云端 push 的数据生成
     * 也可以是本地生成，取代部分广播的使用
     */
    public void sendMessage(BasicMessage msg) {

        List<OnReceiveListener> listeners = this.receiveListenerCache.get(msg.getType());

        if (listeners == null) {
            LogUtil.e("Not found listener for " + msg.getType());
            return;
        }
        LogUtil.e("Receive Message for" + msg.getType());

        for (OnReceiveListener listener : listeners) {
            try {
                listener.onReceive(new JSONObject(msg.getContent()));
            } catch (JSONException e) {
                LogUtil.e("BasicMessage content[" + msg.getContent() + "] cannot cost to JSONObject");
                listener.onReceive(new JSONObject());
            }
        }
    }


    /**
     * 接受到信息监听回调
     */
    public interface OnReceiveListener {

        void onReceive(JSONObject jsonObject);
    }

    /**
     * 注册监听Message通知事件
     */
    public synchronized void registerListener(@MessageType String msgType, OnReceiveListener listener) {

        if (!this.receiveListenerCache.containsKey(msgType)) {
            this.receiveListenerCache.put(msgType, new ArrayList<OnReceiveListener>());
        }

        List<OnReceiveListener> listeners = this.receiveListenerCache.get(msgType);
        listeners.add(listener);
    }

    /**
     * 注销已注册的Message通知事件
     */
    public synchronized void unregisterListener(@MessageType String msgTag, OnReceiveListener listener) {

        if (!this.receiveListenerCache.containsKey(msgTag)) {
            return;
        }

        List<OnReceiveListener> listeners = this.receiveListenerCache.get(msgTag);
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }
}
