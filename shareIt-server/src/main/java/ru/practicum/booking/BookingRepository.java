package ru.practicum.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_Id(Long bookerId);

    Booking findByBooker_IdAndItem_Id(Long bookerId, Long itemId);

    List<Booking> findByEndDateBefore(LocalDateTime localDateTime);

    List<Booking> findByEndDateAfter(LocalDateTime localDateTime);

    List<Booking> findByStatusLike(BookingStatus status);

    List<Booking> findByItemIdIn(List<Long> itemIds);

    @Query(value = "SELECT b.id, b.start_date, b.end_date FROM booking as b WHERE b.item_id = ?1 AND b.start_date > ?2 LIMIT 1", nativeQuery = true)
    BookingDtoWithDate findFirstByItemIdAndStartAfter(Long itemId, LocalDateTime start);

    @Query("SELECT  new ru.practicum.booking.BookingDtoWithDate(b.id, b.startDate, b.endDate) FROM Booking b WHERE b.item.id = :itemId AND b.startDate > CURRENT_TIMESTAMP ORDER BY b.startDate ASC")
    BookingDtoWithDate findFirstByItemIdAndStartAfterNow(Long itemId);

    @Query("SELECT new ru.practicum.booking.BookingDtoWithDate(b.id, b.startDate, b.endDate) FROM Booking b WHERE b.item.id = :itemId AND b.endDate < CURRENT_TIMESTAMP ORDER BY b.endDate DESC")
    BookingDtoWithDate findFirstByItemIdAndEndBeforeNow(Long itemId);

    List<Booking> findByBookerIdAndStartDateBeforeAndEndDateAfter(
            Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

}