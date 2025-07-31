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
import ru.practicum.booking.BookingController;
import ru.practicum.booking.BookingDto;
import ru.practicum.booking.BookingService;
import ru.practicum.booking.BookingStatus;
import ru.practicum.item.ItemDto;
import ru.practicum.user.UserDto;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private BookingDto bookingDto;
    private final long userId = 1L;
    private final String approved = "true";
    private final String state = "ALL";

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

        bookingDto = new BookingDto(
                1L,
                null,
                null,
                itemDto,
                itemDto.getName(),
                userDto,
                BookingStatus.WAITING,
                itemDto.getId());
    }

    @Test
    void postBooking() throws Exception {
        when(bookingService.postBooking(anyLong(), any()))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.itemName", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItem().getId()), Long.class));

    }

    @Test
    void patchBooking() throws Exception {
        when(bookingService.patchApproved(anyLong(), anyLong(), anyString()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", approved)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.itemName", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItem().getId()), Long.class));
    }

    @Test
    void getInfoBooking() throws Exception {
        when(bookingService.getInfo(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd())))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.itemName", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItem().getId()), Long.class));
    }

    @Test
    void getBookings() throws Exception {
        when(bookingService.getBookings(anyLong(), anyString()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart())))
                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd())))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].itemName", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$[0].itemId", is(bookingDto.getItem().getId()), Long.class));
    }

    @Test
    void getBookingsOwner() throws Exception {
        when(bookingService.getBookingOwner(anyLong(), anyString()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart())))
                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd())))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].itemName", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$[0].itemId", is(bookingDto.getItem().getId()), Long.class));
    }
}