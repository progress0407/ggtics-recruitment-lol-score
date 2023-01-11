package com.your.lol.dto.riot

import com.your.lol.domain.ChampionDataDto
import com.your.lol.domain.Stat

class StatDto(stat: Stat) {

    val winRate: Double
    val championDataDtos: List<ChampionDataDto>

    init {
        winRate = stat.winRate
        championDataDtos = stat.championDataDtos
    }
}