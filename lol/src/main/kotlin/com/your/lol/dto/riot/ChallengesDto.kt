package com.your.lol.dto.riot

class ChallengesDto(val deathsByEnemyChamps: Long, val kda: Double) {

    private constructor() : this(0, 0.0)
}
