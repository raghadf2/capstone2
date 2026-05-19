package com.example.spaceflow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {

    private Integer bookingId;
    private Double amount;
    private String status;
    private String message;
}
