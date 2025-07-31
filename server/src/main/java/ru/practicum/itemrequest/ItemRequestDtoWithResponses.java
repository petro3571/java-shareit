package ru.practicum.itemrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.ItemDto;
import ru.practicum.itemrequest.responsetoitemreq.ResponseToItemReqDto;
import ru.practicum.user.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDtoWithResponses {
    private Long id;

    private String description;

    private LocalDateTime created;

    private UserDto user;

    private List<ItemDto> items = new ArrayList<>();

    private List<ResponseToItemReqDto> responses = new ArrayList<>();
}