package com.example.spaceflow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaitingListResponse {

    private String status;
    private String message;
    private Integer position;
}
