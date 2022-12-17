package com.your.lol.dto.riot;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InfoDto {
    private List<ParticipantDto> participants;
    private List<TeamDto> teams;

    public ParticipantTeamDto filterAndCreateCompositeDto(String summonerName) {
        ParticipantDto findParticipant = participants.stream()
                .filter(participant -> participant.equalsId(summonerName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("챔피언을 찾을 수 없습니다."));
        TeamDto findTeam = teams.stream()
                .filter(team -> team.equalsId(findParticipant.getTeamId()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));
        return new ParticipantTeamDto(findParticipant, findTeam.isWin());
    }
}
