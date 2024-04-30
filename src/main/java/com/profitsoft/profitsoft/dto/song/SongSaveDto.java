package com.profitsoft.profitsoft.dto.song;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entity to save song with validation parameters
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongSaveDto {

    @NotBlank(message = "Song Title is mandatory!")
    @Size(min = 3, max = 256)
    @JsonProperty("title")
    private String title;

    @JsonProperty("duration")
    private Float duration;

    @Size(min = 3, max = 256)
    @JsonProperty("album")
    private String album;

    @Size(min = 3, max = 256)
    @JsonProperty("genre")
    private String genre;

    @NotNull
    @JsonProperty("artist_id")
    private long artistId;
}
