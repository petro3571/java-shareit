import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ShareItServerApp;
import ru.practicum.user.UserDto;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import ru.practicum.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = ShareItServerApp.class)
class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User existingUser;
    private UserDto updateDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setName("Existing User");
        existingUser.setEmail("existing@email.com");
        existingUser = userRepository.save(existingUser);

        updateDto = new UserDto();
        updateDto.setName("Updated Name");
        updateDto.setEmail("updated@email.com");

        userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@email.com");
    }

    @Test
    void updateUser_ShouldUpdateAllFields() {
        UserDto result = userService.updateUser(updateDto, existingUser.getId());

        assertNotNull(result);
        assertEquals(existingUser.getId(), result.getId());
        assertEquals(updateDto.getName(), result.getName());
        assertEquals(updateDto.getEmail(), result.getEmail());

        User updatedUser = userRepository.findById(existingUser.getId()).orElseThrow();
        assertEquals(updateDto.getName(), updatedUser.getName());
        assertEquals(updateDto.getEmail(), updatedUser.getEmail());
    }

    @Test
    void updateUser_ShouldUpdateOnlyNameWhenEmailNull() {
        updateDto.setEmail(null);

        UserDto result = userService.updateUser(updateDto, existingUser.getId());

        assertNotNull(result);
        assertEquals(existingUser.getId(), result.getId());
        assertEquals(updateDto.getName(), result.getName());
        assertEquals(existingUser.getEmail(), result.getEmail()); // Email остался прежним
    }

    @Test
    void updateUser_ShouldUpdateOnlyEmailWhenNameNull() {
        updateDto.setName(null);

        UserDto result = userService.updateUser(updateDto, existingUser.getId());

        assertNotNull(result);
        assertEquals(existingUser.getId(), result.getId());
        assertEquals(existingUser.getName(), result.getName()); // Имя осталось прежним
        assertEquals(updateDto.getEmail(), result.getEmail());
    }

    @Test
    void updateUser_ShouldThrowExceptionWhenUserNotFound() {
        Long nonExistentId = 999L;

        assertThrows(JpaObjectRetrievalFailureException.class, () -> {
            userService.updateUser(updateDto, nonExistentId);
        });
    }

    @Test
    void updateUser_ShouldThrowExceptionWhenEmailAlreadyExists() {
        User anotherUser = new User();
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@email.com");
        userRepository.save(anotherUser);

        updateDto.setEmail(anotherUser.getEmail());

        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(updateDto, existingUser.getId());
        });
    }

    // Тесты для getAllUsers
    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(existingUser.getId(), result.get(0).getId());
    }

    @Test
    void getAllUsers_WhenNoUsers_ShouldReturnEmptyList() {
        userRepository.deleteAll();

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Тесты для saveUser
    @Test
    void saveUser_ShouldCreateNewUser() {
        UserDto result = userService.saveUser(userDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void saveUser_WhenEmailExists_ShouldThrowException() {
        userDto.setEmail(existingUser.getEmail());

        assertThrows(RuntimeException.class, () -> {
            userService.saveUser(userDto);
        });
    }

    // Тесты для getUserById
    @Test
    void getUserById_ShouldReturnUser() {
        UserDto result = userService.getUserById(existingUser.getId());

        assertNotNull(result);
        assertEquals(existingUser.getId(), result.getId());
        assertEquals(existingUser.getName(), result.getName());
        assertEquals(existingUser.getEmail(), result.getEmail());
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
    }

    // Тесты для deleteUser
    @Test
    void deleteUser_ShouldDeleteUser() {
        UserDto result = userService.deleteUser(existingUser.getId());

        assertNotNull(result);
        assertEquals(existingUser.getId(), result.getId());
        assertFalse(userRepository.existsById(existingUser.getId()));
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(999L);
        });
    }

    // Дополнительные тесты для проверки уникальности email
    @Test
    void saveUser_WithDuplicateEmail_ShouldThrowDataIntegrityException() {
        UserDto duplicateEmailUser = new UserDto();
        duplicateEmailUser.setName("Duplicate Email");
        duplicateEmailUser.setEmail(existingUser.getEmail());

        assertThrows(DataIntegrityViolationException.class, () -> {
            try {
                userService.saveUser(duplicateEmailUser);
            } catch (RuntimeException e) {
                throw new DataIntegrityViolationException(e.getMessage());
            }
        });
    }

    @Test
    void saveUser_ShouldCorrectlyMapAllFields() {
        UserDto newUser = new UserDto();
        newUser.setName("Full Fields User");
        newUser.setEmail("full@fields.com");

        UserDto result = userService.saveUser(newUser);

        assertNotNull(result);
        assertEquals(newUser.getName(), result.getName());
        assertEquals(newUser.getEmail(), result.getEmail());
    }
}
