package com.your.lol.domain

import com.your.lol.domain.dto.PreProcessedData
import com.your.lol.dto.riot.*
import com.your.lol.dto.statistics.ChampionDataDto
import com.your.lol.dto.statistics.StatsDto
import com.your.lol.support.WebClientFacade
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
@RequiredArgsConstructor
class LoLScoreStatsCalculator(
        private val webClientFacade: WebClientFacade) {

    fun calculateStatsBySummonerName(summonerName: String): StatsDto {
        val matchDtos = webClientFacade!!.requestRiotStatistics(summonerName)
        val preProcessedData = createPreProcessData(summonerName, matchDtos)
        return createStatistics(preProcessedData)
    }

    private fun createStatistics(preProcessedData: List<PreProcessedData>): StatsDto {
        val winRate = calculateWinRate(preProcessedData)
        val dataByChampions = groupByChampionName(preProcessedData)
        val championDataDtos = calculateStatsOfChampions(dataByChampions)
        return StatsDto(winRate, championDataDtos)
    }

    private fun calculateStatsOfChampions(dataByChampions: Map<String, List<PreProcessedData>>): List<ChampionDataDto> {
        return dataByChampions.entries.stream()
                .map { entry: Map.Entry<String, List<PreProcessedData>> -> calculateStatsOfChampion(entry) }
                .collect(Collectors.toUnmodifiableList())
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
                .mapToInt { it: PreProcessedData -> it.convertTeamWinToNumber() }
                .average()
                .asDouble
    }

    private fun calculateAverageKda(championData: List<PreProcessedData>): Double {
        val totalGameDuration = championData.stream()
                .mapToLong { (_, _, _, _, gameDuration): PreProcessedData -> gameDuration }
                .sum()
        return championData.stream()
                .mapToDouble { (_, _, kda, _, gameDuration): PreProcessedData -> gameDuration * kda }
                .sum() / totalGameDuration
    }

    private fun groupByChampionName(preProcessedData: List<PreProcessedData>): Map<String, List<PreProcessedData>> {
        return preProcessedData.stream()
                .collect(Collectors.groupingBy(PreProcessedData::championName))
    }

    private fun calculateWinRate(preProcessedData: List<PreProcessedData>): Double {
        return preProcessedData.stream()
                .mapToInt { obj: PreProcessedData -> obj.convertTeamWinToNumber() }
                .average()
                .asDouble
    }

    private fun createPreProcessData(summonerName: String, matchDtos: List<MatchDto?>): List<PreProcessedData> {
        return matchDtos.stream()
                .map { it?.info }
                .map { it?.filterAndCreateCompositeDto(summonerName) }
                .map { mapPreProcessData(it!!) }
                .collect(Collectors.toUnmodifiableList())
    }

    private fun mapPreProcessData(participantTeam: ParticipantTeamDto): PreProcessedData {
        val participant: ParticipantDto = participantTeam.participantDto
        val challenges: ChallengesDto? = participant.challenges
        return PreProcessedData(
                championName = participant.championName,
                teamId = participant.teamId,
                deathsByEnemyChamps = challenges!!.deathsByEnemyChamps,
                gameDuration = participant.gameDuration,
                kda = challenges!!.kda,
                teamWin = participantTeam.teamWin
        )
    }
}