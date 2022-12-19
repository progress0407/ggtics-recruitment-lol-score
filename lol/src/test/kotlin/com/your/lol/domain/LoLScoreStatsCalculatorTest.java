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
import org.junit.jupiter.api.function.Executable;

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
        ChampionDataDto jinx = findChampionByName(result, "Jinx");
        ChampionDataDto riven = findChampionByName(result, "Riven");

        // then
        assertAll(
                assertCloseTo(result.getWinRate(), 0.666, 0.001),

                assertEquals(jinx.getMatchCount(), 1),
                assertEquals(jinx.getWinRate(), 1.0),
                assertCloseTo(jinx.getAverageKda(), 3.6, 0.01),

                assertEquals(riven.getMatchCount(), 2),
                assertEquals(riven.getWinRate(), 0.5),
                assertCloseTo(riven.getAverageKda(), 5.97, 0.01)
        );
    }

    @NotNull
    private static <T> Executable assertEquals(T actual, T expected) {
        return () -> assertThat(actual).isEqualTo(expected);
    }

    @NotNull
    private static Executable assertCloseTo(double actual, double expected, double offset) {
        return () -> assertThat(actual).isCloseTo(expected, offset(offset));
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