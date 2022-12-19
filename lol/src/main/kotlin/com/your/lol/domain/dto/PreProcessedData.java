package com.your.lol.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PreProcessedData {
    private final String championName;
    private final int teamId;
    private final double kda;
    private final long deathsByEnemyChamps;
    private final long gameDuration;
    private final boolean teamWin;

    public int convertTeamWinToNumber() {
        return teamWin ? 1 : 0;
    }
}
