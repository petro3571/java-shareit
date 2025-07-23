package ru.practicum.user.dto;

import ru.practicum.user.model.User;

public class UserMapper {
    public static User mapToUser(NewUserDto request) {
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

//    public static User updateUserFields(User user, UpdateUserRequest request) {
//        if (request.hasName()) {
//            user.setName(request.getName());
//        }
//
//        if (request.hasEmail()) {
//            user.setEmail(request.getEmail());
//        }
//        return user;
//    }
}
