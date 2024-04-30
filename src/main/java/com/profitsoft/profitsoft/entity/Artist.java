package com.profitsoft.profitsoft.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * Entity for database manipulation
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "artist")
@JsonIgnoreProperties
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_id")
    private long artistId;

    @Column(name = "artist_name")
    @NotBlank(message = "Artist Name is mandatory!")
    @Size(min = 3, max = 256)
    private String artistName;

    @Column(name = "artist_country")
    @NotBlank(message = "Artist Country is mandatory!")
    @Size(min = 3, max = 256)
    private String artistCountry;

    /**
     * one artist - many songs
     */
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Song> songs;
}
