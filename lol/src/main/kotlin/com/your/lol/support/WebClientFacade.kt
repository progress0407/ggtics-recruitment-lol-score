package com.your.lol.support

import com.your.lol.dto.riot.MatchDto
import com.your.lol.dto.riot.SummonerDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriBuilder
import java.util.stream.Collectors

@Component
class WebClientFacade(
        @Value("\${riot.api.key}")
        private var API_KEY: String = "RGAPI-8f7191c6-d169-437d-8a3d-f85b68d8c21e") {

    companion object {
        private const val KR_API_URL = "/summoner/v4/summoners/by-name/{summonerName}"

        private val krApiWebClient = WebClient.create("https://kr.api.riotgames.com/lol")
        private val asisWebClient = WebClient.create("https://asia.api.riotgames.com/lol")
    }


    fun requestRiotStatistics(summonerName: String): List<MatchDto?> {
        val summonerDto: SummonerDto? = requestSummonerDto(summonerName)
        val summonerPuuid = summonerDto!!.puuid
        val matchGames = requestMatchGames(summonerPuuid)
        return requestMatchDtos(matchGames)
    }

    private fun requestSummonerDto(summonerName: String): SummonerDto? {
        return krApiWebClient.get()
                .uri { uriBuilder: UriBuilder ->
                    uriBuilder
                            .path(KR_API_URL)
                            .queryParam("api_key", API_KEY)
                            .build(summonerName)
                }
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SummonerDto::class.java)
                .block()
    }

    private fun requestGameResult(matchGame: String): MatchDto? {
        return asisWebClient.get()
                .uri { uriBuilder: UriBuilder ->
                    uriBuilder
                            .path("/match/v5/matches/{matchGame}")
                            .queryParam("api_key", API_KEY)
                            .build(matchGame)
                }
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(MatchDto::class.java)
                .block()
    }

    private fun requestMatchDtos(matchGames: List<String>?): List<MatchDto?> {
        return matchGames!!.stream()
                .map { it: String -> requestGameResult(it) }
                .collect(Collectors.toUnmodifiableList())
    }

    private fun requestMatchGames(summonerPuuid: String): List<String>? {
        return asisWebClient.get()
                .uri { uriBuilder: UriBuilder ->
                    uriBuilder
                            .path("/match/v5/matches/by-puuid/{summonerPuuid}/ids")
                            .queryParam("start", "0")
                            .queryParam("count", "20")
                            .queryParam("api_key", API_KEY)
                            .build(summonerPuuid)
                }
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(object : ParameterizedTypeReference<List<String>>() {})
                .block()
    }
}