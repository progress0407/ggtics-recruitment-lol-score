package com.your.lol.acceptance

import com.your.lol.dto.statistics.ChampionDataDto
import com.your.lol.dto.statistics.StatsDto
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given

class SummonerStatsAcceptanceTest : AcceptanceTest() {

    @Test
    fun `소환사 이름을 검색하여 전적 통계를을 산출한다`() {
        // given
        val matchDtos = createTestData("match-dtos.json")
        given(webClientFacade.requestRiotStatistics("테스트 소환사 GG"))
                .willReturn(matchDtos)

        // when
        val response = get("/api/summoner/${"테스트 소환사 GG"}")
        val statsDto = response!!.extract().`as`(StatsDto::class.java)
        val jinx: ChampionDataDto = findChampionByName(statsDto, "Jinx")
        val riven: ChampionDataDto = findChampionByName(statsDto, "Riven")

        // then
        Assertions.assertAll(
                assertCloseTo<Double>(statsDto.winRate, 0.666, 0.001),
                assertEquals(jinx.matchCount, 1),
                assertEquals(jinx.winRate, 1.0),
                assertCloseTo<Double>(jinx.averageKda, 3.6, 0.01),
                assertEquals(riven.matchCount, 2),
                assertEquals(riven.winRate, 0.5),
                assertCloseTo<Double>(riven.averageKda, 5.97, 0.01)
        )
    }
}
