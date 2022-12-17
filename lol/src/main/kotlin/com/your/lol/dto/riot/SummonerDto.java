package com.your.lol.dto.riot;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SummonerDto {
    private String id;
    private String accountId;
    private String puuid;
    private String name;
    private String summonerLevel;
}
