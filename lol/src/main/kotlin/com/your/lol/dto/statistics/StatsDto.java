package com.your.lol.dto.statistics;

import com.your.lol.domain.PreProcessedData;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StatsDto {

    private final double winRate;
    private final List<ChampionDataDto> championDataDtos;
}
