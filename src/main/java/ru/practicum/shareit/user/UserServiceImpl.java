package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    @Override
    public UserDto saveUser(NewUserRequest user) {
        User newUser = repository.save(UserMapper.mapToUser(user));
        return UserMapper.mapToUserDto(newUser);
    }

    @Override
    public UserDto updateUser(UpdateUserRequest user,Long userId) {
        if (repository.getUserById(userId) == null) {
            throw new NotFoundException("Пользователя с id = " + userId + " нет.");
        }

        User updateUser = UserMapper.updateUserFields(new User(), user);
        updateUser.setId(userId);
        updateUser = repository.update(updateUser);
        return UserMapper.mapToUserDto(updateUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User findUser = repository.getUserById(userId);
        return UserMapper.mapToUserDto(findUser);
    }

    @Override
    public UserDto deleteUser(Long userId) {
            User userForDel = repository.getUserById(userId);
            userForDel = repository.deleteUser(userForDel);
            return UserMapper.mapToUserDto(userForDel);
    }
}