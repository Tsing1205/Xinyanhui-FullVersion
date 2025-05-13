package com.example.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class WsChatMsg {
    private boolean isSystem;
    private String msg;

    @JSONField(format = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime time;
}
