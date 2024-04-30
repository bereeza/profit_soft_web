package com.profitsoft.profitsoft.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

/**
 * Main entity Song for database manipulation
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "song")
@JsonIgnoreProperties
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "duration")
    private Float duration;

    @Column(name = "album")
    private String album;

    @Column(name = "genre")
    private String genre;

    /**
     * many songs - one artist
     */
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;
}
