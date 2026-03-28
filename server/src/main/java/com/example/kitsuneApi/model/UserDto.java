package com.example.kitsuneApi.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank(message = "Username needs to be provided")
    private String username;
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String bio;
    private String profilePicture;
    private Integer points;

    private Integer level;
    private Integer xpToNextLevel;
    private String role;
}
