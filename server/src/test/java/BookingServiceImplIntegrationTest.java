import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ShareItServerApp;
import ru.practicum.booking.*;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.NotFoundUserException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemMapper;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = ShareItServerApp.class)
class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private User booker;
    private User owner;
    private User otherUser;
    private Item item;
    private Item unavailableItem;
    private Booking pastBooking;
    private Booking currentBooking;
    private Booking futureBooking;
    private Booking waitingBooking;
    private Booking rejectedBooking;
    private BookingDto bookingRequest;

    @BeforeEach
    void setUp() {
        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@example.com");
        booker = userRepository.save(booker);

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner = userRepository.save(owner);

        otherUser = new User();
        otherUser.setName("Other");
        otherUser.setEmail("other@example.com");
        otherUser = userRepository.save(otherUser);

        item = new Item();
        item.setName("Дрель");
        item.setDescription("Аккумуляторная дрель");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        unavailableItem = new Item();
        unavailableItem.setName("Drel");
        unavailableItem.setDescription("Description");
        unavailableItem.setAvailable(false);
        unavailableItem.setOwner(owner);
        unavailableItem = itemRepository.save(unavailableItem);

        pastBooking = createBooking(booker, item,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                BookingStatus.APPROVED);

        currentBooking = createBooking(booker, item,
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1),
                BookingStatus.APPROVED);

        futureBooking = createBooking(booker, item,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                BookingStatus.APPROVED);

        waitingBooking = createBooking(booker, item,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(4),
                BookingStatus.WAITING);

        rejectedBooking = createBooking(booker, item,
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(6),
                BookingStatus.REJECTED);

        bookingRequest = new BookingDto();
        bookingRequest.setItem(ItemMapper.mapToItemDto(item));
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusHours(3));
        bookingRequest.setEnd(LocalDateTime.now().plusHours(4));
        bookingRequest.setStatus(BookingStatus.WAITING);

    }

    private Booking createBooking(User booker, Item item,
                                  LocalDateTime start, LocalDateTime end,
                                  BookingStatus status) {
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStartDate(start);
        booking.setEndDate(end);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Test
    void getBookings_AllState_ShouldReturnAllBookings() {
        List<BookingDto> result = bookingService.getBookings(booker.getId(), "ALL");

        assertNotNull(result);
        assertEquals(5, result.size());
    }

    @Test
    void getBookings_CurrentState_ShouldReturnCurrentBookings() {
        List<BookingDto> result = bookingService.getBookings(booker.getId(), "CURRENT");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(currentBooking.getId(), result.get(0).getId());
    }

    @Test
    void getBookings_PastState_ShouldReturnPastBookings() {
        List<BookingDto> result = bookingService.getBookings(booker.getId(), "PAST");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(pastBooking.getId(), result.get(0).getId());
    }

    @Test
    void getBookings_FutureState_ShouldReturnFutureBookings() {
        List<BookingDto> result = bookingService.getBookings(booker.getId(), "FUTURE");

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void getBookings_WaitingState_ShouldReturnWaitingBookings() {
        List<BookingDto> result = bookingService.getBookings(booker.getId(), "WAITING");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(waitingBooking.getId(), result.get(0).getId());
    }

    @Test
    void getBookings_RejectedState_ShouldReturnRejectedBookings() {
        List<BookingDto> result = bookingService.getBookings(booker.getId(), "REJECTED");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(rejectedBooking.getId(), result.get(0).getId());
    }

    @Test
    void getBookings_UnknownState_ShouldThrowException() {
        assertThrows(NotFoundException.class, () -> {
            bookingService.getBookings(booker.getId(), "UNKNOWN");
        });
    }

    @Test
    void getBookings_NonExistentUser_ShouldThrowException() {
        assertThrows(NotFoundException.class, () -> {
            bookingService.getBookings(999L, "ALL");
        });
    }

    @Test
    void getBookings_ShouldReturnEmptyListWhenNoBookings() {
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new@example.com");
        newUser = userRepository.save(newUser);

        List<BookingDto> result = bookingService.getBookings(newUser.getId(), "ALL");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void postBooking_ShouldCreateNewBooking() {
        BookingDto result = bookingService.postBooking(booker.getId(), bookingRequest);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(booker.getId(), result.getBooker().getId());
        assertEquals(item.getId(), result.getItem().getId());
        assertEquals(BookingStatus.WAITING, result.getStatus());
    }

    @Test
    void postBooking_WhenItemNotExists_ShouldThrowException() {
        bookingRequest.setItemId(999L);

        assertThrows(NotFoundUserException.class, () -> {
            bookingService.postBooking(booker.getId(), bookingRequest);
        });
    }

    @Test
    void postBooking_WhenUserNotExists_ShouldThrowException() {
        assertThrows(NotFoundException.class, () -> {
            bookingService.postBooking(999L, bookingRequest);
        });
    }

    @Test
    void postBooking_WhenItemUnavailable_ShouldThrowException() {
        bookingRequest.setItemId(unavailableItem.getId());

        assertThrows(NotFoundException.class, () -> {
            bookingService.postBooking(booker.getId(), bookingRequest);
        });
    }

    @Test
    void patchApproved_ShouldApproveBooking() {
        BookingDto created = bookingService.postBooking(booker.getId(), bookingRequest);
        BookingDto result = bookingService.patchApproved(owner.getId(), created.getId(), "true");

        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void patchApproved_ShouldRejectBooking() {
        BookingDto created = bookingService.postBooking(booker.getId(), bookingRequest);
        BookingDto result = bookingService.patchApproved(owner.getId(), created.getId(), "false");

        assertEquals(BookingStatus.REJECTED, result.getStatus());
    }

    @Test
    void patchApproved_WhenNotOwner_ShouldThrowException() {
        BookingDto created = bookingService.postBooking(booker.getId(), bookingRequest);

        assertThrows(NotFoundException.class, () -> {
            bookingService.patchApproved(booker.getId(), created.getId(), "true");
        });
    }

    @Test
    void patchApproved_WhenBookingNotExists_ShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> {
            bookingService.patchApproved(owner.getId(), 999L, "true");
        });
    }

    @Test
    void getInfo_ShouldReturnBookingForBooker() {
        BookingDto created = bookingService.postBooking(booker.getId(), bookingRequest);
        BookingDto result = bookingService.getInfo(booker.getId(), created.getId());

        assertEquals(created.getId(), result.getId());
    }

    @Test
    void getInfo_ShouldReturnBookingForOwner() {
        BookingDto created = bookingService.postBooking(booker.getId(), bookingRequest);
        BookingDto result = bookingService.getInfo(owner.getId(), created.getId());

        assertEquals(created.getId(), result.getId());
    }

    @Test
    void getInfo_WhenNotBookerOrOwner_ShouldThrowException() {
        BookingDto created = bookingService.postBooking(booker.getId(), bookingRequest);

        assertThrows(NotFoundException.class, () -> {
            bookingService.getInfo(otherUser.getId(), created.getId());
        });
    }

    @Test
    void getInfo_WhenBookingNotExists_ShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> {
            bookingService.getInfo(booker.getId(), 999L);
        });
    }

    @Test
    void getBookingOwner_AllState_ShouldReturnAllBookings() {
        bookingService.postBooking(booker.getId(), bookingRequest);
        List<BookingDto> result = bookingService.getBookingOwner(owner.getId(), "ALL");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getBookingOwner_WhenNoItems_ShouldReturnEmptyList() {
        User userWithoutItems = new User();
        userWithoutItems.setName("NoItems");
        userWithoutItems.setEmail("noitems@example.com");
        userWithoutItems = userRepository.save(userWithoutItems);

        List<BookingDto> result = bookingService.getBookingOwner(userWithoutItems.getId(), "ALL");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}