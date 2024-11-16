package ru.practicum.ewm.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.AssessmentType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentDto {
    private Long id;
    private AssessmentType assessmentType;
}
