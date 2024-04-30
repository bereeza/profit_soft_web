package com.profitsoft.profitsoft.controller;

import com.profitsoft.profitsoft.dto.Response;
import com.profitsoft.profitsoft.dto.artist.ArtistSaveDto;
import com.profitsoft.profitsoft.dto.artist.ArtistInfoDto;
import com.profitsoft.profitsoft.entity.Artist;
import com.profitsoft.profitsoft.exception.artist.ArtistNotFoundException;
import com.profitsoft.profitsoft.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Secondary controller responsible for manipulating the artist
 */
@RestController
@RequestMapping("/api/artist")
@RequiredArgsConstructor
public class ArtistController {

    /**
     * Artist Service for interaction between the controller & DB
     */
    private final ArtistService artistService;

    /**
     * To get list with artist
     *
     * @return - list response with Artist data ( Artist entity is incomplete )
     */
    @GetMapping
    @Operation(summary = "get all artists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The artist is retrieved")
    })
    public ResponseEntity<List<ArtistInfoDto>> getAllArtists() {
        return ResponseEntity
                .ok(artistService.getAllArtists());
    }

    /**
     * To create a new valid artist. Incorrect entity = 400 Bad request
     *
     * @param artist - valid entity of artist
     * @return - response with ArtistSaveDto data
     */
    @PostMapping
    @Operation(summary = "add new artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The artist is added"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<ArtistSaveDto> addArtist(@Valid @RequestBody Artist artist) {
        return ResponseEntity
                .ok(artistService.save(artist));
    }

    /**
     * To update an artist with valid entity artist.
     * User can put a completely new entity or partial param.
     * Incorrect entity = 400 Bad request
     *
     * @param id     - artist id
     * @param artist - new valid entity
     * @return - response with message
     */
    @PutMapping("/{id}")
    @Operation(summary = "update artist by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public Response updateArtistById(@PathVariable long id, @Valid @RequestBody Artist artist) {
        artistService.updateArtistById(id, artist);
        return new Response("Artist updated successfully");
    }

    /**
     * To delete an existing artist.
     *
     * @param id - artist id
     * @return - response with successful message or 404 Not found.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "delete artist by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The artist is deleted"),
            @ApiResponse(responseCode = "404", description = "Artist not found")
    })
    public ResponseEntity<Response> deleteArtistById(@PathVariable long id) {
        try {
            artistService.deleteArtistById(id);
            return ResponseEntity.ok(new Response("Artist deleted successfully"));
        } catch (ArtistNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(ex.getMessage()));
        }
    }
}

