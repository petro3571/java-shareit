import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ShareItServerApp;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.itemrequest.ItemRequestDtoWithResponses;
import ru.practicum.itemrequest.ItemRequestRepo;
import ru.practicum.itemrequest.ItemRequestService;
import ru.practicum.itemrequest.responsetoitemreq.ResponseToItemReq;
import ru.practicum.itemrequest.responsetoitemreq.ResponseToItemReqRepo;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = ShareItServerApp.class)
class ItemRequestServiceImplIntegrationTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepo itemRequestRepo;

    @Autowired
    private ResponseToItemReqRepo responseRepo;

    private User requestOwner;
    private User itemOwner;
    private ItemRequestDtoWithResponses request1;
    private ItemRequestDtoWithResponses request2;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        requestOwner = new User();
        requestOwner.setName("Request Owner");
        requestOwner.setEmail("request.owner@example.com");
        requestOwner = userRepository.save(requestOwner);

        itemOwner = new User();
        itemOwner.setName("Item Owner");
        itemOwner.setEmail("item.owner@example.com");
        itemOwner = userRepository.save(itemOwner);

        request1 = createItemRequest(requestOwner, "Нужна дрель");
        request2 = createItemRequest(requestOwner, "Нужен перфоратор");

        item1 = createItem(itemOwner, "Дрель", true);
        item2 = createItem(itemOwner, "Перфоратор", true);

        createResponse(item1, request1);
        createResponse(item2, request2);
    }

    private ItemRequestDtoWithResponses createItemRequest(User user, String description) {
        ItemRequestDtoWithResponses dto = new ItemRequestDtoWithResponses();
        dto.setDescription(description);
        return itemRequestService.saveRequest(user.getId(), dto);
    }

    private Item createItem(User owner, String name, boolean available) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(name + " description");
        item.setAvailable(available);
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    private void createResponse(Item item, ItemRequestDtoWithResponses request) {
        ResponseToItemReq response = new ResponseToItemReq();
        response.setItem(item);
        response.setItemRequest(itemRequestRepo.findById(request.getId()).orElseThrow());
//        response.(LocalDateTime.now());
        responseRepo.save(response);
    }

    @Test
    void getOwnerRequests_ShouldReturnAllRequestsWithResponses() {
        List<ItemRequestDtoWithResponses> result = itemRequestService.getOwnerRequests(requestOwner.getId());

        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.get(0).getCreated().isAfter(result.get(1).getCreated()));

        result.forEach(request -> {
            assertFalse(request.getResponses().isEmpty());
            assertEquals(1, request.getResponses().size());
            assertEquals(request.getId() == request1.getId() ? "Дрель" : "Перфоратор",
                    request.getResponses().get(0).getNameItem());
        });
    }

    @Test
    void getOwnerRequests_ShouldReturnEmptyListWhenNoRequests() {
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new@example.com");
        newUser = userRepository.save(newUser);

        List<ItemRequestDtoWithResponses> result = itemRequestService.getOwnerRequests(newUser.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getOwnerRequests_ShouldReturnRequestsWithoutResponsesWhenNoResponses() {
        ItemRequestDtoWithResponses newRequest = createItemRequest(requestOwner, "Новый запрос без ответов");

        List<ItemRequestDtoWithResponses> result = itemRequestService.getOwnerRequests(requestOwner.getId());

        assertNotNull(result);
        assertEquals(3, result.size());

        ItemRequestDtoWithResponses foundRequest = result.stream()
                .filter(r -> r.getId().equals(newRequest.getId()))
                .findFirst()
                .orElseThrow();

        assertTrue(foundRequest.getResponses().isEmpty());
    }
}