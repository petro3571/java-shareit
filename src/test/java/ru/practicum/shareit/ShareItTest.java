package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.repository.NewItemRepo;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDtoWithDateAndComments;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.NewUserRepo;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShareItTest {

    @Mock
    private NewItemRepo itemRepository;

    @Mock
    private NewUserRepo userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;


    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void getItem_whenItemExists_thenReturnItemDtoWithDates() {
        // Подготовка данных
        Long userId = 1L;
        Long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);

        User owner = new User();
        owner.setId(2L);
        item.setOwner(owner);

        when(itemRepository.getById(itemId)).thenReturn(item);
        when(bookingRepository.findFirstByItemIdAndEndBeforeNow(itemId)).thenReturn(null);
        when(bookingRepository.findFirstByItemIdAndStartAfterNow(itemId)).thenReturn(null);
        when(commentRepository.findByItemId(itemId)).thenReturn(null);

        ItemDtoWithDateAndComments result = itemService.getItem(userId, itemId);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals("Test Item", result.getName());
        assertNull(result.getLastBooking());
        assertNull(result.getNextBooking());
        assertTrue(result.getComments().isEmpty());
    }

    @Test
    void addNewItem_whenUserNotExists_thenThrowNotFoundException() {
        Long userId = 99L;
        NewItemRequest request = new NewItemRequest();

        when(userRepository.findAll()).thenReturn(List.of());

        assertThrows(NotFoundUserException.class, () -> {
            itemService.addNewItem(userId, request);
        });
    }

    @Test
    void saveUser_whenValidData_thenUserSaved() {
        NewUserRequest request = new NewUserRequest("New User", "new@email.com");
        User savedUser = new User(1L, "new@email.com", "New User");

        when(userRepository.findAll()).thenReturn(List.of());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.saveUser(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New User", result.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_whenValidData_thenUserUpdated() {
        Long userId = 1L;
        UpdateUserRequest request = new UpdateUserRequest("Updated Name", "updated@email.com");
        User existingUser = new User(userId, "old@email.com", "Old Name");
        User updatedUser = new User(userId, "updated@email.com", "Updated Name");

        when(userRepository.getById(userId)).thenReturn(existingUser);
        when(userRepository.findAll()).thenReturn(List.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDto result = userService.updateUser(request, userId);

        assertEquals(userId, result.getId());
        assertEquals("Updated Name", result.getName());
        assertEquals("updated@email.com", result.getEmail());
    }

    @Test
    void deleteUser_whenUserExists_thenUserDeleted() {
        Long userId = 1L;
        User user = new User(userId,"delete@email.com", "User to delete");

        when(userRepository.getById(userId)).thenReturn(user);

        UserDto result = userService.deleteUser(userId);

        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void postBooking_whenValidData_thenBookingSaved() {
        Long userId = 1L;
        Long itemId = 1L;
        NewBookingRequest request = new NewBookingRequest(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                itemId
        );

        User booker = new User(userId, "Booker", "booker@email.com");
        Item item = new Item(itemId, new User(2L, "owner@email.com", "Owner"), "Item", "Description", true);
        Booking savedBooking = BookingMapper.mapToBooking(request);
        savedBooking.setId(1L);
        savedBooking.setBooker(booker);
        savedBooking.setItem(item);
        savedBooking.setStatus(BookingStatus.WAITING);

        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(userRepository.findAll()).thenReturn(List.of(booker));
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        BookingDto result = bookingService.postBooking(userId, request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(itemId, result.getItem().getId());
        assertEquals(userId, result.getBooker().getId());
        assertEquals(BookingStatus.WAITING, result.getStatus());
    }
}