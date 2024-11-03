package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public User toUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        return user;
    }

    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public List<UserDto> toUserDto(List<User> users) {
        if (users == null) {
            return null;
        }

        return users.stream().map(this::toUserDto).toList();
    }
}
