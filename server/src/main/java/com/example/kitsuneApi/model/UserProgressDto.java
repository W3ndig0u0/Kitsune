package com.example.kitsuneApi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UserProgressDto {
    private String consumetId;
    private String lastEpisodeId;
    private Integer lastTimeInSeconds;
}