package com.profitsoft.profitsoft.dto.artist;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Artist entity with minimal information
 */
@Getter
@Setter
@Builder
public class ArtistInfoDto {
    private String artistName;
    private String artistCountry;
}