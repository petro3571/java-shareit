import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItServerApp;
import ru.practicum.booking.BookingDto;
import ru.practicum.booking.BookingStatus;
import ru.practicum.item.ItemDto;
import ru.practicum.user.UserDto;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItServerApp.class)
public class BookingDtoJsonTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws Exception {
        ItemDto itemDto = new ItemDto(
                1L,
                "Drel",
                "Description",
                true,
                null,
                null,
                Collections.emptyList(),
                null);

        UserDto userDto = new UserDto(
                1L,
                "john.doe@mail.com",
                "John");

        BookingDto bookingDto = new BookingDto(
                1L,
                null,
                null,
                itemDto,
                itemDto.getName(),
                userDto,
                BookingStatus.WAITING,
                itemDto.getId());

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(null);
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.itemName").isEqualTo("Drel");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }
}