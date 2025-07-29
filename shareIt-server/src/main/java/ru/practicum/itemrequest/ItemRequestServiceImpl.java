package ru.practicum.itemrequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.item.ItemDto;
import ru.practicum.item.ItemMapper;
import ru.practicum.item.ItemRepository;
import ru.practicum.itemrequest.responsetoitemreq.ResponseToItemReqDto;
import ru.practicum.itemrequest.responsetoitemreq.ResponseToItemReqMapper;
import ru.practicum.itemrequest.responsetoitemreq.ResponseToItemReqRepo;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepo repository;
    private final ResponseToItemReqRepo responseRepository;

    @Override
    public List<ItemRequestDtoWithResponses> getAllRequests(Long userId) {
        List<ItemRequestDtoWithResponses> listRequests = repository.findAllByUser_IdNot(userId).stream()
                .map(ItemRequestMapper::mapToRequestWithResponses).toList();
        List<ItemRequestDtoWithResponses> newList = listRequests.stream().peek(i -> {
            List<ResponseToItemReqDto> responses = responseRepository.findByRequest_Id(i.getId()).stream()
                    .map(ResponseToItemReqMapper::mapToDto).collect(Collectors.toList());
            if (!responses.isEmpty()) {
                i.setResponses(responses);
            }
        }).sorted(Comparator.comparing(ItemRequestDtoWithResponses::getCreated).reversed()).collect(Collectors.toList());
        return newList;
    }

    @Override
    public ItemRequestDtoWithResponses saveRequest(Long userId, ItemRequestDtoWithResponses itemRequestDto) {
        if (userRepository.getById(userId) == null) {
            throw new NotFoundException("Пользователя с id = " + userId + " нет.");
        }

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now().plusHours(8));
        itemRequest.setUser(userRepository.getById(userId));
        itemRequest = repository.save(itemRequest);

        return ItemRequestMapper.mapToRequestWithResponses(itemRequest);
    }

    @Override
    public List<ItemRequestDtoWithResponses> getOwnerRequests(Long userId) {
        if (userRepository.getById(userId) == null) {
            throw new NotFoundException("Пользователя с id = " + userId + " нет.");
        }

        List<ItemRequestDtoWithResponses> listOwnerRequests = repository.findByUser_Id(userId).stream()
                .map(ItemRequestMapper::mapToRequestWithResponses).toList();
        List<ItemRequestDtoWithResponses> newList = listOwnerRequests.stream().peek(i -> {
            List<ResponseToItemReqDto> responses = responseRepository.findByRequest_Id(i.getId()).stream()
                    .map(ResponseToItemReqMapper::mapToDto).collect(Collectors.toList());
            if (!responses.isEmpty()) {
                i.setResponses(responses);
            }
        }).sorted(Comparator.comparing(ItemRequestDtoWithResponses::getCreated).reversed()).collect(Collectors.toList());
        return newList;
    }


    @Override
    public ItemRequestDtoWithResponses getRequest(Long requestId, Long userId) {
        log.info("Запрос на получение данных о вещи от пользователя с id = {}", userId);
        ItemRequestDtoWithResponses itemRequest = ItemRequestMapper.mapToRequestWithResponses(repository.getById(requestId));
        List<ResponseToItemReqDto> responses = responseRepository.findByRequest_Id(itemRequest.getId())
                .stream().map(ResponseToItemReqMapper::mapToDto).toList();
        if (!responses.isEmpty()) {
            itemRequest.setResponses(responses);
        }

        List<ItemDto> items = itemRepository.findByRequest_id(requestId).stream()
                .map(ItemMapper::mapToItemDto).collect(Collectors.toList());

        if (!items.isEmpty()) {
            itemRequest.setItems(items);
        }

        return itemRequest;
    }
}
