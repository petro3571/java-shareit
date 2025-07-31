package ru.practicum.user;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserDto {
    private String name;

    @Email
    private String email;
}