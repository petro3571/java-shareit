package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface NewItemRepo extends JpaRepository<Item, Long> {
    @Query(value = "select * " +
            "from items as it " +
            "where (LOWER(it.name) like LOWER(?1) OR LOWER(it.description) like LOWER(?1)) AND it.available = true", nativeQuery = true
            )
    List<Item> searchItemBy(String text);

    List<Item> findByOwner_id(Long userId);

    @Query(value = "select it " +
            "from Item as it " +
            "where it.requestId = ?1"
    )
    List<Item> findByRequest_id(Long requestId);
}