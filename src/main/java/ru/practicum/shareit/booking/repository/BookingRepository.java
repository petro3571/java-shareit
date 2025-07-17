package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoWithDate;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_Id(Long bookerId);

    List<Booking> findByEndDateBefore(LocalDateTime localDateTime);

    List<Booking> findByEndDateAfter(LocalDateTime localDateTime);

    List<Booking> findByStatusLike(BookingStatus status);

    List<Booking> findByItemIdIn(List<Long> itemIds);

    @Query("SELECT  new ru.practicum.shareit.booking.dto.BookingDtoWithDate(b.id, b.startDate, b.endDate) FROM Booking b WHERE b.item.id = :itemId AND b.startDate > CURRENT_TIMESTAMP ORDER BY b.startDate ASC")
    BookingDtoWithDate findFirstByItemIdAndStartAfterNow(Long itemId);

    @Query("SELECT new ru.practicum.shareit.booking.dto.BookingDtoWithDate(b.id, b.startDate, b.endDate) FROM Booking b WHERE b.item.id = :itemId AND b.endDate < CURRENT_TIMESTAMP ORDER BY b.endDate DESC")
    BookingDtoWithDate findFirstByItemIdAndEndBeforeNow(Long itemId);

    List<Booking> findByBookerIdAndStartDateBeforeAndEndDateAfter(
            Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

}