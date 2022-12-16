package com.your.lol.presentation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SummonerResponse {
    private String id;
    private String accountId;
    private String puuid;
    private String name;
    private String summonerLevel;
}
