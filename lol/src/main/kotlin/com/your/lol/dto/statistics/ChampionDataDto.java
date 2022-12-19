package com.your.lol.dto.statistics;

import lombok.Builder;
import lombok.Data;

@Data
public class ChampionDataDto {

    private final String championName;
    private final double winRate;
    private final int matchCount; // 경기 횟수
    private final double averageKda;
}
