package com.your.lol.dto.statistics;

import com.your.lol.domain.PreProcessedData;
import java.util.List;
import java.util.Map;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatsDto {

    private double winRate;
    private List<ChampionDataDto> championDataDtos;
}
