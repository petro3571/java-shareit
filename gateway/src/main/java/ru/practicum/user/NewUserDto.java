package ru.practicum.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewUserDto {
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
}