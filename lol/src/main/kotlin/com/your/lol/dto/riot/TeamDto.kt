package com.your.lol.dto.riot

class TeamDto(
        val teamId: Int,
        val win: Boolean) {

    fun equalsId(teamId: Int): Boolean {
        return this.teamId == teamId
    }
}