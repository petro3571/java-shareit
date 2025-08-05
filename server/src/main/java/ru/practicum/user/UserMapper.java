package ru.practicum.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    public static User mapToUser(UserDto request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static User updateUserFields(User user, UserDto request) {
        if (!(request.getName() == null || request.getName().isBlank())) {
            user.setName(request.getName());
        }

        if (!(request.getEmail() == null || request.getEmail().isBlank())) {
            user.setEmail(request.getEmail());
        }
        return user;
    }
}