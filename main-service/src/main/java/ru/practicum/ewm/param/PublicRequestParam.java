package ru.practicum.ewm.param;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.model.EventSortType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class PublicRequestParam extends AbstractRequestParam {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private String rangeStart;
    private String rangeEnd;
    private Boolean onlyAvailable = false;
    private Boolean pinned;
    private EventSortType sort;

    public LocalDateTime getRangeStart() {
        return rangeStart != null
                ? LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : null;
    }

    public LocalDateTime getRangeEnd() {
        return rangeEnd != null
                ? LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : null;
    }
}
