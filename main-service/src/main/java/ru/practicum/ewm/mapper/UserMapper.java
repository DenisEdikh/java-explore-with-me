package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.model.User;

import java.util.List;

@Component
public class UserMapper {
    public User toUser(NewUserRequest dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }

    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRating());
    }

    public UserShortDto toUserShortDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserShortDto(user.getId(), user.getName());
    }

    public List<UserDto> toUserDto(List<User> users) {
        if (users == null) {
            return null;
        }

        return users.stream().map(this::toUserDto).toList();
    }
}
