package com.your.lol.presentation;

import com.your.lol.domain.LoLScoreStatsCalculator;
import com.your.lol.dto.statistics.StatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class SummonerStatsController {

    private final LoLScoreStatsCalculator loLScoreStatsCalculator;

    @RequestMapping("/summoner/{summonerName}")
    public StatsDto findStatisticsBySummonerName(@PathVariable String summonerName) {
        return loLScoreStatsCalculator.calculateStatsBySummonerName(summonerName);
    }
}