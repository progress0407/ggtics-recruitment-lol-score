package com.your.lol.converter

import com.your.lol.domain.dto.PreProcessedData
import com.your.lol.dto.riot.ChallengesDto
import com.your.lol.dto.riot.MatchDto
import com.your.lol.dto.riot.ParticipantDto
import com.your.lol.dto.riot.ParticipantTeamDto
import org.springframework.stereotype.Component
import java.util.stream.Collectors.toUnmodifiableList

/**
 * Riot API DTO를 통계 추출을 위한 데이터로 변경합니다.
 */
@Component
class PreProcessor {
    fun createPreProcessData(summonerName: String, matchDtos: List<MatchDto>): List<PreProcessedData> {
        return matchDtos.stream()
                .map { matchDto -> matchDto.info ?: throw IllegalArgumentException("Info는 Null이 될 수 없습니다.") }
                .map { info -> info.filterAndCreateCompositeDto(summonerName) }
                .map { participantTeamDto -> mapPreProcessData(participantTeamDto) }
                .collect(toUnmodifiableList())
    }

    private fun mapPreProcessData(participantTeam: ParticipantTeamDto): PreProcessedData {
        val participant: ParticipantDto = participantTeam.participantDto
        val challenges: ChallengesDto =
                participant.challenges ?: throw IllegalArgumentException("ChallengesDto는 Null이 될 수 없습니다.")

        return PreProcessedData(
                championName = participant.championName,
                teamId = participant.teamId,
                deathsByEnemyChamps = challenges.deathsByEnemyChamps,
                gameDuration = participant.gameDuration,
                kda = challenges.kda,
                teamWin = participantTeam.teamWin
        )
    }
}