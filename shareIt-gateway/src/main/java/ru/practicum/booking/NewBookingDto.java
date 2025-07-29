package ru.practicum.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewBookingDto {
    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    @Positive
    private Long itemId;

    private BookingStatus status = BookingStatus.WAITING;

    public NewBookingDto(LocalDateTime localDateTime, LocalDateTime localDateTime1, Long itemId) {
        this.start = localDateTime;
        this.end = localDateTime1;
        this.itemId = itemId;
    }
}
