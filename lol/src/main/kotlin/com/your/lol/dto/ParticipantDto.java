package com.your.lol.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParticipantDto {
    private long physicalDamageDealt;
    private long physicalDamageTaken;
    private long champExperience;
    private double kda;
    private String summonerId;
    private long summonerLevel;
    private String summonerName;
    private ChallengesDto challenges;
}
