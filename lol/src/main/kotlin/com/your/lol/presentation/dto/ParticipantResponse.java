package com.your.lol.presentation.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParticipantResponse {
    private long physicalDamageDealt;
    private long physicalDamageTaken;
    private long champExperience;
    private double kda;
    private String summonerId;
    private long summonerLevel;
    private String summonerName;
    private ChallengeResponse challenges;
}
