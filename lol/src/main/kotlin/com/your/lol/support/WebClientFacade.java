package com.your.lol.support;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.your.lol.dto.riot.MatchDto;
import com.your.lol.dto.riot.SummonerDto;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientFacade {

    private static final String API_KEY = "RGAPI-69961808-ee94-4d60-ac2b-4d68e178a340";
    private static final WebClient krApiWebClient = WebClient.create("https://kr.api.riotgames.com/lol");
    private static final WebClient asisWebClient = WebClient.create("https://asia.api.riotgames.com/lol");

    @NotNull
    public List<MatchDto> requestRiotStatistics(String summonerName) {
        SummonerDto summonerDto = requestSummonerDto(summonerName);
        String summonerPuuid = summonerDto.getPuuid();
        List<String> matchGames = requestMatchGames(summonerPuuid);
        List<MatchDto> matchDtos = requestMatchDtos(matchGames);
        return matchDtos;
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
                .collect(toUnmodifiableList());
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
