package com.your.lol.dto.statistics

class StatsDto(
        val winRate: Double,
        val championDataDtos: List<ChampionDataDto>?) {
    private constructor() : this(0.0, emptyList())
}