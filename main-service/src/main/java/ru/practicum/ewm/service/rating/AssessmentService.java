package ru.practicum.ewm.service.rating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.assessment.AssessmentDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.AssessmentMapper;
import ru.practicum.ewm.model.Assessment;
import ru.practicum.ewm.model.AssessmentType;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.RequestState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.AssessmentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AssessmentService {
    private final AssessmentMapper assessmentMapper;
    private final AssessmentRepository assessmentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    public AssessmentDto create(Long assessorId, Long eventId, AssessmentType type) {
        final User assessor = userRepository.findById(assessorId)
                .orElseThrow(() -> new NotFoundException(User.class, assessorId));
        final Event storedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));
        assessmentRepository.findByAssessorIdAndEventId(assessorId, eventId).ifPresent(a -> {
            throw new ConflictException(Assessment.class, assessorId, eventId);
        });
        checkingAddingLike(storedEvent, assessor);
        return assessmentMapper.toAssessmentDto(assessmentRepository.save(assessmentMapper.toAssessment(
                storedEvent,
                assessor,
                type))
        );
    }

    private void checkingAddingLike(Event event, User assessor) {
        final Request request = requestRepository.findByRequesterIdAndEventId(assessor.getId(), event.getId())
                .orElseThrow(() -> new NotFoundException(Request.class, event.getId()));
        if (!(RequestState.CONFIRMED == request.getStatus())) {
            throw new ConflictException(Request.class, assessor.getId(), event.getId());
        }
    }

    public AssessmentDto update(Long assessorId, Long assessmentId, AssessmentType type) {
        final User assessor = userRepository.findById(assessorId)
                .orElseThrow(() -> new NotFoundException(User.class, assessorId));
        final Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new NotFoundException(Assessment.class, assessmentId));
        assessment.setAssessmentType(type);
        return assessmentMapper.toAssessmentDto(assessmentRepository.save(assessment));
    }

    public void delete(Long assessorId, Long assessmentId) {
        userRepository.findById(assessorId)
                .orElseThrow(() -> new NotFoundException(User.class, assessorId));
        if (!assessmentRepository.existsById(assessmentId)) {
            throw new NotFoundException(Assessment.class, assessmentId);
        }
        assessmentRepository.deleteById(assessmentId);
    }
}
