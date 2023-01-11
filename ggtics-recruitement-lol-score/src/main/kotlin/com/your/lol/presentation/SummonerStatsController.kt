package com.your.lol.presentation

import com.your.lol.converter.PreProcessor
import com.your.lol.domain.LoLScoreStatsCalculator
import com.your.lol.dto.riot.MatchDto
import com.your.lol.dto.riot.StatDto
import com.your.lol.infrastructure.WebClientFacade
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
class SummonerStatsController(
        private val webClientFacade: WebClientFacade,
        private val loLScoreStatsCalculator: LoLScoreStatsCalculator,
        private val preProcessor: PreProcessor) {

    @RequestMapping("/summoner/{summonerName}")
    @ResponseStatus(OK)
    fun findStatisticsBySummonerName(@PathVariable summonerName: String): StatDto {
        val matchDtos: List<MatchDto> = webClientFacade.requestRiotStatistics(summonerName)
        val processData = preProcessor.createPreProcessData(summonerName, matchDtos)
        return loLScoreStatsCalculator.calculateStats(processData)
    }
}