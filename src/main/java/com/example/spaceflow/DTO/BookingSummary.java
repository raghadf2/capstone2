package com.example.spaceflow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class BookingSummary {

    private String spaceName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double totalPrice;
}
