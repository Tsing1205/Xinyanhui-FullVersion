package com.example.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@TableName("SupervisorConsultations")
public class SupervisorConsultation {
    @Id
    @TableId(type = IdType.AUTO)
    private Integer recordId;

    private Integer consultantId;
    private Integer supervisorId;
    private Integer sessionId;

    private LocalDateTime requestTime;
    private LocalDateTime responseTime;

    public SupervisorConsultation() {
    }

}
