package com.your.lol.domain;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.your.lol.dto.MatchDto;
import com.your.lol.dto.SummonerDto;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SummonerStatistics {

    private static final String API_KEY = "RGAPI-69961808-ee94-4d60-ac2b-4d68e178a340";
    private static final WebClient krApiWebClient = WebClient.create("https://kr.api.riotgames.com/lol");
    private static final WebClient asisWebClient = WebClient.create("https://asia.api.riotgames.com/lol");

    public List<MatchDto> calculateStatisticsBySummonerName(String summonerName) {
        SummonerDto summonerDto = requestSummonerDto(summonerName);
        String summonerPuuid = summonerDto.getPuuid();
        List<String> matchGames = requestMatchGames(summonerPuuid);
        return requestMatchDtos(matchGames);
    }

    @Nullable
    private SummonerDto requestSummonerDto(String summonerName) {
        return krApiWebClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/summoner/v4/summoners/by-name/{summonerName}")
                                .queryParam("api_key", API_KEY)
                                .build(summonerName))
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(SummonerDto.class)
                .block();
    }

    @Nullable
    private MatchDto requestGameResult(String matchGame) {
        return asisWebClient.get()
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
    }

    @NotNull
    private List<MatchDto> requestMatchDtos(List<String> matchGames) {
        return matchGames.stream()
                .map(it -> requestGameResult(it))
                .collect(Collectors.toUnmodifiableList());
    }

    @Nullable
    private List<String> requestMatchGames(String summonerPuuid) {
        return asisWebClient.get()
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
    }
}
