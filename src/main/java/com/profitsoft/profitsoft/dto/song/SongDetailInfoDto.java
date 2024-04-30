package com.profitsoft.profitsoft.dto.song;

import com.profitsoft.profitsoft.entity.Artist;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Song entity with detail information
 * ( For artist it's id, name and country )
 */
@Getter
@Setter
@Builder
public class SongDetailInfoDto {
    private long id;
    private String title;
    private Float duration;
    private String album;
    private String genre;
    private Artist artist;
}
