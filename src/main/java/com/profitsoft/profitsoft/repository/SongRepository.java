package com.profitsoft.profitsoft.repository;

import com.profitsoft.profitsoft.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}
