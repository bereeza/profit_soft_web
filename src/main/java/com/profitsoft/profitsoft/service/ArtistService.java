package com.profitsoft.profitsoft.service;

import com.profitsoft.profitsoft.dto.artist.ArtistSaveDto;
import com.profitsoft.profitsoft.dto.artist.ArtistInfoDto;
import com.profitsoft.profitsoft.entity.Artist;
import com.profitsoft.profitsoft.exception.artist.ArtistNotFoundException;
import com.profitsoft.profitsoft.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for manipulating the artist
 */
@Service
@RequiredArgsConstructor
public class ArtistService {

    // artist repository for performing entity operations
    private final ArtistRepository artistRepository;

    // To save artist in repository
    public ArtistSaveDto save(Artist artist) {
        Artist savedArtist = artistRepository.save(artist);
        return getBuild(savedArtist);
    }

    // To get all Artist (ArtistInfoDto)
    public List<ArtistInfoDto> getAllArtists() {
        return artistRepository.findAll().stream()
                .map(this::extractArtistInfo)
                .toList();
    }

    // To delete an artist with existing id or get exception
    public void deleteArtistById(long id) {
        Optional<Artist> artist = artistRepository.findById(id);
        if (artist.isPresent()) {
            artistRepository.deleteById(id);
        } else {
            throw new ArtistNotFoundException("Artist not found");
        }
    }

    // To update an artist with existing id
    public void updateArtistById(long id, Artist artist) {
        Optional<Artist> artistOptional = artistRepository.findById(id);
        if (artistOptional.isPresent()) {
            Artist existingArtist = artistOptional.get();
            updateArtist(artist, existingArtist);
            artistRepository.save(existingArtist);
        }
    }

    private void updateArtist(Artist artist, Artist existingArtist) {
        existingArtist.setArtistName(artist.getArtistName());
        existingArtist.setArtistCountry(artist.getArtistCountry());
        existingArtist.setSongs(artist.getSongs());
    }

    private ArtistInfoDto extractArtistInfo(Artist artist) {
        return ArtistInfoDto.builder()
                .artistCountry(artist.getArtistCountry())
                .artistName(artist.getArtistName())
                .build();
    }

    private ArtistSaveDto getBuild(Artist savedArtist) {
        return ArtistSaveDto.builder()
                .artistId(savedArtist.getArtistId())
                .artistName(savedArtist.getArtistName())
                .artistCountry(savedArtist.getArtistCountry())
                .songs(savedArtist.getSongs())
                .build();
    }
}
