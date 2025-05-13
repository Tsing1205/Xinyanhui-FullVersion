package com.example.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatMsg {
    private String msg;
    private Boolean senderType;    //false(0) is user/supervisor,(true) 1 is consultant

    @JSONField(format = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;
}
