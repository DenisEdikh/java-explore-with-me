package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.EndpointHitDto;
import ru.practicum.ewm.stats.mapper.EndpointHitMapper;
import ru.practicum.ewm.stats.repository.StatRepository;
import ru.practicum.ewm.stats.ViewStatDto;
import ru.practicum.ewm.stats.exception.ValidationException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StatServiceImpl implements StatService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatRepository statRepository;

    @Override
    @Transactional
    public void saveHit(EndpointHitDto endpointHitDto) {
        log.debug("Started saving endpoint hit {}", endpointHitDto);
        statRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
        log.debug("Finished saving endpoint hit {}", endpointHitDto);
    }

    @Override
    public List<ViewStatDto> getViewDto(String start, String end, List<String> uris, boolean unique) {
        final LocalDateTime startTime = decodeTime(start);
        final LocalDateTime endTime = decodeTime(end);
        log.debug("Started check time in method getViewDto with parameters: start {}, end {}, uris {}",
                start,
                end,
                uris
        );
        checkTime(startTime, endTime);
        log.debug("Finished check time in method getViewDto with parameters: start {}, end {}, uris {}",
                start,
                end,
                uris
        );

        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return statRepository.findAllEndpointWithUniqueIpAndWithOutUris(startTime, endTime);
            } else {
                return statRepository.findAllEndpointWithOutUniqueIpAndWithOutUris(startTime, endTime);
            }
        } else {
            if (unique) {
                return statRepository.findAllEndpointWithUniqueIpAndWithUris(startTime, endTime, uris);
            } else {
                return statRepository.findAllEndpointWithOutUniqueIpAndWitUris(startTime, endTime, uris);
            }
        }
    }

    private LocalDateTime decodeTime(String time) {
        return LocalDateTime.parse(URLDecoder.decode(time, StandardCharsets.UTF_8), DATE_TIME_FORMATTER);
    }

    private void checkTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new ValidationException("Время начала не может быть позже времени окончания");
        }
    }
}