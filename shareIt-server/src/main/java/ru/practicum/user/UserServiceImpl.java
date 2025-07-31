package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    @Override
    public UserDto saveUser(UserDto user) {
        if (repository.findAll().stream().map(u -> u.getEmail())
                .collect(Collectors.toSet()).contains(user.getEmail())) {
            throw new RuntimeException("Пользователь с почтой " + user.getEmail() + " уже существует.");
        }
        User newUser = repository.save(UserMapper.mapToUser(user));
        return UserMapper.mapToUserDto(newUser);
    }

    @Override
    public UserDto updateUser(UserDto user, Long userId) {
        if (repository.getById(userId) == null) {
            throw new NotFoundException("Пользователя с id = " + userId + " нет.");
        }

        User updateUser = UserMapper.updateUserFields(new User(), user);

        if (repository.findAll().stream().map(u -> u.getEmail())
                .collect(Collectors.toSet()).contains(updateUser.getEmail()) && updateUser.getEmail() != null) {
            throw new RuntimeException("Пользователь с почтой " + user.getEmail() + " уже существует.");
        }

        updateUser.setId(userId);
        updateUser = repository.save(updateUser);
        return UserMapper.mapToUserDto(updateUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        User findUser = repository.getById(userId);
        return UserMapper.mapToUserDto(findUser);
    }

    @Override
    public UserDto deleteUser(Long userId) {
        User userForDel = repository.getById(userId);
        repository.delete(userForDel);
        return UserMapper.mapToUserDto(userForDel);
    }
}