package com.your.lol.domain

class Stat(
        val winRate: Double,
        val championDataDtos: List<ChampionDataDto>) {
    private constructor() : this(0.0, emptyList())
}
