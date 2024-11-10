package ru.practicum.ewm.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.log.Log;
import ru.practicum.ewm.param.AdminRequestParam;
import ru.practicum.ewm.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final Log logger;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@RequestBody @Valid NewUserRequest newUserRequest, HttpServletRequest req) {
        logger.startLog(req);
        final UserDto storedUser = userService.create(newUserRequest);
        logger.finishLog(req);
        return storedUser;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll(@Valid AdminRequestParam param, HttpServletRequest req) {
        logger.startLog(req);
        final List<UserDto> userDto = userService.getAll(param);
        logger.finishLog(req);
        return userDto;
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Long userId, HttpServletRequest req) {
        logger.startLog(req);
        userService.delete(userId);
        logger.finishLog(req);
    }
}
