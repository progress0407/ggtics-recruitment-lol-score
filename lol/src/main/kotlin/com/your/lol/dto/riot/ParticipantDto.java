package com.your.lol.dto.riot;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParticipantDto {
    private String championName;
    private int teamId;
    private long physicalDamageDealt;
    private long physicalDamageTaken;
    private long champExperience;
    private String summonerId;
    private long summonerLevel;
    private String summonerName;
    private ChallengesDto challenges;

    public boolean equalsId(String summonerName) {
        return this.summonerName.equals(summonerName);
    }
}
