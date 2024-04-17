package com.profitsoft.profitsoft.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    @Id
    private long id;
    private String title;
    private Float duration;
    private String album;
    private String genre;
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;
}
