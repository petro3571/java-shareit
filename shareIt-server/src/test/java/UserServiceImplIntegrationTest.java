import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ShareItServerApp;
import ru.practicum.user.UserDto;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import ru.practicum.user.UserService;

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

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setName("Existing User");
        existingUser.setEmail("existing@email.com");
        existingUser = userRepository.save(existingUser);

        updateDto = new UserDto();
        updateDto.setName("Updated Name");
        updateDto.setEmail("updated@email.com");
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
}