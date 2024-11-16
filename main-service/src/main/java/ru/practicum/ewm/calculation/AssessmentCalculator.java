package ru.practicum.ewm.calculation;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.model.Assessment;
import ru.practicum.ewm.model.AssessmentType;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AssessmentCalculator {
    private final static int scoreCoefficient = 5;

    private double calculateRatingEvent(List<Assessment> assessments) {
        double likes = 0.0;
        double dislikes = 0.0;

        if (assessments == null || assessments.isEmpty()) {
            return 0.0;
        }

        for (Assessment as : assessments) {
            if (AssessmentType.LIKE == as.getAssessmentType()) {
                likes++;
            }
            if (AssessmentType.DISLIKE == as.getAssessmentType()) {
                dislikes++;
            }
        }

        if (likes == 0.0) {
            return 0.0;
        }
        return (double) Math.round(scoreCoefficient * likes / (likes + dislikes) * 10) / 10;
    }

    public void calculateRatingEvents(List<Event> events, List<Assessment> assessments) {
        Map<Long, List<Assessment>> mapAssessment = assessments.stream()
                .collect(Collectors.groupingBy(a -> a.getEvent().getId()));
        events.forEach(e -> e.setRating(calculateRatingEvent(mapAssessment.get(e.getId()))));
    }

    public void calculateRatingUser(List<User> users, List<Event> events, List<Assessment> assessments) {
        calculateRatingEvents(events, assessments);
        Map<Long, List<Event>> mapEvents = events.stream()
                .collect(Collectors.groupingBy(e -> e.getInitiator().getId()));

        users.stream()
                .filter(u -> mapEvents.containsKey(u.getId()))
                .forEach(u -> u.setRating(
                        (double) Math.round(mapEvents.get(u.getId()).stream()
                                                    .mapToDouble(Event::getRating)
                                                    .sum() / mapEvents.get(u.getId()).size() * 10) / 10)
                );
    }
}
