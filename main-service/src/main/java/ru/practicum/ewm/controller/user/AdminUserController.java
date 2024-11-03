package ru.practicum.ewm.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.param.AdminPageParam;
import ru.practicum.ewm.service.user.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@Valid @RequestBody UserDto userDto, HttpServletRequest req) {
        startLog(req);
        final UserDto storedUser = userService.create(userDto);
        finishLog(req);
        return storedUser;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll(@Valid AdminPageParam param, HttpServletRequest req) {
        startLog(req);
        final List<UserDto> userDto = userService.getAll(param);
        finishLog(req);
        return userDto;
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Long userId, HttpServletRequest req) {
        startLog(req);
        userService.delete(userId);
        finishLog(req);
    }


    private void startLog(HttpServletRequest req) {
        String method = req.getMethod();
        StringBuffer url = req.getRequestURL();
        String query = req.getQueryString();
        if (query != null) {
            url.append("?").append(query);
        }
        log.info("Starting method: {}; url: {}", method, url.toString());
    }

    private void finishLog(HttpServletRequest req) {
        String method = req.getMethod();
        StringBuffer url = req.getRequestURL();
        String query = req.getQueryString();
        if (query != null) {
            url.append("?").append(query);
        }
        log.info("Finishing method {}, url: {}", method, url.toString());
    }
}
