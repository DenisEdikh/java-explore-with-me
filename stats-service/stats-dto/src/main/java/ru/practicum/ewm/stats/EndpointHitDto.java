package ru.practicum.ewm.stats;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitDto {
    @NotBlank
    @Size(max = 200)
    private String app;
    @NotBlank
    @Size(max = 200)
    private String uri;
    @NotBlank
    @Size(max = 30)
    private String ip;
    @NotNull
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
