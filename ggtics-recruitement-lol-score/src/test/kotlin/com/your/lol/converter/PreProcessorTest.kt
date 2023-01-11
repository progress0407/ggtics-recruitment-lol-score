package com.your.lol.converter

import com.your.lol.domain.dto.PreProcessedData
import com.your.lol.dto.riot.MatchDto
import com.your.lol.infrastructure.WebClientFacade
import com.your.lol.support.GlobalTestUtils
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class PreProcessorTest : GlobalTestUtils() {

    private val webClientFacade: WebClientFacade = mockk()
    private val preProcessor = PreProcessor()

    @Test
    fun createPreProcessData() {
        // given
        val matchDtos: List<MatchDto> = createTestData("match-dtos.json")
        every { webClientFacade.requestRiotStatistics("테스트 소환사 GG") } returns matchDtos

        // when
        val preProcessData = preProcessor.createPreProcessData("테스트 소환사 GG", matchDtos)
        val jinxs: List<PreProcessedData> = preProcessData.findChampionsByName("Jinx")
        val jinx = jinxs[0]
        val rivens: List<PreProcessedData> = preProcessData.findChampionsByName("Riven")

        // then
        assertAll(
                assertEquals(jinxs.size, 1),
                assertEquals(rivens.size, 2),
                assertEquals(jinx.teamId, 100),
                assertEquals(jinx.kda, 3.6),
                assertEquals(jinx.deathsByEnemyChamps, 1),
                assertEquals(jinx.gameDuration, 2400),
                assertEquals(jinx.teamWin, true)
        )
    }
}
