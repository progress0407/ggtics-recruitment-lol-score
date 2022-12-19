package com.your.lol.dto.statistics;

import java.util.List;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatsDto {

    private double winRate;
    private List<ChampionDataDto> championDataDtos;
}
