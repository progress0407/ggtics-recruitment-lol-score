package com.your.lol.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.google.gson.reflect.TypeToken;
import com.your.lol.dto.riot.MatchDto;
import com.your.lol.dto.statistics.ChampionDataDto;
import com.your.lol.dto.statistics.StatsDto;
import com.your.lol.support.JsonFileConverter;
import com.your.lol.support.WebClientFacade;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

class LoLScoreStatsCalculatorTest {

    private WebClientFacade webClientFacade = mock(WebClientFacade.class);
    private LoLScoreStatsCalculator loLScoreStatsCalculator = new LoLScoreStatsCalculator(webClientFacade);


    @Test
    void calculateStatisticsBySummonerName() {
        // given
        List<MatchDto> matchDtos = createTestData();
        given(webClientFacade.requestRiotStatistics("테스트 소환사 GG"))
                .willReturn(matchDtos);

        // when
        StatsDto result = loLScoreStatsCalculator.calculateStatsBySummonerName("테스트 소환사 GG");
        ChampionDataDto Jinx = findChampionByName(result, "Jinx");
        ChampionDataDto Riven = findChampionByName(result, "Riven");

        // then
        assertAll(
                () -> assertThat(result.getWinRate()).isCloseTo(0.66, offset(0.01)),
                () -> assertThat(Jinx.getMatchCount()).isEqualTo(1),
                () -> assertThat(Jinx.getWinRate()).isEqualTo(1.0),
                () -> assertThat(Jinx.getAverageKda()).isEqualTo(1.0),
                () -> assertThat(Riven.getMatchCount()).isEqualTo(2),
                () -> assertThat(Riven.getWinRate()).isEqualTo(0.5),
                () -> assertThat(Riven.getAverageKda()).isEqualTo(1.0)
        );
    }

    @NotNull
    private static ChampionDataDto findChampionByName(StatsDto stats, String championName) {
        return stats.getChampionDataDtos().stream()
                .filter(it -> it.getChampionName().equals(championName))
                .findAny()
                .get();
    }

    private List<MatchDto> createTestData() {
        return JsonFileConverter.fromJsonFile(
                "match-dtos.json",
                new TypeToken<>() {
                });
    }
}