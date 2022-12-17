package com.your.lol.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.google.gson.reflect.TypeToken;
import com.your.lol.dto.riot.MatchDto;
import com.your.lol.dto.statistics.StatisticsDto;
import com.your.lol.support.JsonFileConverter;
import com.your.lol.support.WebClientFacade;
import java.util.List;
import org.junit.jupiter.api.Test;

class SummonerStatisticsTest {

    private WebClientFacade webClientFacade = mock(WebClientFacade.class);
    private SummonerStatistics summonerStatistics = new SummonerStatistics(webClientFacade);


    @Test
    void calculateStatisticsBySummonerName() {
        // given
        List<MatchDto> matchDtos = createTestData();
        given(webClientFacade.requestRiotStatistics("테스트 소환사"))
                .willReturn(matchDtos);

        // when
        StatisticsDto result = summonerStatistics.calculateStatisticsBySummonerName("테스트 소환사");

        // then
        assertThat(result.getWinRate()).isEqualTo(0.5);
    }

    private List<MatchDto> createTestData() {
        return JsonFileConverter.fromJsonFile("match-dto.json",
                new TypeToken<>() {
                });
    }
}