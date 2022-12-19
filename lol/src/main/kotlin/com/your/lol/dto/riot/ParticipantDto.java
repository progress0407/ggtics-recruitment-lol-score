package com.your.lol.dto.riot;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParticipantDto {
    private String summonerName;
    private int teamId;
    private String championName;
    private long gameDuration;
    private ChallengesDto challenges;

    public boolean equalsId(String summonerName) {
        return this.summonerName.equals(summonerName);
    }
}
