package com.your.lol.presentation.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InfoResponse {
    private List<ParticipantResponse> participants;
}
