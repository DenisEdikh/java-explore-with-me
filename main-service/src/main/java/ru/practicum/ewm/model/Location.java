package ru.practicum.ewm.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Embeddable
@NoArgsConstructor
@Setter
@Getter
public class Location {
    private float lat;
    private float lon;
}
