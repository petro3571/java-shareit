import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.ShareItServerApp;
import ru.practicum.item.ItemDto;
import ru.practicum.itemrequest.ItemRequestDtoWithResponses;
import ru.practicum.itemrequest.responsetoitemreq.ResponseToItemReqDto;
import ru.practicum.user.UserDto;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItServerApp.class)
public class ItemRequestJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDtoWithResponses> json;

    @Test
    void testItemRequestDto() throws Exception {
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

        ItemRequestDtoWithResponses requestDto = new ItemRequestDtoWithResponses(
                1L,
                "Нужна дрель для ремонта",
                null,
                userDto,
                List.of(itemDto),
                List.of(new ResponseToItemReqDto(
                        1L,
                        itemDto.getId(),
                        itemDto.getName(),
                        1L,
                        1L))

        );

        JsonContent<ItemRequestDtoWithResponses> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Нужна дрель для ремонта");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(null);
        assertThat(result).extractingJsonPathNumberValue("$.user.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("Drel");
    }
}