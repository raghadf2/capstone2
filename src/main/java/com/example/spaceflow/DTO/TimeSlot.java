package com.example.spaceflow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TimeSlot {

    private LocalTime start;
    private LocalTime end;
}
