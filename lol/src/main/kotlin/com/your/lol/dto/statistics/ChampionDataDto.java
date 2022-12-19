package com.your.lol.dto.statistics;

import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ChampionDataDto {

    private String championName;
    private double winRate;
    private int matchCount; // 경기 횟수
    private double averageKda;
}
