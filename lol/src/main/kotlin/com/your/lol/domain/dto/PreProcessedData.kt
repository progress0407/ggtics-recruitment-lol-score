package com.your.lol.domain.dto

import lombok.Builder

/**
 * 도메인 내부에서 사용되고 있는 데이터 가공목적으로 사용한 DTO 클래스
 */
data class PreProcessedData(
        val championName: String,
        val teamId: Int,
        val kda: Double,
        val deathsByEnemyChamps: Long,
        val gameDuration: Long,
        val teamWin: Boolean) {

    fun convertTeamWinToNumber(): Int {
        return if (teamWin) 1 else 0
    }
}