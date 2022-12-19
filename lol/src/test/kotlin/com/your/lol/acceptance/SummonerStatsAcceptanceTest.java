package com.your.lol.acceptance;

import com.google.gson.reflect.TypeToken;
import com.your.lol.dto.riot.MatchDto;
import com.your.lol.support.JsonFileConverter;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

public class SummonerStatsAcceptanceTest extends AcceptanceTest {

    @Test
    void findStatisticsBySummonerName() {
        // given
        List<MatchDto> matchDtos = createTestData();
        given(webClientFacade.requestRiotStatistics("테스트 소환사"))
                .willReturn(matchDtos);

        // when
        ValidatableResponse response = get("/api/summoner/" + "테스트 소환사");

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("winRate", equalTo(0.5));
    }

    private List<MatchDto> createTestData() {
        return JsonFileConverter.fromJsonFile(
                "match-dtos.json",
                new TypeToken<>() {
                });
    }
}
