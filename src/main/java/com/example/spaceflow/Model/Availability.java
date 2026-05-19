package com.example.spaceflow.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Space reference is required")
    @Column(nullable = false)
    private Integer spaceId;

    @NotEmpty(message = "Day value is required")
    @Pattern(regexp = "^(MON|TUE|WED|THU|FRI|SAT|SUN)$",message = "Invalid day format.Day must be MON, TUE, WED, THU, FRI, SAT or SUN")
    @Column(nullable = false, length = 3)
    private String day;

    @NotNull(message = "Start time must be specified")
    @Column(nullable = false)
    private LocalTime startTime;

    @NotNull(message = "End time must be specified")
    @Column(nullable = false)
    private LocalTime endTime;

    @NotNull(message = "Availability flag must be set")
    @Column(nullable = false)
    private Boolean isAvailable;
}
