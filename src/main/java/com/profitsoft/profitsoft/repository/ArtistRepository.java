package com.profitsoft.profitsoft.repository;

import com.profitsoft.profitsoft.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
