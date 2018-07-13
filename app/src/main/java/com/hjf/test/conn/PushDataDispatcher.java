package com.hjf.test.conn;

import android.support.annotation.StringDef;

import com.hjf.test.conn.pack.BasicPacket;
import com.hjf.test.conn.pack.PacketType;
import com.hjf.test.conn.pack.PacketUtil;

import org.hjf.liblogx.LogUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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
     * 收到推送数据，转为{@link BasicPacket}在分发
     */
    @Override
    public void onReceiveData(byte[] data) {

        BasicPacket basicPack = BasicPacket.create().parseByte(data);

        String msgTypeStr = basicPack.getMsgTypeStr();
        // TODO BasicPacket.packetType  BasicPacket.msgTypeStr
        // TODO 长连接做玩后可不可以统一任务
        List<OnReceiveListener> listeners = this.receiveListenerCache.get(msgTypeStr);

        if (listeners == null) {
            LogUtil.e("Not found " + OnReceiveListener.class);
            return;
        }

        for (OnReceiveListener listener : listeners) {
            try {
                listener.onReceive(new JSONObject(basicPack.getContentStr()));
            } catch (JSONException e) {
                listener.onReceive(new JSONObject());
                LogUtil.e("Message content[" + basicPack.getContentStr() + "] cannot cost to JSONObject");
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
     * 注册监听
     */
    public synchronized void registerListener(@MessageTag String msgType, OnReceiveListener listener) {

        if (!this.receiveListenerCache.containsKey(msgType)) {
            this.receiveListenerCache.put(msgType, new ArrayList<OnReceiveListener>());
        }

        List<OnReceiveListener> listeners = this.receiveListenerCache.get(msgType);
        listeners.add(listener);
    }

    /**
     * 注册监听
     */
    public synchronized void registerListener(@PacketType int packType, OnReceiveListener listener) {
        String msgType = PacketUtil.getMsgType(packType);
        if (!this.receiveListenerCache.containsKey(msgType)) {
            this.receiveListenerCache.put(msgType, new ArrayList<OnReceiveListener>());
        }

        List<OnReceiveListener> listeners = this.receiveListenerCache.get(msgType);
        listeners.add(listener);
    }

    public synchronized void unregisterListener(@MessageTag String msgTag, OnReceiveListener listener) {

        if (!this.receiveListenerCache.containsKey(msgTag)) {
            return;
        }

        List<OnReceiveListener> listeners = this.receiveListenerCache.get(msgTag);
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    @StringDef({
            MessageTag.CONNECT_SUCCESS,
            MessageTag.MSG_TYPE_NOTICE_MSG,
            MessageTag.MSG_TYPE_SECURITY_ALARM_MSG
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageTag {

        String CONNECT_SUCCESS = "connect_success";

        // 1.分享消息
        String MSG_TYPE_SHARE_MSG = "share.msg";
        // 2.提示信息 ，包括 红外触发
        String MSG_TYPE_NOTICE_MSG = "notice.msg";
        // 3.SOS消息
        String MSG_TYPE_SOS_MSG = "sos";
        // 4.SOS提示信息
        String MSG_TYPE_SOS_NOTICE_MSG = "notice";
        // 5.第三方消息
        String MSG_TYPE_SOS_THIRTH_MSG = "third";
        // 6.健康消息
        String MSG_TYPE_HEALTH_MSG = "health.msg";
        // 7.家庭安全
        String MSG_TYPE_SECURITY_ALARM_MSG = "alarm";
        // 家庭安全模块推送消息,新接口
        String MSG_TYPE_SECURITY = "security";
        // 8.低功耗电量过低/设备的正常、异常（健康度）/设备信息更改
        String MSG_TYPE_DEV_SYNC = "dev.sync";
        // 9.告警消息(电压过低)
        String MSG_TYPE_DEV_VOLTAGE_ALARM = "alert.msg";
        // 10.新设备信息(非智能设备/智能设备)
        String MSG_TYPE_DEV_NEW_DUMB_DEV = "new_dev";
        String MSG_TYPE_DEV_NEW_WISE_DEV = "new_wise";
        // 11.提示信息 ，设备上线、下线等等
        String MSG_TYPE_DEV_MSG = "dev.msg";
        // 12.家庭信息/家庭成员消息
        String MSG_TYPE_USER_SYNC = "user.sync";
        // 邀请加入家庭消息
        String MSG_TYPE_USER_INVITE = "user.invite";
        // 13.好友信息更改/好友增加、删除命令
        String MSG_TYPE_FRIEND_MSG = "sys.msg";
        // 14.服务状态改变/场景状态更改/视频播放状态
        String MSG_TYPE_SVC_STATE = "svc_state";
        // 15.服务同步
        String MSG_TYPE_SVC_SYNC = "svc_sync";
        // 16.配置改变/家环境配置数据更改/安保配置数据更改
        String MSG_TYPE_CONFIG_CHANGE = "cfg_change";
        // 17.房间信息同步
        String MSG_TYPE_ROOM_SYNC = "home.sync";
        // 18.SESSION_ID同步
        String MSG_TYPE_SESSION_SYNC = "session";
        // 19.关联关系同步
        String MSG_TYPE_CR_SYNC = "cr.sync";
        // 20.关联关系同步
        String MSG_TYPE_UDR_SYNC = "udr_sync";
        // 21.查询/设置客户端参数配置・
        String MSG_TYPE_QUERY_APP_CONFIG = "qry";
        // 22.音乐变化,喜马拉雅、虾米
        String MSG_TYPE_RADIO_MSG = "radio";
        // 23.健康设备与用户关系变更通知
        String MSG_TYPE_HEALTH_DEV_SYNC = "health.dev_bind_user";
    }
}
