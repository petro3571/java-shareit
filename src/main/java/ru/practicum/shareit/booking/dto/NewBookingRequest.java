package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
public class NewBookingRequest {
    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    @Positive
    private Long itemId;

    private BookingStatus status = BookingStatus.WAITING;

    public NewBookingRequest(LocalDateTime localDateTime, LocalDateTime localDateTime1, Long itemId) {
        this.start = localDateTime;
        this.end = localDateTime1;
        this.itemId = itemId;
    }
}