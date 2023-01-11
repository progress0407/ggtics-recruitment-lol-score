package com.your.lol.infrastructure

import com.your.lol.dto.riot.MatchDto
import com.your.lol.dto.riot.SummonerDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import java.lang.IllegalArgumentException
import java.util.stream.Collectors.toUnmodifiableList

@Component
class WebClientFacade(
        @Value("\${riot.api.key}") private val apiKey: String = "RGAPI-8f7191c6-d169-437d-8a3d-f85b68d8c21e") {

    companion object {
        private const val KR_API_URL = "/summoner/v4/summoners/by-name/{summonerName}"

        private val krApiWebClient = WebClient.create("https://kr.api.riotgames.com/lol")
        private val asisWebClient = WebClient.create("https://asia.api.riotgames.com/lol")
    }


    fun requestRiotStatistics(summonerName: String): List<MatchDto> {
        val summonerDto: SummonerDto = requestSummonerDto(summonerName)
        val matchGames: List<String> = requestMatchGames(summonerDto.puuid)
        return convertMatchDtos(matchGames)
    }

    private fun requestSummonerDto(summonerName: String): SummonerDto {
        return krApiWebClient.get()
                .uri { uriBuilder -> uriBuildBySummonerName(uriBuilder, summonerName) }
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SummonerDto::class.java)
                .block() ?: throw IllegalArgumentException("Riot API의 응답값으로 Null이 들어왔습니다.")
    }

    private fun uriBuildBySummonerName(uriBuilder: UriBuilder, summonerName: String) =
            uriBuilder
                    .path(KR_API_URL)
                    .queryParam("api_key", apiKey)
                    .build(summonerName)

    private fun requestGameResult(matchGame: String): MatchDto {
        return asisWebClient.get()
                .uri { uriBuilder -> uriBuildByMatchGame(uriBuilder, matchGame) }
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(MatchDto::class.java)
                .block() ?: throw IllegalArgumentException("Riot API의 응답값으로 Null이 들어왔습니다.")
    }

    private fun uriBuildByMatchGame(uriBuilder: UriBuilder, matchGame: String) = uriBuilder
            .path("/match/v5/matches/{matchGame}")
            .queryParam("api_key", apiKey)
            .build(matchGame)

    private fun convertMatchDtos(matchGames: List<String>): List<MatchDto> {
        return matchGames.stream()
                .map { matchGame -> requestGameResult(matchGame) }
                .collect(toUnmodifiableList())
    }

    private fun requestMatchGames(summonerPuuid: String): List<String> {
        return asisWebClient.get()
                .uri { uriBuilder: UriBuilder -> uriBuildBySummonerPuuid(uriBuilder, summonerPuuid) }
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(object : ParameterizedTypeReference<List<String>>() {})
                .block() ?: throw IllegalArgumentException("Riot API의 응답값으로 Null이 들어왔습니다.")
    }

    private fun uriBuildBySummonerPuuid(uriBuilder: UriBuilder, summonerPuuid: String) =
            uriBuilder
                    .path("/match/v5/matches/by-puuid/{summonerPuuid}/ids")
                    .queryParam("start", "0")
                    .queryParam("count", "20")
                    .queryParam("api_key", apiKey)
                    .build(summonerPuuid)
}