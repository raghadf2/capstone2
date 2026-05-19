package com.example.spaceflow.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "User reference is required")
    @Column(nullable = false)
    private Integer userId;

    @NotNull(message = "Space reference is required")
    @Column(nullable = false)
    private Integer spaceId;
}
