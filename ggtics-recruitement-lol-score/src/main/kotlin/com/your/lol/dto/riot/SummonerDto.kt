package com.your.lol.dto.riot

import lombok.Data
import lombok.NoArgsConstructor

class SummonerDto(val id: String,
                  val accountId: String,
                  val puuid: String,
                  val name: String,
                  val summonerLevel: String) {

    private constructor() : this("", "", "", "", "")
}
