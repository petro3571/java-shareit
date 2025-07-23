package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.NewUserDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    @Override
    public UserDto saveUser(NewUserDto user) {
//        if (repository.findAll().stream().map(u -> u.getEmail())
//                .collect(Collectors.toSet()).contains(user.getEmail())) {
//            throw new EmailAlreadyExistException("Пользователь с почтой " + user.getEmail() + " уже существует.");
//        }
        User newUser = repository.save(UserMapper.mapToUser(user));
        return UserMapper.mapToUserDto(newUser);
    }

//    @Override
//    public UserDto updateUser(UpdateUserRequest user, Long userId) {
//        if (repository.getById(userId) == null) {
//            throw new NotFoundException("Пользователя с id = " + userId + " нет.");
//        }
//
//        User updateUser = UserMapper.updateUserFields(new User(), user);
//
//        if (repository.findAll().stream().map(u -> u.getEmail())
//                .collect(Collectors.toSet()).contains(updateUser.getEmail()) && updateUser.getEmail() != null) {
//            throw new EmailAlreadyExistException("Пользователь с почтой " + user.getEmail() + " уже существует.");
//        }
//
//        updateUser.setId(userId);
//        updateUser = repository.save(updateUser);
//        return UserMapper.mapToUserDto(updateUser);
//    }

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
