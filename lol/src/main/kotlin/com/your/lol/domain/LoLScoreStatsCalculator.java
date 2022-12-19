package com.your.lol.domain;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableList;

import com.your.lol.dto.riot.ChallengesDto;
import com.your.lol.dto.riot.MatchDto;
import com.your.lol.dto.riot.ParticipantDto;
import com.your.lol.dto.riot.ParticipantTeamDto;
import com.your.lol.dto.statistics.ChampionDataDto;
import com.your.lol.dto.statistics.StatsDto;
import com.your.lol.support.WebClientFacade;
import java.util.ArrayList;
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
        Map<String, List<PreProcessedData>> dataByChampions = calculateDataByChampions(preProcessedData);
        List<ChampionDataDto> championDataDtos = championDataDtos(dataByChampions);
        return new StatsDto(winRate, championDataDtos);
    }

    private List<ChampionDataDto> championDataDtos(Map<String, List<PreProcessedData>> dataByChampions) {
        return dataByChampions.entrySet().stream()
                .map(this::calculateChampionDataDto)
                .collect(toUnmodifiableList());
    }

    @NotNull
    private ChampionDataDto calculateChampionDataDto(Entry<String, List<PreProcessedData>> entry) {
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
        return championData.stream()
                .mapToDouble(it -> it.getGameDuration() * it.getKda())
                .average()
                .getAsDouble();
    }

    private Map<String, List<PreProcessedData>> calculateDataByChampions(List<PreProcessedData> preProcessedData) {
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
