package com.your.lol.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PreProcessedData {
    private final String championName;
    private final int teamId;
    private final long physicalDamageDealt;
    private final long physicalDamageTaken;
    private final long champExperience;
    private final double kda;
    private final long deathsByEnemyChamps;
    private final boolean teamWin;

    public int convertTeamWinToNumber() {
        return teamWin ? 1 : 0;
    }
}
