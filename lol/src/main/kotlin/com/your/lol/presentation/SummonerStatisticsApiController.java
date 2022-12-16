package com.your.lol.presentation;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.your.lol.presentation.dto.MatchDto;
import com.your.lol.presentation.dto.SummonerDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/")
public class SummonerStatisticsApiController {

    public static final String API_KEY = "RGAPI-69961808-ee94-4d60-ac2b-4d68e178a340";

    @RequestMapping("/summoner/{summonerName}")
    public List<MatchDto> findStatisticsBySummonerName(@PathVariable String summonerName) {

        // https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/{summonerName}
        SummonerDto summonerDTO = WebClient
                .create("https://kr.api.riotgames.com/lol").get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/summoner/v4/summoners/by-name/{summonerName}")
                                .queryParam("api_key", API_KEY)
                                .build(summonerName))
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SummonerDto.class)
                .block();

        String summonerPuuid = summonerDTO.getPuuid();

        WebClient asisApiClient = WebClient.create("https://asia.api.riotgames.com/lol");
        List<String> matchGames = asisApiClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/match/v5/matches/by-puuid/{summonerPuuid}/ids")
                                .queryParam("start", "0")
                                .queryParam("count", "20")
                                .queryParam("api_key", API_KEY)
                                .build(summonerPuuid))
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                })
                .block();

        // https://asia.api.riotgames.com/lol/match/v5/matches/KR_6262807957?api_key=RGAPI-69961808-ee94-4d60-ac2b-4d68e178a340
        List<MatchDto> matchDtos = new ArrayList<>();
        for (String matchGame : matchGames) {
            MatchDto gameResult = asisApiClient.get()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path("/match/v5/matches/{matchGame}")
                                    .queryParam("api_key", API_KEY)
                                    .build(matchGame)
                    )
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(MatchDto.class)
                    .block();
            matchDtos.add(gameResult);
        }
        return matchDtos;
    }
}
