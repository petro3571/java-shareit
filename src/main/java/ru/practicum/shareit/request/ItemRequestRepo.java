package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface ItemRequestRepo extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByUser_Id(Long userId);

    List<ItemRequest> findAllByUser_IdNot(Long userId);
}
