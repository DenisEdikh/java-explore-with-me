package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Assessment;

import java.util.List;
import java.util.Optional;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByAssessorIdIn(List<Long> ids);

    List<Assessment> findByEventIdIn(List<Long> ids);

    Optional<Assessment> findByAssessorIdAndEventId(Long assessorId, Long eventId);
}
