package com.your.lol.dto.riot

import java.util.Collections.emptyList

class InfoDto(
        val participants: List<ParticipantDto>,
        val teams: List<TeamDto>) {

    private constructor() : this(emptyList(), emptyList())

    fun filterAndCreateCompositeDto(summonerName: String): ParticipantTeamDto {
        val findParticipant = findParticipant(summonerName)
        val findTeam = getFindTeam(findParticipant)
        return ParticipantTeamDto(findParticipant, findTeam.win)
    }

    private fun findParticipant(summonerName: String): ParticipantDto = participants.stream()
            .filter { it.equalsId(summonerName) }
            .findAny()
            .orElseThrow { IllegalArgumentException("챔피언을 찾을 수 없습니다.") }

    private fun getFindTeam(findParticipant: ParticipantDto): TeamDto =
            teams.stream()
                    .filter { it.equalsId(findParticipant.teamId) }
                    .findAny()
                    .orElseThrow { IllegalArgumentException("팀을 찾을 수 없습니다.") }
}