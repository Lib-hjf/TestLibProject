
package com.hjf.test.t_conn.pack;


import org.hjf.util.log.LogUtil;
import org.json.JSONObject;


// 发送 2.4G 请求包 的串口读写 请求包 ，后面是 json 串
public class WirelessPacket extends BasicPacket {

    public WirelessPacket() {
        super.packetType = (byte) 0x24;
    }

    @Override
    protected void preBuildBytes() {
        super.preBuildBytes();
        try {
            JSONObject json = new JSONObject();
            json.put("cmd_type", "hub");
            json.put("cmd", "get_all");
            super.contentStr = json.toString();
        } catch (Exception e) {
            super.contentStr = "";
        }
        LogUtil.d("build_req() json_str = " + super.contentStr);
    }
}

