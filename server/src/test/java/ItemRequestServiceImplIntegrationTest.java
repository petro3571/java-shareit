import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
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
    private User anotherUser;
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

        anotherUser = new User();
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@example.com");
        anotherUser = userRepository.save(anotherUser);

        itemOwner = new User();
        itemOwner.setName("Item Owner");
        itemOwner.setEmail("item.owner@example.com");
        itemOwner = userRepository.save(itemOwner);

        request1 = createItemRequest(requestOwner, "Нужна дрель");
        request2 = createItemRequest(requestOwner, "Нужен перфоратор");

        item1 = createItem(itemOwner, "Дрель", true, request1.getId());
        item2 = createItem(itemOwner, "Перфоратор", true, request2.getId());

        createResponse(item1, request1);
        createResponse(item2, request2);
    }

    private ItemRequestDtoWithResponses createItemRequest(User user, String description) {
        ItemRequestDtoWithResponses dto = new ItemRequestDtoWithResponses();
        dto.setDescription(description);
        return itemRequestService.saveRequest(user.getId(), dto);
    }

    private void createResponse(Item item, ItemRequestDtoWithResponses request) {
        ResponseToItemReq response = new ResponseToItemReq();
        response.setItem(item);
        response.setItemRequest(itemRequestRepo.findById(request.getId()).orElseThrow());
        responseRepo.save(response);
    }

    private Item createItem(User owner, String name, boolean available, Long requestId) {
        Item item = new Item();
        item.setName(name);
        item.setDescription(name + " description");
        item.setAvailable(available);
        item.setOwner(owner);
        item.setRequestId(itemRequestRepo.findById(requestId).get().getId());
        return itemRepository.save(item);
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

    @Test
    void saveRequest_ShouldCreateNewRequest() {
        ItemRequestDtoWithResponses newRequest = new ItemRequestDtoWithResponses();
        newRequest.setDescription("Новый запрос");

        ItemRequestDtoWithResponses result = itemRequestService.saveRequest(anotherUser.getId(), newRequest);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Новый запрос", result.getDescription());
        assertEquals(anotherUser.getId(), result.getUser().getId());
    }

    @Test
    void saveRequest_WhenUserNotFound_ShouldThrowException() {
        ItemRequestDtoWithResponses newRequest = new ItemRequestDtoWithResponses();
        newRequest.setDescription("Новый запрос");

        assertThrows(DataIntegrityViolationException.class, () -> {
            itemRequestService.saveRequest(999L, newRequest);
        });
    }

    @Test
    void getOwnerRequests_ShouldReturnAllRequestsWithResponse() {
        List<ItemRequestDtoWithResponses> result = itemRequestService.getOwnerRequests(requestOwner.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getCreated().isAfter(result.get(1).getCreated()));
    }

    @Test
    void getOwnerRequests_WhenNoRequests_ShouldReturnEmptyList() {
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new@example.com");
        newUser = userRepository.save(newUser);

        List<ItemRequestDtoWithResponses> result = itemRequestService.getOwnerRequests(newUser.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllRequests_ShouldReturnRequestsFromOtherUsers() {
        List<ItemRequestDtoWithResponses> result = itemRequestService.getAllRequests(anotherUser.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        result.forEach(request -> {
            assertEquals(requestOwner.getId(), request.getUser().getId());
            assertEquals(1, request.getResponses().size());
        });
    }

    @Test
    void getAllRequests_WhenNoOtherRequests_ShouldReturnList() {
        List<ItemRequestDtoWithResponses> result = itemRequestService.getAllRequests(anotherUser.getId());

        assertNotNull(result);
    }

    @Test
    void getRequest_ShouldReturnRequestWithResponsesAndItems() {
        ItemRequestDtoWithResponses result = itemRequestService.getRequest(request1.getId(), anotherUser.getId());

        assertNotNull(result);
        assertEquals(request1.getId(), result.getId());
        assertEquals(1, result.getResponses().size());
        assertEquals(1, result.getItems().size());
        assertEquals("Дрель", result.getItems().get(0).getName());
    }

    @Test
    void getRequest_WhenRequestNotFound_ShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> {
            itemRequestService.getRequest(999L, anotherUser.getId());
        });
    }

    @Test
    void getRequest_WhenNoResponses_ShouldReturnRequestWithoutResponses() {
        ItemRequestDtoWithResponses newRequest = createItemRequest(anotherUser, "Запрос без ответов");

        ItemRequestDtoWithResponses result = itemRequestService.getRequest(newRequest.getId(), requestOwner.getId());

        assertNotNull(result);
        assertEquals(newRequest.getId(), result.getId());
        assertTrue(result.getResponses().isEmpty());
    }
}