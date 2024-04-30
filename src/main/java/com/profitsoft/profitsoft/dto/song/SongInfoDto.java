package com.profitsoft.profitsoft.dto.song;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Song entity with minimal information
 */
@Getter
@Setter
@Builder
public class SongInfoDto {
    private String title;
    private String album;
    private String genre;
}