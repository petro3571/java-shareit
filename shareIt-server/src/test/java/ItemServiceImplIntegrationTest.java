import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ShareItServerApp;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingStatus;
import ru.practicum.item.comment.CommentRepository;
import ru.practicum.item.comment.Comment;
import ru.practicum.item.ItemRepository;
import ru.practicum.item.ItemService;
import ru.practicum.item.ItemDto;
import ru.practicum.item.Item;
import ru.practicum.user.UserRepository;
import ru.practicum.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = ShareItServerApp.class)
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User owner;
    private User booker;
    private Item item;
    private Booking lastBooking;
    private Booking nextBooking;
    private Comment comment;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner = userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@example.com");
        booker = userRepository.save(booker);

        item = new Item();
        item.setName("Drel");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        lastBooking = new Booking();
        lastBooking.setItem(item);
        lastBooking.setBooker(booker);
        lastBooking.setStatus(BookingStatus.APPROVED);
        lastBooking.setStartDate(LocalDateTime.now().minusDays(2));
        lastBooking.setEndDate(LocalDateTime.now().minusDays(1));
        lastBooking = bookingRepository.save(lastBooking);

        nextBooking = new Booking();
        nextBooking.setItem(item);
        nextBooking.setBooker(booker);
        nextBooking.setStatus(BookingStatus.APPROVED);
        nextBooking.setStartDate(LocalDateTime.now().plusDays(1));
        nextBooking.setEndDate(LocalDateTime.now().plusDays(2));
        nextBooking = bookingRepository.save(nextBooking);

        comment = new Comment();
        comment.setText("Отличная дрель!");
        comment.setItem(item);
        comment.setAuthor(booker);
        comment.setCreated(LocalDateTime.now());
        comment = commentRepository.save(comment);
    }

    @Test
    void getItem_ShouldReturnItemWithAllFields() {
        ItemDto result = itemService.getItem(owner.getId(), item.getId());

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.isAvailable(), result.isAvailable());

        assertNotNull(result.getLastBooking());
        assertEquals(lastBooking.getId(), result.getLastBooking().getId());

        assertNotNull(result.getNextBooking());
        assertEquals(nextBooking.getId(), result.getNextBooking().getId());

        assertFalse(result.getComments().isEmpty());
        assertEquals(1, result.getComments().size());
        assertEquals(comment.getText(), result.getComments().get(0).getText());
    }

    @Test
    void getItem_WhenNoBookings_ShouldReturnItemWithoutBookings() {
        bookingRepository.deleteAll();

        ItemDto result = itemService.getItem(owner.getId(), item.getId());

        assertNull(result.getLastBooking());
        assertNull(result.getNextBooking());
    }

    @Test
    void getItem_WhenNoComments_ShouldReturnItemWithoutComments() {
        commentRepository.deleteAll();

        ItemDto result = itemService.getItem(owner.getId(), item.getId());

        assertTrue(result.getComments().isEmpty());
    }

    @Test
    void getItem_WhenLastBookingNotFinished_ShouldReturnWithoutLastBooking() {
        bookingRepository.deleteAll();

        Booking currentBooking = new Booking();
        currentBooking.setItem(item);
        currentBooking.setBooker(booker);
        currentBooking.setStatus(BookingStatus.APPROVED);
        currentBooking.setStartDate(LocalDateTime.now().minusHours(1));
        currentBooking.setEndDate(LocalDateTime.now().plusHours(1));
        bookingRepository.save(currentBooking);

        ItemDto result = itemService.getItem(owner.getId(), item.getId());

        assertNull(result.getLastBooking());
    }
}