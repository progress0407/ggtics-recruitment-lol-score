package com.your.lol.dto.statistics;

import static java.util.stream.Collectors.groupingBy;

import com.your.lol.domain.PreProcessedData;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 최근 20경기 승률
 * 시간 가중치 + 캐릭터별 평균 승률
 */
@Getter
@RequiredArgsConstructor
public class StatisticsDto {

    private final double winRate;
    private final Map<String, List<PreProcessedData>> dataByChampion;
}
