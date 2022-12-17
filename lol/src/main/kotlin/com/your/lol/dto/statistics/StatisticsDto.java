package com.your.lol.dto.statistics;

import static java.util.stream.Collectors.groupingBy;

import com.your.lol.domain.PreProcessedData;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * 최근 20경기 승률
 * 시간 가중치 + 캐릭터별 평균 승률
 */
@Getter
public class StatisticsDto {

    private final double winRate;
    private final Map<String, List<PreProcessedData>> dataByChampion;

    public StatisticsDto(List<PreProcessedData> preProcessedData) {
        winRate = preProcessedData.stream()
                .mapToInt(PreProcessedData::convertTeamWinToNumber)
                .average()
                .getAsDouble();

        dataByChampion =
                preProcessedData.stream()
                        .collect(groupingBy(PreProcessedData::getChampionName));
    }
}
