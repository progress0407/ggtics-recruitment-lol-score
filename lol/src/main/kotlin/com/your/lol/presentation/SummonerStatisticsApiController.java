package com.your.lol.presentation;

import com.your.lol.domain.SummonerStatistics;
import com.your.lol.dto.statistics.StatisticsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class SummonerStatisticsApiController {

    private final SummonerStatistics summonerStatistics;

    @RequestMapping("/summoner/{summonerName}")
    public StatisticsDto findStatisticsBySummonerName(@PathVariable String summonerName) {
        return summonerStatistics.calculateStatisticsBySummonerName(summonerName);
    }
}
