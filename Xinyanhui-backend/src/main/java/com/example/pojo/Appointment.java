package com.example.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "Appointments")
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "consultant_id", nullable = false)
    private Integer consultantId;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;

    @Column(name = "booking_date", nullable = false, updatable = false)
    private LocalDateTime bookingDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @Column(name = "cancellation_time", nullable = true)
    private LocalDateTime cancellationTime;

    @Column(name = "cancellation_reason", nullable = true)
    private String cancellationReason;

    private String consultantName;

    private String UserName;

    public Appointment() {
    }

    public Appointment(Integer userId, Integer consultantId, LocalDate appointmentDate, LocalTime appointmentTime, AppointmentStatus status) {
        this.userId = userId;
        this.consultantId = consultantId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }
}