package com.your.lol.dto.statistics

class ChampionDataDto(
        val championName: String?,
        val winRate: Double,
        val matchCount: Int, // 경기 횟수

        val averageKda: Double) {

    private constructor() : this("", 0.0, 0, 0.0)
}