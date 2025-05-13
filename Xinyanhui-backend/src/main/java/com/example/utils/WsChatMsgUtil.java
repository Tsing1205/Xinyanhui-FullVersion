package com.example.utils;

import com.alibaba.fastjson.JSON;
import com.example.pojo.WsChatMsg;

import java.time.LocalDateTime;

public class WsChatMsgUtil {

    public static String getWsChatMsgJson(boolean isSystem, String msg, LocalDateTime time) {
        WsChatMsg wsChatMsg = new WsChatMsg(isSystem, msg, time);
        return JSON.toJSONString(wsChatMsg);
    }
}
