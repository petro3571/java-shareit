package ru.practicum.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_Id(Long bookerId);

    Booking findByBooker_IdAndItem_Id(Long bookerId, Long itemId);

    List<Booking> findByEndDateBefore(LocalDateTime localDateTime);

    List<Booking> findByStartDateAfter(LocalDateTime localDateTime);

    List<Booking> findByStatusLike(BookingStatus status);

    List<Booking> findByItemIdIn(List<Long> itemIds);

    @Query("SELECT new ru.practicum.booking.BookingDtoWithDate(b.id, b.startDate, b.endDate) FROM Booking b WHERE b.item.id = :itemId AND b.startDate > :now ORDER BY b.startDate ASC LIMIT 1")
    BookingDtoWithDate findFirstByItemIdAndStartAfter(@Param("itemId") Long itemId, @Param("now") LocalDateTime now);

    @Query("SELECT new ru.practicum.booking.BookingDtoWithDate(b.id, b.startDate, b.endDate) FROM Booking b WHERE b.item.id = :itemId AND b.endDate < CURRENT_TIMESTAMP ORDER BY b.endDate DESC")
    BookingDtoWithDate findFirstByItemIdAndEndBeforeNow(Long itemId);

    List<Booking> findByBookerIdAndStartDateBeforeAndEndDateAfter(
            Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);
}