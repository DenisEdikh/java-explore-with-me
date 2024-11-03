package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.practicum.ewm.stats.EndpointHitDto;
import ru.practicum.ewm.stats.client.StatClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
public class MainApp {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(MainApp.class, args);
//        StatClient statClient = applicationContext.getBean(StatClient.class);
//
//        statClient.saveHit(new EndpointHitDto(
//                "ewm-main-service",
//                "/events/1",
//                "192.163.0.1",
//                LocalDateTime.parse("2022-09-06 11:00:23", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//        );
//
//        System.out.println(statClient.getViewStats(
//                "2022-09-06 11:00:23",
//                "2022-09-06 11:00:23",
//                List.of("/events/1"),
//                false));
    }
}