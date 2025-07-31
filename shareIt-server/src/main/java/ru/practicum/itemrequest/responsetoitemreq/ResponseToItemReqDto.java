package ru.practicum.itemrequest.responsetoitemreq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseToItemReqDto {
    private Long id;
    private Long itemId;
    private String nameItem;
    private Long ownerId;
    private Long requestId;
}