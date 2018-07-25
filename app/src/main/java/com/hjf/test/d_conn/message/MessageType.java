package com.hjf.test.d_conn.message;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        MessageType.UNKNOWN_TYPE,
        MessageType.MSG_TYPE_NOTICE_MSG,
        MessageType.MSG_TYPE_SECURITY_ALARM_MSG,
        // PacketType -> MessageType
        MessageType.HEART_BEAT_RESP,
        MessageType.MOBILE_REGISTER_RESP,
        MessageType.HUB_DEVICE_REGISTER_RESP,
        MessageType.HUB_DEVICE_UNREGISTER_RESP,
        MessageType.PUSH_CLOUD2MOBILE_RESP,
        MessageType.PUSH_MOBILE2DEVICE_RESP,
})
@Retention(RetentionPolicy.SOURCE)
public @interface MessageType {
    /**
     * 未知消息类型
     */
    String UNKNOWN_TYPE = "unknown_type";

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

    /**#############################################################
     * ###   {@link com.hjf.test.conn.pack.PacketType 转换类型}   ###
     * ###   只负责转换 cloud -> mobile 的 response 数据包就可以了   ###
     * #############################################################
     */
    /**
     * 心跳回应包
     */
    String HEART_BEAT_RESP = "heartbeat_resp";
    /**
     * App注册登录回应包
     */
    String MOBILE_REGISTER_RESP = "mobile_register_resp";
    /**
     * 主机注册绑定回应包
     */
    String HUB_DEVICE_REGISTER_RESP = "box_register_resp";
    /**
     * 主机解绑回应包
     */
    String HUB_DEVICE_UNREGISTER_RESP = "unregister_resp";
    /**
     * mobile -> cloud 回应包
     */
    String PUSH_CLOUD2MOBILE_RESP = "push_mobile_resp";
    /**
     * mobile -> device 回应包
     */
    String PUSH_MOBILE2DEVICE_RESP = "mobile_cmd_resp";

}