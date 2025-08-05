import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItServerApp;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingRepository;
import ru.practicum.booking.BookingStatus;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = ShareItServerApp.class)
public class BookingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void whenSaveBooking_thenCanFindIt() {
        User owner = new User(null, "Owner", "owner@email.com");
        User booker = new User(null, "Booker", "booker@email.com");
        entityManager.persist(owner);
        entityManager.persist(booker);
        entityManager.flush();

        Item item = new Item(null, owner, "Item", "Description", true, null);
        entityManager.persist(item);
        entityManager.flush();

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        Booking booking = new Booking(null, start, end, item, booker, BookingStatus.WAITING);

        Booking savedBooking = bookingRepository.save(booking);

        assertNotNull(savedBooking.getId());

        assertEquals(booker.getId(), savedBooking.getBooker().getId());
        assertEquals(item.getId(), savedBooking.getItem().getId());
        assertEquals(BookingStatus.WAITING, savedBooking.getStatus());
    }
}