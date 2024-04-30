package com.profitsoft.profitsoft.service;

import com.profitsoft.profitsoft.dto.song.SongDetailInfoDto;
import com.profitsoft.profitsoft.dto.song.SongInfoDto;
import com.profitsoft.profitsoft.dto.song.SongQueryDto;
import com.profitsoft.profitsoft.dto.song.SongSaveDto;
import com.profitsoft.profitsoft.entity.Artist;
import com.profitsoft.profitsoft.entity.Song;
import com.profitsoft.profitsoft.exception.artist.ArtistNotFoundException;
import com.profitsoft.profitsoft.exception.song.SongNotFoundException;
import com.profitsoft.profitsoft.repository.ArtistRepository;
import com.profitsoft.profitsoft.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for manipulating the song
 */
@Service
@RequiredArgsConstructor
public class SongService {

    // song repository for performing entity operations
    private final SongRepository songRepository;

    // to get and save an artist by id or get exception
    private final ArtistRepository artistRepository;

    // To save songDetailInfo in repository
    public SongDetailInfoDto save(SongSaveDto songSaveDto) {
        Song savedSong = extractSong(songSaveDto);
        Artist artist = getArtistOrElseThrow(songSaveDto);

        savedSong.setArtist(artist);
        songRepository.save(savedSong);
        return getBuild(savedSong);
    }

    // To get Song (SongDetailInfoDto) or get exception
    public Optional<SongDetailInfoDto> getSongById(long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));
        return Optional.ofNullable(getBuild(song));
    }

    // To update song by id and valid new entity or get exception
    public SongDetailInfoDto updateSongById(long id, SongSaveDto song) {
        Optional<Song> optionalSong = songRepository.findById(id);
        if (optionalSong.isPresent()) {
            Song existingSong = optionalSong.get();
            updateSong(song, existingSong);
            songRepository.save(existingSong);
            return getBuild(existingSong);
        } else {
            throw new SongNotFoundException("Song not found");
        }
    }

    // To delete song by id or get exception
    public void deleteSongById(long id) {
        Optional<Song> song = songRepository.findById(id);
        if (song.isPresent()) {
            songRepository.deleteById(id);
        } else {
            throw new SongNotFoundException("Song not found");
        }
    }

    // To get list of song (SongInfoDto) filtered by criteria
    public List<SongInfoDto> getSongsByCriteria(SongQueryDto queryDto) {
        return songRepository.findAll().stream()
                .filter(song -> isMatch(song, queryDto))
                .map(this::getBuilt)
                .toList();
    }

    /**
     * To save all song from list.
     * I also use @BatchSize because I think that it's wrong to store a large batch of data at once,
     * and it's better to break it into batches
     */
    @BatchSize(size = 30)
    public void saveAllSongs(List<SongSaveDto> data) {
        List<Song> songs = data.stream()
                .map(this::extractSong)
                .toList();

        songRepository.saveAll(songs);
    }

    private boolean isMatch(Song song, SongQueryDto queryDto) {
        String album = queryDto.getAlbum();
        String genre = queryDto.getGenre();

        return (album == null || album.equalsIgnoreCase(song.getAlbum())) &&
                (genre == null || genre.equalsIgnoreCase(song.getGenre()));
    }

    private Artist getArtistOrElseThrow(SongSaveDto songSaveDto) {
        return artistRepository
                .findById(
                        songSaveDto.getArtistId()).orElseThrow(() -> new ArtistNotFoundException("Artist not found")
                );
    }

    private void updateSong(SongSaveDto song, Song existingSong) {
        existingSong.setTitle(song.getTitle() != null ? song.getTitle() : existingSong.getTitle());
        existingSong.setDuration(song.getDuration() != null ? song.getDuration() : existingSong.getDuration());
        existingSong.setAlbum(song.getAlbum() != null ? song.getAlbum() : existingSong.getAlbum());
        existingSong.setGenre(song.getGenre() != null ? song.getGenre() : existingSong.getGenre());
        existingSong.setArtist(getArtistOrElseThrow(song));
    }

    private SongInfoDto getBuilt(Song song) {
        return SongInfoDto.builder()
                .title(song.getTitle())
                .album(song.getAlbum())
                .genre(song.getGenre())
                .build();
    }

    private Song extractSong(SongSaveDto songSaveDto) {
        return Song.builder()
                .title(songSaveDto.getTitle())
                .duration(songSaveDto.getDuration())
                .album(songSaveDto.getAlbum())
                .genre(songSaveDto.getGenre())
                .artist(getArtistOrElseThrow(songSaveDto))
                .build();
    }

    private SongDetailInfoDto getBuild(Song savedSong) {
        return SongDetailInfoDto.builder()
                .id(savedSong.getId())
                .title(savedSong.getTitle())
                .duration(savedSong.getDuration())
                .album(savedSong.getAlbum())
                .genre(savedSong.getGenre())
                .artist(savedSong.getArtist())
                .build();
    }
}
