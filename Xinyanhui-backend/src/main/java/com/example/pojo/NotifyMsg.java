package com.example.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.constants.NotifyConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifyMsg {
    private int type;
    private String msg;

    @JSONField(format="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;
    private Object data;

    public static NotifyMsg getSessionMsg(Object data) {
        return new NotifyMsg(NotifyConstant.SESSION_CODE, "您有新的会话", LocalDateTime.now(), data);
    }

    public static NotifyMsg getNormalMsg(String msg) {
        return new NotifyMsg(NotifyConstant.NORMAL_CODE, msg, LocalDateTime.now(), null);
    }

    public static NotifyMsg getCancelMsg(Object data){
        return new NotifyMsg(NotifyConstant.CANCEL_CODE,"您好，您有一条预约记录变更",LocalDateTime.now(),data);
    }
}
