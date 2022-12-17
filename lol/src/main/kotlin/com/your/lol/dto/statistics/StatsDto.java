package com.your.lol.dto.statistics;

import static java.util.stream.Collectors.groupingBy;

import com.your.lol.domain.PreProcessedData;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StatsDto {

    private final double winRate;
    private final Map<String, List<PreProcessedData>> dataByChampion;
}
