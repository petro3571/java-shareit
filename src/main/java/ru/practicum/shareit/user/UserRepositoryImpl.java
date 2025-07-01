package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final List<User> users = new ArrayList<>();

    private final Set<String> mailSet = new HashSet<>();

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User save(User user) {
        if (mailSet.contains(user.getEmail())) {
            throw new EmailAlreadyExistException("Пользователь с почтой " + user.getEmail() + " уже существует.");
        }

        user.setId(getId());
        users.add(user);
        mailSet.add(user.getEmail());
        return user;
    }

    @Override
    public User update(User user) {
        if (mailSet.contains(user.getEmail())) {
            throw new EmailAlreadyExistException("Пользователь с почтой " + user.getEmail() + " уже существует.");
        }

       User updateUser = getUserById(user.getId());
       if (user.getName() != null) {
           updateUser.setName(user.getName());
       }
       if (user.getEmail() != null) {
           updateUser.setEmail(user.getEmail());
       }
        mailSet.add(updateUser.getEmail());
       return users.set(updateUser.getId().intValue() - 1, updateUser);
    }

    @Override
    public User getUserById(Long userId) {
        return users.get((int) (userId - 1));
    }

    @Override
    public User deleteUser(User user) {
        return users.remove(user.getId().intValue() - 1);
    }

    private long getId() {
        long lastId = users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}