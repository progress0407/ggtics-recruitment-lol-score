package com.your.lol.acceptance;

import com.your.lol.dto.riot.MatchDto;
import com.your.lol.dto.statistics.ChampionDataDto;
import com.your.lol.dto.statistics.StatsDto;
import io.restassured.response.ValidatableResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

public class SummonerStatsAcceptanceTest extends AcceptanceTest {

    @Test
    void findStatisticsBySummonerName() {
        // given
        List<MatchDto> matchDtos = createTestData("match-dtos.json");
        given(webClientFacade.requestRiotStatistics("테스트 소환사 GG"))
                .willReturn(matchDtos);

        // when
        ValidatableResponse response = get("/api/summoner/" + "테스트 소환사 GG");
        StatsDto statsDto = response.extract().as(StatsDto.class);
        ChampionDataDto jinx = findChampionByName(statsDto, "Jinx");
        ChampionDataDto riven = findChampionByName(statsDto, "Riven");

        // then
        assertAll(
                assertStatusCode(response, HttpStatus.OK),
                assertEquals(jinx.getMatchCount(), 1),
                assertEquals(jinx.getWinRate(), 1.0),
                assertCloseTo(jinx.getAverageKda(), 3.6, 0.01),

                assertEquals(riven.getMatchCount(), 2),
                assertEquals(riven.getWinRate(), 0.5),
                assertCloseTo(riven.getAverageKda(), 5.97, 0.01)
        );
    }

    @NotNull
    private static Executable assertStatusCode(ValidatableResponse response, HttpStatus httpStatus) {
        return () -> response.statusCode(httpStatus.value());
    }
}
