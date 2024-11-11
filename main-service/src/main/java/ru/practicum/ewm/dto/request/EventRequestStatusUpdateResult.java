package ru.practicum.ewm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
    List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

    public void addConfirmedRequests(ParticipationRequestDto participationRequestDto) {
        confirmedRequests.add(participationRequestDto);
    }

    public void addRejectedRequests(ParticipationRequestDto participationRequestDto) {
        rejectedRequests.add(participationRequestDto);
    }
}
