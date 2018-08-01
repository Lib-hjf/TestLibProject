package com.hjf.util;

import android.content.Context;
import android.text.TextUtils;

import org.hjf.util.EnvironUtils;

public class AppEnvUtil {

    /**
     * 根据本地信息获取CHANNEL_ID，手机的唯一标识
     */
    public static String getChannelId(Context context, String phone_num) {
        // mac 地址
        String channelId = EnvironUtils.getMacAddress(context);
        // IMEI 属性值
        if (TextUtils.isEmpty(channelId)){
            channelId = EnvironUtils.getIMEI(context);
        }
        // 根据手机号码计算
        if(TextUtils.isEmpty(channelId) && !TextUtils.isEmpty(phone_num)) {
            channelId = phone_num + System.currentTimeMillis();
        }
        if(!TextUtils.isEmpty(channelId)) {
            channelId = channelId.toLowerCase();
        }
        // 默认赋值
        if (TextUtils.isEmpty(channelId)){
            channelId = "00:00:00:00:00:00";
        }
        return channelId;
    }
}
