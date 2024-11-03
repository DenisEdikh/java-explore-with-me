package ru.practicum.ewm.service.user;

import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.param.AdminPageParam;

import java.util.List;

public interface UserService {
    UserDto create(UserDto user);

    List<UserDto> getAll(AdminPageParam param);

    void delete(Long userId);
}
