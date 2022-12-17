package com.your.lol.domain;

import static java.lang.System.out;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableList;

import com.your.lol.dto.riot.ChallengesDto;
import com.your.lol.dto.riot.MatchDto;
import com.your.lol.dto.riot.ParticipantDto;
import com.your.lol.dto.riot.ParticipantTeamDto;
import com.your.lol.dto.statistics.StatsDto;
import com.your.lol.support.WebClientFacade;
import java.util.List;
import java.util.Map;
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
        double winRate = preProcessedData.stream()
                .mapToInt(PreProcessedData::convertTeamWinToNumber)
                .average()
                .getAsDouble();

        Map<String, List<PreProcessedData>> dataByChampion =
                preProcessedData.stream()
                        .collect(groupingBy(PreProcessedData::getChampionName));

        StatsDto statsDto = new StatsDto(winRate, dataByChampion);
        return statsDto;
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
                .physicalDamageTaken(participant.getPhysicalDamageTaken())
                .physicalDamageDealt(participant.getPhysicalDamageDealt())
                .champExperience(participant.getChampExperience())
                .deathsByEnemyChamps(challenges.getDeathsByEnemyChamps())
                .kda(challenges.getKda())
                .teamWin(participantTeam.isTeamWin())
                .build();
    }
}
