package com.example.spaceflow.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Space title field cannot be left blank")
    @Size(min=4,max=40,message ="Space title must be within 4–40 characters")
    @Column(unique = true, nullable = false, length = 40)
    private String name;

    @NotEmpty(message = "Space Description must be filled")
    @Size(min=20,max=200,message ="Description should be detailed (20–200 chars)")
    @Column(nullable = false, length = 200)
    private String description;

    @NotNull(message = "Hourly price is missing")
    @Positive(message = "Hourly price must be greater than zero")
    @Column(nullable = false)
    private Double pricePerHour;


    @NotNull(message = "Capacity value is required")
    @Min(value =1, message = "Capacity must be at least 1")
    @Max(value =1000, message = "Capacity exceeds allowed limit")
    @Column(nullable = false)
    private Integer capacity;

    @NotEmpty(message = "Size must be specified")
    @Pattern(regexp = "^(Small|Medium|Large)$",message = "Invalid size value.Size must be Small, Medium or Large")
    @Column(nullable = false)
    private String size;


    @NotEmpty(message = "City cannot be empty")
    @Size(min =3, max = 30,message = "City name length is invalid")
    @Column(nullable = false, length = 30)
    private String city;

    @NotEmpty(message = "Address must be entered")
    @Size(min = 5, max = 100,message ="Address length is out of range")
    @Column(nullable = false, length = 100)
    private String address;

    @NotEmpty(message = "Image link is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String imageUrl;


    @ElementCollection
    @NotEmpty(message = "Space must include at least one facility")
    private List<String> facilities;


    @NotNull(message = "Category reference is missing")
    @Column(nullable = false)
    private Integer categoryId;


    @NotNull(message = "Owner reference is missing")
    @Column(nullable = false)
    private Integer ownerId;

    @NotEmpty(message = "Status must be defined")
    @Pattern(regexp = "^(ACTIVE|INACTIVE|PENDING)$",message = "Invalid status value.Status must be ACTIVE, INACTIVE or PENDING")
    @Column(nullable = false, length = 10)
    private String status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
