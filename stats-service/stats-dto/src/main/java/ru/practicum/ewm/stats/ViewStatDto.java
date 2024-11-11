package ru.practicum.ewm.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatDto {
    private String app;
    private String uri;
    private Long hits;
}
