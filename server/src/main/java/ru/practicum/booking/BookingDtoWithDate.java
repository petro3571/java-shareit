package ru.practicum.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoWithDate {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
}