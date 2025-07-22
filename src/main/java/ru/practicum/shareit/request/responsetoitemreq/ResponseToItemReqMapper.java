package ru.practicum.shareit.request.responsetoitemreq;

public class ResponseToItemReqMapper {
    public static ResponseToItemReqDto mapToDto(ResponseToItemReq response) {
        ResponseToItemReqDto dto = new ResponseToItemReqDto();
        dto.setId(response.getId());
        dto.setItemId(response.getItem().getId());
        dto.setNameItem(response.getItem().getName());
        dto.setOwnerId(response.getItem().getOwner().getId());
        dto.setRequestId(response.getItemRequest().getId());

        return dto;
    }
}
