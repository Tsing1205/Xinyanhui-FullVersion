package com.example.pojo;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;

@Entity
@Data
public class ConsultantSchedule {
    @Id
    private Integer scheduleId;

    private Integer consultantId;

    private LocalDate availableDate;
    private Integer startTime;
    private Integer endTime;
    private Integer slotCapacity = 5;
    private String status;
    private String note;

    public ConsultantSchedule() {
    }

    public ConsultantSchedule(Integer consultantId, LocalDate availableDate, Integer startTime, Integer endTime) {
        this.consultantId = consultantId;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
