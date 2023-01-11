package com.your.lol.domain

import com.your.lol.converter.PreProcessor
import com.your.lol.dto.riot.MatchDto
import com.your.lol.dto.riot.StatDto
import com.your.lol.support.GlobalTestUtils
import com.your.lol.infrastructure.WebClientFacade
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test

class LoLScoreStatCalculatorTest : GlobalTestUtils() {

    private val webClientFacade: WebClientFacade = mockk()
    private val loLScoreStatsCalculator = LoLScoreStatsCalculator()
    private val preProcessor = PreProcessor()


    @Test
    fun `소환사 이름을 검색하여 전적 통계를을 산출한다`() {
        // given
        val matchDtos: List<MatchDto> = createTestData("match-dtos.json")
        every { webClientFacade.requestRiotStatistics("테스트 소환사 GG") } returns matchDtos
        val preProcessData = preProcessor.createPreProcessData("테스트 소환사 GG", matchDtos)

        // when
        val statDto: StatDto = loLScoreStatsCalculator.calculateStats(preProcessData)
        val jinx: ChampionDataDto = findChampionByName(statDto, "Jinx")
        val riven: ChampionDataDto = findChampionByName(statDto, "Riven")

        // then
        assertAll(
                assertCloseTo<Double>(statDto.winRate, 0.666, 0.001),
                assertEquals(jinx.matchCount, 1),
                assertEquals(jinx.winRate, 1.0),
                assertCloseTo<Double>(jinx.averageKda, 3.6, 0.01),
                assertEquals(riven.matchCount, 2),
                assertEquals(riven.winRate, 0.5),
                assertCloseTo<Double>(riven.averageKda, 5.97, 0.01)
        )
    }
}