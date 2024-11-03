package ru.practicum.ewm.log;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Log {
    private String makeUrl(HttpServletRequest req) {
        StringBuffer url = req.getRequestURL();
        String query = req.getQueryString();
        if (query != null) {
            url.append("?").append(query);
        }
        return url.toString();
    }

    public void startLog(HttpServletRequest req) {
        log.info("Starting method: {}; url: {}", req.getMethod(), makeUrl(req));
    }

    public void finishLog(HttpServletRequest req) {
        log.info("Finishing method {}, url: {}", req.getMethod(), makeUrl(req));
    }
}
