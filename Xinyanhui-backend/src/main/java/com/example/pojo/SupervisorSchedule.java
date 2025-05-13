package com.example.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class SupervisorSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleId;

    private Integer supervisorId;

    private LocalDate availableDate;
    private Integer startTime;
    private Integer endTime;
    private Integer slotCapacity = 5;

    public SupervisorSchedule() {
    }

    public SupervisorSchedule(Integer supervisorId, LocalDate availableDate, Integer startTime, Integer endTime) {
        this.supervisorId = supervisorId;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
