package com.example.spaceflow.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "User ID must be provided")
    @Column(nullable = false)
    private Integer userId;

    @NotNull(message = "Space ID must be provided")
    @Column(nullable = false)
    private Integer spaceId;

    @NotNull(message = "Booking date must be specified")
    @Column(nullable = false)
    private LocalDate bookingDate;

    @NotNull(message = "Start time must be specified")
    @Column(nullable = false)
    private LocalTime startTime;

    @NotNull(message = "End time must be specified")
    @Column(nullable = false)
    private LocalTime endTime;

    @NotNull(message = "Total price must be provided")
    @Positive(message = "Total price must be a positive value")
    @Column(nullable = false)
    private Double totalPrice;

    @NotEmpty(message = "Booking status cannot be empty")
    @Pattern(regexp = "^(PENDING|CONFIRMED|CANCELLED|COMPLETED)$",message = "Invalid booking status.Status must be PENDING, CONFIRMED, CANCELLED or COMPLETED")
    @Column(nullable = false, length = 15)
    private String status;
}
