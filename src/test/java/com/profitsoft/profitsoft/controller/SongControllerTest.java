package com.profitsoft.profitsoft.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profitsoft.profitsoft.dto.song.SongDetailInfoDto;
import com.profitsoft.profitsoft.dto.song.SongQueryDto;
import com.profitsoft.profitsoft.dto.song.SongSaveDto;
import com.profitsoft.profitsoft.entity.Artist;
import com.profitsoft.profitsoft.exception.artist.ArtistNotFoundException;
import com.profitsoft.profitsoft.exception.song.SongNotFoundException;
import com.profitsoft.profitsoft.service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SongControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SongService songService;

    private Artist artist;

    @BeforeEach
    void setUp() {
        SongSaveDto song1 = SongSaveDto.builder()
                .title("Title 1")
                .duration(1.11F)
                .album("Album 1")
                .genre("Genre 1")
                .artistId(1)
                .build();

        SongSaveDto song2 = SongSaveDto.builder()
                .title("Title 2")
                .duration(2.22F)
                .album("Album 2")
                .genre("Genre 2")
                .artistId(2)
                .build();

        songService.save(song1);
        songService.save(song2);

        artist = Artist.builder()
                .artistId(3)
                .artistName("Artist 3")
                .artistCountry("Country 3")
                .build();
    }

    @Test
    @DisplayName("Add new valid song")
    public void addSongTest() throws Exception {
        SongSaveDto song = SongSaveDto.builder()
                .title("Title 3")
                .duration(3.33F)
                .album("Album 3")
                .genre("Genre 3")
                .artistId(3)
                .build();

        SongDetailInfoDto songDetailInfoDto = SongDetailInfoDto.builder()
                .title("Title 3")
                .duration(3.33F)
                .album("Album 3")
                .genre("Genre 3")
                .artist(artist)
                .build();

        String songJson = objectMapper.writeValueAsString(songDetailInfoDto);

        when(songService.save(song)).thenReturn(songDetailInfoDto);

        this.mvc.perform(post("/api/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(songJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Try to add a song with invalid entity")
    public void addSongWithInvalidEntityTest() throws Exception {
        SongSaveDto song = SongSaveDto.builder()
                .title("")
                .duration(3.33F)
                .album("")
                .genre("Genre 3")
                .artistId(3)
                .build();

        SongDetailInfoDto songDetailInfoDto = SongDetailInfoDto.builder()
                .title("")
                .duration(3.33F)
                .album("")
                .genre("Genre 3")
                .artist(artist)
                .build();

        String songJson = objectMapper.writeValueAsString(songDetailInfoDto);

        when(songService.save(song)).thenReturn(songDetailInfoDto);

        this.mvc.perform(post("/api/song")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(songJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Try to add a song with invalid artist")
    public void addSongWithInvalidArtistTest() throws Exception {
        SongSaveDto song = SongSaveDto.builder()
                .title("Title 3")
                .duration(3.33F)
                .album("Album 3")
                .genre("Genre 3")
                .artistId(123)
                .build();

        doThrow(new ArtistNotFoundException("Artist not found")).when(songService).save(song);

        this.mvc.perform(post("/api/song")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get song by id")
    public void getSongByIdTest() throws Exception {
        long songId = 1;
        Optional<SongDetailInfoDto> song = songService.getSongById(songId);

        when(songService.getSongById(songId)).thenReturn(song);
        this.mvc.perform(get("/api/song/{id}", songId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Try to get a song with invalid id")
    public void tryToGetSongWithInvalidIdTest() throws Exception {
        long songId = 123;
        doThrow(new SongNotFoundException("Song not found")).when(songService).getSongById(songId);
        this.mvc.perform(get("/api/song/{id}", songId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update existing song")
    public void updateSongById() throws Exception {
        long songId = 1;
        SongSaveDto song = SongSaveDto.builder()
                .title("Title 1.1")
                .duration(4.44F)
                .album("Album 1.1")
                .genre("Genre 1.1")
                .artistId(2)
                .build();

        SongDetailInfoDto detailSong = songService.updateSongById(songId, song);

        when(songService.updateSongById(songId, song)).thenReturn(detailSong);

        String songJson = objectMapper.writeValueAsString(song);

        this.mvc.perform(put("/api/song/{id}", songId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(songJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Try to update a song with invalid entity")
    public void tryToUpdateSongWithInvalidEntityTest() throws Exception {
        long songId = 1;
        SongSaveDto song = SongSaveDto.builder()
                .title("")
                .duration(4.44F)
                .album("")
                .genre("")
                .build();

        SongDetailInfoDto detailSong = songService.updateSongById(songId, song);

        when(songService.updateSongById(songId, song)).thenReturn(detailSong);

        this.mvc.perform(put("/api/song/{id}", songId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete existing song")
    public void deleteSongByIdTest() throws Exception {
        long songId = 1;
        doNothing().when(songService).deleteSongById(songId);
        this.mvc.perform(delete("/api/song/{id}", songId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Song deleted successfully"));
    }

    @Test
    @DisplayName("Try to delete a song with an invalid id")
    public void tryToDeleteSongWithInvalidIdTest() throws Exception {
        long songId = 123;
        doThrow(new SongNotFoundException("Song not found")).when(songService).deleteSongById(songId);
        this.mvc.perform(delete("/api/song/{id}", songId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Song not found"));
    }

    @Test
    @DisplayName("Get songs by criteria")
    public void getSongsByCriteriaTestWithPagination() throws Exception {
        SongSaveDto song3 = SongSaveDto.builder()
                .title("Title 3")
                .duration(3.21F)
                .album("Album 1")
                .genre("Genre 1")
                .artistId(1)
                .build();

        String songJson = objectMapper.writeValueAsString(song3);

        // Set the startPage to 1
        // because this is the second song from the Album 1
        this.mvc.perform(post("/api/song/_list")
                        .param("album", "Album 1")
                        .param("startPage", "1")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(songJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Download the song file by criteria")
    public void downloadSongByCriteriaTest() throws Exception {
        SongQueryDto query = SongQueryDto.builder()
                .album("Album 1")
                .build();

        MvcResult result = this.mvc.perform(post("/api/song/_report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(query)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentType()).isEqualTo("text/csv");
        assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Upload the song file")
    public void uploadSongFileTest() throws Exception {
        SongSaveDto song5 = SongSaveDto.builder()
                .title("Title 5")
                .duration(5.55F)
                .album("Album 5")
                .genre("Genre 5")
                .artistId(5)
                .build();

        SongSaveDto song6 = SongSaveDto.builder()
                .title("Title 6")
                .duration(5.55F)
                .album("Album 6")
                .genre("Genre 6")
                .artistId(6)
                .build();

        List<SongSaveDto> songs = Arrays.asList(song5, song6);

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonContent = objectMapper.writeValueAsString(songs);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.json",
                MediaType.APPLICATION_JSON_VALUE,
                jsonContent.getBytes()
        );

        this.mvc.perform(MockMvcRequestBuilders.multipart("/api/song/upload")
                        .file(file))
                .andExpect(status().isOk());
    }
}
