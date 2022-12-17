package com.your.lol.dto.riot;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChallengesDto {
    private long deathsByEnemyChamps;
    private double kda;
}
