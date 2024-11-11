package ru.practicum.ewm.stats.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import ru.practicum.ewm.stats.EndpointHitDto;
import ru.practicum.ewm.stats.ViewStatDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class StatClientImpl implements StatClient {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RestClient restClient;

    public StatClientImpl(@Value("${stats.server.url}") String url) {
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public void saveHit(EndpointHitDto endpointHitDto) {
        log.debug("Отправка запроса POST на сервер статистики с hit {}", endpointHitDto);
        restClient.post()
                .uri("/hit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(endpointHitDto)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public List<ViewStatDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.debug("Запрос GET на сервер статистики для uris: {}", uris);
        try {
            return restClient.get()
                    .uri(
                            uri -> uri.path("/stats")
                                    .queryParam("start", codeTime(start.format(DATE_TIME_FORMATTER)))
                                    .queryParam("end", codeTime(end.format(DATE_TIME_FORMATTER)))
                                    .queryParam("uris", uris)
                                    .queryParam("unique", unique)
                                    .build()
                    )
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException e) {
            log.warn("Получение ответа от сервера статистики завершилось с ошибкой: {}", e.getMessage());
            return List.of();
        }
    }

    private String codeTime(String time) {
        return URLEncoder.encode(time, StandardCharsets.UTF_8);
    }
}