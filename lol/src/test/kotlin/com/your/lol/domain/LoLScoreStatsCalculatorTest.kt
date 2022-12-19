package com.your.lol.domain

import com.your.lol.dto.riot.MatchDto
import com.your.lol.dto.statistics.ChampionDataDto
import com.your.lol.dto.statistics.StatsDto
import com.your.lol.support.GlobalTestUtils
import com.your.lol.support.WebClientFacade
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LoLScoreStatsCalculatorTest : GlobalTestUtils() {

    private val webClientFacade: WebClientFacade = mockk()
    private val loLScoreStatsCalculator: LoLScoreStatsCalculator = LoLScoreStatsCalculator(webClientFacade)


    @Test
    fun `소환사 이름을 검색하여 전적 통계를을 산출한다`() {
        // given
        val matchDtos: List<MatchDto> = createTestData("match-dtos.json")
        every { webClientFacade.requestRiotStatistics("테스트 소환사 GG") } returns matchDtos

        // when
        val statsDto: StatsDto = loLScoreStatsCalculator.calculateStatsBySummonerName("테스트 소환사 GG")
        val jinx: ChampionDataDto = findChampionByName(statsDto, "Jinx")
        val riven: ChampionDataDto = findChampionByName(statsDto, "Riven")

        // then
        Assertions.assertAll(
                assertCloseTo(statsDto.winRate, 0.666, 0.001),
                assertEquals(jinx.matchCount, 1),
                assertEquals(jinx.winRate, 1.0),
                assertCloseTo(jinx.averageKda, 3.6, 0.01),
                assertEquals(riven.matchCount, 2),
                assertEquals(riven.winRate, 0.5),
                assertCloseTo(riven.averageKda, 5.97, 0.01)
        )
    }
}