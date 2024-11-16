package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.assessment.AssessmentDto;
import ru.practicum.ewm.model.Assessment;
import ru.practicum.ewm.model.AssessmentType;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;

@Component
public class AssessmentMapper {
    public Assessment toAssessment(Event event, User assessor, AssessmentType type) {
        Assessment assessment = new Assessment();

        assessment.setAssessmentType(type);
        assessment.setAssessor(assessor);
        assessment.setEvent(event);
        return assessment;
    }

    public AssessmentDto toAssessmentDto(Assessment assessment) {
        AssessmentDto assessmentDto = new AssessmentDto();

        assessmentDto.setId(assessment.getId());
        assessmentDto.setAssessmentType(assessment.getAssessmentType());
        return assessmentDto;
    }
}
