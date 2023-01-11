package com.your.lol.domain

import com.your.lol.domain.dto.PreProcessedData
import com.your.lol.dto.riot.StatDto
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import java.util.stream.Collectors.groupingBy
import java.util.stream.Collectors.toUnmodifiableList

@Component
@RequiredArgsConstructor
class LoLScoreStatsCalculator {

    fun calculateStats(preProcessedData: List<PreProcessedData>): StatDto {
        val winRate = calculateWinRate(preProcessedData)
        val dataByChampions = groupByChampionName(preProcessedData)
        val championDataDtos = calculateStatsOfChampions(dataByChampions)
        val stat = Stat(winRate, championDataDtos)
        return StatDto(stat)
    }

    private fun calculateWinRate(preProcessedData: List<PreProcessedData>): Double {
        return preProcessedData.stream()
                .mapToInt { preProcessedData -> preProcessedData.convertTeamWinToNumber() }
                .average()
                .asDouble
    }

    private fun groupByChampionName(preProcessedData: List<PreProcessedData>): Map<String, List<PreProcessedData>> {
        return preProcessedData.stream()
                .collect(groupingBy(PreProcessedData::championName))
    }

    private fun calculateStatsOfChampions(dataByChampions: Map<String, List<PreProcessedData>>): List<ChampionDataDto> {
        return dataByChampions.entries.stream()
                .map { entry -> calculateStatsOfChampion(entry) }
                .collect(toUnmodifiableList())
    }

    private fun calculateStatsOfChampion(entry: Map.Entry<String, List<PreProcessedData>>): ChampionDataDto {
        val championName = entry.key
        val championData = entry.value
        return ChampionDataDto(
                championName,
                calculateWinRateByChampion(championData),
                championData.size,
                calculateAverageKda(championData)
        )
    }

    private fun calculateWinRateByChampion(championData: List<PreProcessedData>): Double {
        return championData.stream()
                .mapToInt(PreProcessedData::convertTeamWinToNumber)
                .average()
                .asDouble
    }

    private fun calculateAverageKda(championData: List<PreProcessedData>): Double {
        val totalGameDuration = championData.stream()
                .mapToLong { championData -> championData.gameDuration }
                .sum()
        return championData.stream()
                .mapToDouble { championData -> championData.gameDuration * championData.kda }
                .sum() / totalGameDuration
    }
}