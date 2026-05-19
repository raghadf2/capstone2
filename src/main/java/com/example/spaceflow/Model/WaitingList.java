package com.example.spaceflow.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WaitingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "User reference is required")
    @Column(nullable = false)
    private Integer userId;

    @NotNull(message = "Space reference is required")
    @Column(nullable = false)
    private Integer spaceId;

    @NotNull(message = "Date must be not empty")
    @FutureOrPresent(message = "Date must be today or in the future")
    private LocalDate date;

    @NotNull(message = "Start time must be specified")
    @Column(nullable = false)
    private LocalTime startTime;

    @NotNull(message = "End time must be specified")
    @Column(nullable = false)
    private LocalTime endTime;

    @NotNull(message = "Position is required")
    @Min(value = 1, message = "Position must be at least 1")
    private Integer position;

    @NotEmpty(message = "Status must be not empty")
    @Pattern(regexp = "WAITING|NOTIFIED",message = "status must be WAITING or NOTIFIED")
    private String status;
}
