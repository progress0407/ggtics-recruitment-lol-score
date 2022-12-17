package com.your.lol.dto.riot;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamDto {
    private int teamId;
    private boolean win;

    public boolean equalsId(int teamId) {
        return this.teamId == teamId;
    }
}
