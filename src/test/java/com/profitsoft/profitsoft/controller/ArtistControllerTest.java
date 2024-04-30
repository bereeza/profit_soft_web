package com.profitsoft.profitsoft.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profitsoft.profitsoft.dto.artist.ArtistInfoDto;
import com.profitsoft.profitsoft.dto.artist.ArtistSaveDto;
import com.profitsoft.profitsoft.entity.Artist;
import com.profitsoft.profitsoft.exception.artist.ArtistNotFoundException;
import com.profitsoft.profitsoft.service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ArtistControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArtistService artistService;

    @BeforeEach
    public void setUp() {
        Artist artist1 = Artist.builder()
                .artistId(1)
                .artistName("Artist_1")
                .artistCountry("Ukraine")
                .build();

        Artist artist2 = Artist.builder()
                .artistId(2)
                .artistName("Artist_2")
                .artistCountry("Ukraine")
                .build();

        artistService.save(artist1);
        artistService.save(artist2);
    }

    @Test
    @DisplayName("Get list of artists")
    public void getAllArtistsTest() throws Exception {
        List<ArtistInfoDto> artists = artistService.getAllArtists();

        when(artistService.getAllArtists()).thenReturn(artists);

        this.mvc.perform(get("/api/artist"))
                .andExpect(status().isOk())
                .andExpect(result -> assertNotNull(artistService.getAllArtists()));
    }

    @Test
    @DisplayName("Add new valid artist")
    public void addArtistTest() throws Exception {
        Artist artist = Artist.builder()
                .artistName("Saved artist")
                .artistCountry("Ukraine")
                .build();

        ArtistSaveDto artistInfoDto = ArtistSaveDto.builder()
                .artistName("Saved artist")
                .artistCountry("Ukraine")
                .build();

        String artistJson = objectMapper.writeValueAsString(artistInfoDto);

        when(artistService.save(artist)).thenReturn(artistInfoDto);

        this.mvc.perform(post("/api/artist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(artistJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Try to add an artist with invalid entity")
    public void addArtistWithInvalidEntityTest() throws Exception {
        Artist artist = Artist.builder()
                .artistName("")
                .artistCountry("")
                .build();

        ArtistSaveDto artistInfoDto = ArtistSaveDto.builder()
                .artistName("")
                .artistCountry("")
                .build();

        String artistJson = objectMapper.writeValueAsString(artistInfoDto);

        when(artistService.save(artist)).thenReturn(artistInfoDto);

        this.mvc.perform(post("/api/artist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(artistJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update existing artist")
    public void updateArtistByIdTest() throws Exception {
        long artistId = 1;
        Artist artist = Artist.builder()
                .artistName("id 1")
                .artistCountry("id 1")
                .build();

        doNothing().when(artistService).updateArtistById(artistId, artist);

        String artistJson = objectMapper.writeValueAsString(artist);

        this.mvc.perform(put("/api/artist/{id}", artistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(artistJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Try to update an artist with a invalid entity")
    public void tryToUpdateArtistWithInvalidEntityTest() throws Exception {
        long artistId = 1;
        Artist artist = Artist.builder()
                .artistName("")
                .artistCountry("")
                .build();

        doNothing().when(artistService).updateArtistById(artistId, artist);

        this.mvc.perform(put("/api/artist/{id}", artistId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete existing artist")
    public void deleteArtistByIdTest() throws Exception {
        long artistId = 1;
        doNothing().when(artistService).deleteArtistById(artistId);
        this.mvc.perform(delete("/api/artist/{id}", artistId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Artist deleted successfully"));
    }

    @Test
    @DisplayName("Try to delete an artist with an invalid id")
    public void tryToDeleteArtistWithInvalidIdTest() throws Exception {
        long artistId = 123;
        doThrow(new ArtistNotFoundException("Artist not found")).when(artistService).deleteArtistById(artistId);
        this.mvc.perform(delete("/api/artist/{id}", artistId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Artist not found"));
    }
}
