package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final NewUserRepo repository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    @Override
    @Transactional
    public UserDto saveUser(NewUserRequest user) {
        if (repository.findAll().stream().map(u -> u.getEmail())
                .collect(Collectors.toSet()).contains(user.getEmail())) {
            throw new EmailAlreadyExistException("Пользователь с почтой " + user.getEmail() + " уже существует.");
        }
        User newUser = repository.save(UserMapper.mapToUser(user));
        return UserMapper.mapToUserDto(newUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(UpdateUserRequest user, Long userId) {
        if (repository.getById(userId) == null) {
            throw new NotFoundException("Пользователя с id = " + userId + " нет.");
        }

        if (repository.findAll().stream().map(u -> u.getEmail())
                .collect(Collectors.toSet()).contains(user.getEmail())) {
            throw new EmailAlreadyExistException("Пользователь с почтой " + user.getEmail() + " уже существует.");
        }

        User updateUser = UserMapper.updateUserFields(new User(), user);
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
    @Transactional
    public UserDto deleteUser(Long userId) {
            User userForDel = repository.getById(userId);
            repository.delete(userForDel);
            return UserMapper.mapToUserDto(userForDel);
    }
}