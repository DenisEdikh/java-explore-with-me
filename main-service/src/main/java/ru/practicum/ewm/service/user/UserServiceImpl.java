package ru.practicum.ewm.service.user;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.QUser;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.param.AdminRequestParam;
import ru.practicum.ewm.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(newUserRequest)));
    }

    @Override
    public List<UserDto> getAll(AdminRequestParam param) {
        Pageable page = PageRequest.of(param.getPage(), param.getSize());
        BooleanBuilder byParam = new BooleanBuilder();
        Optional.ofNullable(param.getIds()).ifPresent(i -> byParam.and(QUser.user.id.in(i)));
        return userMapper.toUserDto(userRepository.findAll(byParam, page).toList());
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        checkExistenceUser(userId);
        userRepository.deleteById(userId);
    }

    private void checkExistenceUser(Long userId) {
        log.debug("Start checking contains user with userId {}", userId);
        if (!userRepository.existsById(userId)) {
            log.warn("User with userId {} does not exist", userId);
            throw new NotFoundException(User.class, userId);
        }
    }
}
