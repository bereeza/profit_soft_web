package com.profitsoft.profitsoft.dto.song;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity for filtering songs
 */
@Getter
@Setter
@Builder
public class SongQueryDto {
    private String album;
    private String genre;
    private int startPage;
    private int size;
}
