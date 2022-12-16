package com.your.lol.presentation.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatchListDto {
    private List<String> games;
}
