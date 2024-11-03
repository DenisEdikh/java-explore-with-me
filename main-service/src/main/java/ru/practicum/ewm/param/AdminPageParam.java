package ru.practicum.ewm.param;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class AdminPageParam {
    private List<Long> ids;
    @PositiveOrZero
    private int from = 0;
    @Positive
    private int size = 10;

    public int getPage() {
        return from / size;
    }
}
