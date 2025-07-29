package ru.practicum.itemrequest.responsetoitemreq;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResponseToItemReqRepo extends JpaRepository<ResponseToItemReq, Long> {
    @Query(value = "select r " +
            "from ResponseToItemReq as r " +
            "where r.itemRequest.id = ?1"
    )
    List<ResponseToItemReq> findByRequest_Id(Long requestId);
}