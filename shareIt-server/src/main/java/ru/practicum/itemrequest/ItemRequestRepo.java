package ru.practicum.itemrequest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepo extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByUser_Id(Long userId);

    List<ItemRequest> findAllByUser_IdNot(Long userId);
}
