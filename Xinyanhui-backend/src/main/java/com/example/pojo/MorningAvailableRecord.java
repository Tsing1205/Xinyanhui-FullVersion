package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MorningAvailableRecord {
    private LocalDate availableDate;
    private short hour8Available;
    private short hour9Available;
    private short hour10Available;
    private short hour11Available;
    private short hour12Available;
}
