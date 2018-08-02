package com.hjf.test.t_conn.pack;

import android.content.Context;

import com.hjf.test.R;
import com.hjf.util.AppEnvUtil;

import org.hjf.log.LogUtil;
import org.hjf.util.EnvironUtils;
import org.hjf.util.NetworkUtils;
import org.json.JSONObject;

// 手机/PAD 端 连接注册包
public class RegisterPacket extends BasicPacket {

    private String userMobile;
    private String password;
    private String channelID;
    private String channelType = "1";
    private int appID;
    private String ip;
    private String appVersionName;
    private String phoneModel = android.os.Build.MODEL;
    private String netType;

    public RegisterPacket(Context context, String ip) {
        super.packetType = PacketType.MOBILE_REGISTER_REQ;
        this.ip = ip;
        this.appID = Integer.parseInt(context.getString(R.string.app_id));
        this.netType = String.valueOf(NetworkUtils.getNetWorkType(context));
        this.appVersionName = EnvironUtils.getAppVersionName(context);
        this.channelID = AppEnvUtil.getChannelId(context, "");
    }

    @Override
    protected void preBuildBytes() {
        super.preBuildBytes();
        try {
            JSONObject json = new JSONObject();
            json.put("channel_id", channelID);
            json.put("channel_type", channelType);
            json.put("model", phoneModel);
            json.put("net_type", String.valueOf(netType));
            json.put("app_id", appID);
            json.put("internal_ip", ip);
            json.put("ver_name", appVersionName);
            json.put("mobile", userMobile);
            json.put("passwd", password);
            super.contentStr = json.toString();
        } catch (Exception e) {
            super.contentStr = "";
        }
        LogUtil.d("build_req() json_str = " + super.contentStr);
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
