package com.your.lol.dto.riot

class ParticipantDto(
        private val summonerName: String,
        val teamId: Int,
        val championName: String,
        val gameDuration: Long,
        val challenges: ChallengesDto?) {

    private constructor() : this("", 0, "", 0, null)

    fun equalsId(summonerName: String): Boolean {
        return this.summonerName == summonerName
    }
}