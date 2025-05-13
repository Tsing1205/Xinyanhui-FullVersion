package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfternoonAvailableRecord {
    private LocalDate availableDate;
    private short hour13Available;
    private short hour14Available;
    private short hour15Available;
    private short hour16Available;
    private short hour17Available;
}
