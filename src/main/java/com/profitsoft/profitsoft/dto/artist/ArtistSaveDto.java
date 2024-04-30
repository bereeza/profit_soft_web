package com.profitsoft.profitsoft.dto.artist;

import com.profitsoft.profitsoft.entity.Song;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Artist entity to save
 */
@Getter
@Setter
@Builder
public class ArtistSaveDto {
    private long artistId;
    private String artistName;
    private String artistCountry;
    private List<Song> songs;
}
