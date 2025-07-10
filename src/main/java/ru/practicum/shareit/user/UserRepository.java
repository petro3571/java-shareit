package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    User save(User user);

    User update(User user);

    User getUserById(Long userId);

    User deleteUser(User user);
}