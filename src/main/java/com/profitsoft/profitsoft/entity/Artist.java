package com.profitsoft.profitsoft.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Artist {
    @Id
    @Column(name = "artist_id")
    private long id;
    private String artistName;
    private String country;
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Song> songs;
}
