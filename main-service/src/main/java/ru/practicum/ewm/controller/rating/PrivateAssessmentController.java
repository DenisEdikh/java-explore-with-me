package ru.practicum.ewm.controller.rating;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.assessment.AssessmentDto;
import ru.practicum.ewm.log.Log;
import ru.practicum.ewm.model.AssessmentType;
import ru.practicum.ewm.service.rating.AssessmentService;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/ratings")
@RequiredArgsConstructor
public class PrivateAssessmentController {
    private final Log logger;
    private final AssessmentService assessmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssessmentDto create(@PathVariable(value = "userId") Long assessorId,
                                @RequestParam(value = "eventId") Long eventId,
                                @RequestParam(value = "assessmentType") AssessmentType assessmentType,
                                HttpServletRequest req) {
        logger.startLog(req);
        final AssessmentDto dto = assessmentService.create(assessorId, eventId, assessmentType);
        logger.finishLog(req);
        return dto;
    }

    @PatchMapping(path = "/{assessmentId}")
    @ResponseStatus(HttpStatus.OK)
    public AssessmentDto update(@PathVariable(value = "userId") Long assessorId,
                                @PathVariable(value = "assessmentId") Long assessmentId,
                                @RequestParam(value = "assessmentType") AssessmentType assessmentType,
                                HttpServletRequest req) {
        logger.startLog(req);
        final AssessmentDto dto = assessmentService.update(assessorId, assessmentId, assessmentType);
        logger.finishLog(req);
        return dto;
    }

    @DeleteMapping(path = "/{assessmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "userId") Long assessorId,
                       @PathVariable(value = "assessmentId") Long assessmentId,
                       HttpServletRequest req) {
        logger.startLog(req);
        assessmentService.delete(assessorId, assessmentId);
        logger.finishLog(req);
    }
}
