package com.your.lol.dto.riot

class ParticipantDto(
        val summonerName: String,
        val teamId: Int,
        val championName: String,
        val gameDuration: Long,
        val challenges: ChallengesDto? = null) {

    private constructor() : this("", 0, "", 0, null)

    fun equalsId(summonerName: String): Boolean {
        return this.summonerName == summonerName
    }
}