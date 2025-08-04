package ru.practicum.itemrequest;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRequestRepo extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByUser_Id(Long userId);

    Page<ItemRequest> findAllByUser_IdNot(Long userId, Pageable pageable);

}