package com.example.spaceflow.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Category title cannot be empty")
    @Size(min=3,max=30,message ="Category title length is invalid,must be between 3 to 30")
    @Column(unique = true, nullable = false, length = 30)
    private String name;

    @NotEmpty(message = "Category description must be provided")
    @Size(min=10,max=100,message ="Description length must be between 10 and 100")
    @Column(nullable = false, length = 100)
    private String description;
}
