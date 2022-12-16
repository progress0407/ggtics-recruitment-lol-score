package com.your.lol.presentation;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.your.lol.application.SummonerStatisticsService;
import com.your.lol.domain.SummonerStatistics;
import com.your.lol.dto.MatchDto;
import com.your.lol.dto.SummonerDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class SummonerStatisticsApiController {

    private final SummonerStatistics summonerStatistics;

    @RequestMapping("/summoner/{summonerName}")
    public List<MatchDto> findStatisticsBySummonerName(@PathVariable String summonerName) {
        return summonerStatistics.calculateStatisticsBySummonerName(summonerName);
    }
}
