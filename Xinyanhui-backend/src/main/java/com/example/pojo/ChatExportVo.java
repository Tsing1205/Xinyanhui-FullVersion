package com.example.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatExportVo {
    private String sender;
    private String receiver;
    private String message;
    private LocalDateTime timestamp;
    private String imageUrl;

    public ChatExportVo() {
    }

    public ChatExportVo(String sender, String receiver, String message, LocalDateTime timestamp, String imageUrl) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
    }
}
