package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final List<User> users = new ArrayList<>();

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User save(User user) {
        user.setId(getId());
        users.add(user);
        return user;
    }

    @Override
    public User update(User user) {
       User updateUser = getUserById(user.getId());
       if (user.getName() != null) {
           updateUser.setName(user.getName());
       }
       if (user.getEmail() != null) {
           updateUser.setEmail(user.getEmail());
       }
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