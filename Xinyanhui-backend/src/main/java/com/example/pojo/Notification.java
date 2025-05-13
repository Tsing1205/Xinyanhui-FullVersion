package com.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("Notification")
public class Notification {
    @TableId(type = IdType.AUTO)
    private Integer notfId;
    private Integer recId;
    private String content;
    private String recRole;
    private NotificationStatus status;
    private LocalDateTime  createTime;
}
