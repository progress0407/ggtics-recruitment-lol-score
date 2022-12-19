package com.your.lol.domain;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableList;

import com.your.lol.domain.dto.PreProcessedData;
import com.your.lol.dto.riot.ChallengesDto;
import com.your.lol.dto.riot.MatchDto;
import com.your.lol.dto.riot.ParticipantDto;
import com.your.lol.dto.riot.ParticipantTeamDto;
import com.your.lol.dto.statistics.ChampionDataDto;
import com.your.lol.dto.statistics.StatsDto;
import com.your.lol.support.WebClientFacade;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoLScoreStatsCalculator {

    private final WebClientFacade webClientFacade;

    private List<MatchDto> matchDtos;

    public StatsDto calculateStatsBySummonerName(String summonerName) {
        if (matchDtos == null) {
            matchDtos = webClientFacade.requestRiotStatistics(summonerName);
        }
        List<PreProcessedData> preProcessedData = createPreProcessData(summonerName, matchDtos);

        return createStatistics(preProcessedData);
    }

    @NotNull
    private StatsDto createStatistics(List<PreProcessedData> preProcessedData) {
        double winRate = calculateWinRate(preProcessedData);
        Map<String, List<PreProcessedData>> dataByChampions = groupByChampionName(preProcessedData);
        List<ChampionDataDto> championDataDtos = calculateStatsOfChampions(dataByChampions);
        return new StatsDto(winRate, championDataDtos);
    }

    private List<ChampionDataDto> calculateStatsOfChampions(Map<String, List<PreProcessedData>> dataByChampions) {
        return dataByChampions.entrySet().stream()
                .map(this::calculateStatsOfChampion)
                .collect(toUnmodifiableList());
    }

    @NotNull
    private ChampionDataDto calculateStatsOfChampion(Entry<String, List<PreProcessedData>> entry) {
        String championName = entry.getKey();
        List<PreProcessedData> championData = entry.getValue();
        return new ChampionDataDto(
                championName,
                calculateWinRateByChampion(championData),
                championData.size(),
                calculateAverageKda(championData)
        );
    }

    private double calculateWinRateByChampion(List<PreProcessedData> championData) {
        return championData.stream()
                .mapToInt(it -> it.convertTeamWinToNumber())
                .average()
                .getAsDouble();
    }

    private double calculateAverageKda(List<PreProcessedData> championData) {
        long totalGameDuration = championData.stream()
                .mapToLong(it -> it.getGameDuration())
                .sum();

        return championData.stream()
                .mapToDouble(it -> it.getGameDuration() * it.getKda())
                .sum() / totalGameDuration;
    }

    private Map<String, List<PreProcessedData>> groupByChampionName(List<PreProcessedData> preProcessedData) {
        return preProcessedData.stream()
                .collect(groupingBy(PreProcessedData::getChampionName));
    }

    private double calculateWinRate(List<PreProcessedData> preProcessedData) {
        return preProcessedData.stream()
                .mapToInt(PreProcessedData::convertTeamWinToNumber)
                .average()
                .getAsDouble();
    }

    private List<PreProcessedData> createPreProcessData(String summonerName, List<MatchDto> matchDtos) {
        return matchDtos.stream()
                .map(MatchDto::getInfo)
                .map(info -> info.filterAndCreateCompositeDto(summonerName))
                .map(participant -> mapPreProcessData(participant))
                .collect(toUnmodifiableList());
    }

    private PreProcessedData mapPreProcessData(ParticipantTeamDto participantTeam) {
        ParticipantDto participant = participantTeam.getParticipantDto();
        ChallengesDto challenges = participant.getChallenges();
        return PreProcessedData.builder()
                .championName(participant.getChampionName())
                .deathsByEnemyChamps(challenges.getDeathsByEnemyChamps())
                .gameDuration(participant.getGameDuration())
                .kda(challenges.getKda())
                .teamWin(participantTeam.isTeamWin())
                .build();
    }
}
