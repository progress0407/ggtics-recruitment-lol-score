package com.your.lol.dto.riot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParticipantTeamDto {
    private final ParticipantDto participantDto;
    private final boolean teamWin;
}
