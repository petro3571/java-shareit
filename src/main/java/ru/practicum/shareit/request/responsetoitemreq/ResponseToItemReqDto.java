package ru.practicum.shareit.request.responsetoitemreq;

import lombok.Data;

@Data
public class ResponseToItemReqDto {
    private Long id;
    private Long itemId;
    private String nameItem;
    private Long ownerId;
    private Long requestId;
}
