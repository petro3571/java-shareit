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
import ru.practicum.item.Item;
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
    private Item item;
    private Booking pastBooking;
    private Booking currentBooking;
    private Booking futureBooking;
    private Booking waitingBooking;
    private Booking rejectedBooking;

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

        item = new Item();
        item.setName("Дрель");
        item.setDescription("Аккумуляторная дрель");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);

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
}