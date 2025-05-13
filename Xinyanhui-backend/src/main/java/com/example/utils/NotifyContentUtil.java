package com.example.utils;

import java.util.Map;

public class NotifyContentUtil {
    private static final String USER_CONSULTATION_BOARD = "个人界面-我的咨询";
    private static final String CONSULTANT_CONSULTATION_BOARD = "预约/咨询";
    private static final String SUPERVISOR_CONSULTATION_BOARD = "会话求助";
    private static final String ADMIN_REQUEST_BOARD = "请假申请";

    private static final String WINDOW_LEAD_STR = "详情请至消息中心查看";

    public static String CENTER_LEAD_STR(String boardName){
        return "更多信息请至"+boardName+"查看";
    }

    public static String OPERATION_STR(String operation){
        return "您已成功"+operation;
    }

    public static String STATUS_CHANGE_STR(String name,String status){
        if(status==null){
            return "您有一条"+name;
        }
        return "您有一条"+name+status;
    }

    public static String DETAIL_STR(Map<String,Object> map){
        StringBuilder sb = new StringBuilder();
        sb.append("详情为： ");
        for(Map.Entry<String,Object> entry:map.entrySet()){
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append(" ");
        }
        return sb.toString();
    }

    public static String[] bookNotificationOfUser(Map<String,Object> map){
        String mainStr = OPERATION_STR("预约咨询");
        String detailStr = DETAIL_STR(map);
        String []msg = new String[2];
        msg[0] = mainStr+", "+WINDOW_LEAD_STR+".";
        msg[1] = mainStr+", "+detailStr+", " +CENTER_LEAD_STR(USER_CONSULTATION_BOARD)+".";
        return msg;
    }

    public static String[] bookNotificationOfConsultant(Map<String,Object> map){
        String mainStr = STATUS_CHANGE_STR("新的咨询预约",null);
        String detailStr = DETAIL_STR(map);
        String []msg = new String[2];
        msg[0] = mainStr+" , "+WINDOW_LEAD_STR+".";
        msg[1] = mainStr+", "+detailStr+", " +CENTER_LEAD_STR(CONSULTANT_CONSULTATION_BOARD)+".";
        return msg;
    }

    public static String[] cancelNotificationOfUser(Map<String,Object> map){
        String mainStr = OPERATION_STR("取消预约");
        String detailStr = DETAIL_STR(map);
        String []msg = new String[2];
        msg[0] = mainStr+", "+WINDOW_LEAD_STR+".";
        msg[1] = mainStr+", "+detailStr+".";
        return msg;
    }

    public static String[] cancelNotificationOfConsultant(Map<String,Object> map){
        String mainStr = STATUS_CHANGE_STR("咨询预约状态变更为", "已取消");
        String detailStr = DETAIL_STR(map);
        String []msg = new String[2];
        msg[0] = mainStr+", "+WINDOW_LEAD_STR+".";
        msg[1] = mainStr+", "+detailStr+", " +CENTER_LEAD_STR(CONSULTANT_CONSULTATION_BOARD)+".";
        return msg;
    }

    public static String[] approveNotificationOfConsultant(Map<String,Object> map){
        String mainStr = STATUS_CHANGE_STR("请假申请", "已通过");
        String detailStr = DETAIL_STR(map);
        String []msg = new String[2];
        msg[0] = mainStr+", "+WINDOW_LEAD_STR+".";
        msg[1] = mainStr+", "+detailStr+".";
        return msg;
    }

    public static String[] approveNotificationOfUser(Map<String,Object> map){
        String mainStr = STATUS_CHANGE_STR("预约状态变更为", "请假");
        String detailStr = DETAIL_STR(map);
        String []msg = new String[2];
        msg[0] = mainStr+", "+WINDOW_LEAD_STR+".";
        msg[1] = mainStr+", "+detailStr+".";
        return msg;
    }

    public static String[] rejectNotificationOfConsultant(Map<String,Object> map){
        String mainStr = STATUS_CHANGE_STR("请假申请", "被拒绝");
        String detailStr = DETAIL_STR(map);
        String []msg = new String[2];
        msg[0] = mainStr+", "+WINDOW_LEAD_STR+".";
        msg[1] = mainStr+", "+detailStr+".";
        return msg;
    }

    public static String[] requestNotificationOfAdmin(Map<String,Object> map){
        String mainStr = STATUS_CHANGE_STR("新的请假申请","待处理");
        String detailStr = DETAIL_STR(map);
        String []msg = new String[2];
        msg[0] = mainStr+", "+WINDOW_LEAD_STR+".";
        msg[1] = mainStr+", "+detailStr+", " +CENTER_LEAD_STR(ADMIN_REQUEST_BOARD)+".";
        return msg;
    }

}
