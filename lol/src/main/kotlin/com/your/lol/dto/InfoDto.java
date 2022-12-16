package com.your.lol.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InfoDto {
    private List<ParticipantDto> participants;
}
