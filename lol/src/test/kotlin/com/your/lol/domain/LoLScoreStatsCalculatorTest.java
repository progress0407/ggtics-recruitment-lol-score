package com.your.lol.domain;

import com.your.lol.dto.riot.MatchDto;
import com.your.lol.dto.statistics.ChampionDataDto;
import com.your.lol.dto.statistics.StatsDto;
import com.your.lol.support.GlobalTestUtils;
import com.your.lol.support.WebClientFacade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class LoLScoreStatsCalculatorTest extends GlobalTestUtils {

    private WebClientFacade webClientFacade = mock(WebClientFacade.class);
    private LoLScoreStatsCalculator loLScoreStatsCalculator = new LoLScoreStatsCalculator(webClientFacade);


    @Test
    void calculateStatisticsBySummonerName() {
        // given
        List<MatchDto> matchDtos = createTestData("match-dtos.json");
        given(webClientFacade.requestRiotStatistics("테스트 소환사 GG"))
                .willReturn(matchDtos);

        // when
        StatsDto statsDto = loLScoreStatsCalculator.calculateStatsBySummonerName("테스트 소환사 GG");
        ChampionDataDto jinx = findChampionByName(statsDto, "Jinx");
        ChampionDataDto riven = findChampionByName(statsDto, "Riven");

        // then
        assertAll(
                assertCloseTo(statsDto.getWinRate(), 0.666, 0.001),

                assertEquals(jinx.getMatchCount(), 1),
                assertEquals(jinx.getWinRate(), 1.0),
                assertCloseTo(jinx.getAverageKda(), 3.6, 0.01),

                assertEquals(riven.getMatchCount(), 2),
                assertEquals(riven.getWinRate(), 0.5),
                assertCloseTo(riven.getAverageKda(), 5.97, 0.01)
        );
    }
}