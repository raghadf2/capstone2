package com.example.spaceflow.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name field cannot be left blank")
    @Size(min=2,max=10,message ="Name length must stay within 2 to 10 characters")
    @Column(nullable = false, length = 10)
    private String name;

    @NotEmpty(message = "Username field cannot be left blank")
    @Size(min=5,max=10,message ="UserName length must stay within 5 to 10 characters")
    @Column(unique = true,nullable = false, length = 10)
    private String userName;

    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Invalid email format")
    @Column(unique = true,nullable = false)
    private String email;

    @NotEmpty(message = "Password field cannot be left blank")
    @Size(min=8,max=50,message ="Password length must stay within 8 to 50")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,50}$", message = "Password must contain uppercase, lowercase, number and allowed special characters (@$!%*?&)")
    @Column(nullable = false, length = 50)
    private String password;

    @NotEmpty(message = "Phone number field is missing")
    @Pattern(regexp = "^(05\\d{8}|\\+9665\\d{8})$", message = "Phone must follow Saudi format (05XXXXXXXX or +9665XXXXXXXX)")
    @Column(unique = true,nullable = false, length = 13)
    private String phone;

    @NotEmpty(message = "Role must be assigned")
    @Pattern(regexp = "^(?i)(USER|OWNER|ADMIN)$",message = "Role value is not recognized")
    @Column(nullable = false)
    private String role;
}
