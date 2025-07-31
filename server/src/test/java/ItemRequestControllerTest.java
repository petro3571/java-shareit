import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.item.ItemDto;
import ru.practicum.itemrequest.ItemRequestController;
import ru.practicum.itemrequest.ItemRequestDtoWithResponses;
import ru.practicum.itemrequest.ItemRequestService;
import ru.practicum.itemrequest.responsetoitemreq.ResponseToItemReqDto;
import ru.practicum.user.UserDto;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;

    private ItemRequestDtoWithResponses requestDto;
    private final long userId = 1L;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

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

        requestDto = new ItemRequestDtoWithResponses(
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
    }

    @Test
    void postItemRequest() throws Exception {
        when(itemRequestService.saveRequest(anyLong(), any()))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", is("Drel")));
    }

    @Test
    void getOwnerRequests() throws Exception {
        when(itemRequestService.getOwnerRequests(anyLong()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())));
    }

    @Test
    void getAllRequests() throws Exception {
        when(itemRequestService.getAllRequests(anyLong()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].name", is("Drel")));
    }

    @Test
    void getRequest() throws Exception {
        when(itemRequestService.getRequest(anyLong(), anyLong()))
                .thenReturn(requestDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.items", hasSize(1)));
    }
}