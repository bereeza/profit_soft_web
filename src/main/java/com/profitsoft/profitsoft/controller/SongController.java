package com.profitsoft.profitsoft.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/song")
public class SongController {

    @PostMapping
    @Operation(summary = "add new song")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The song is added"),
            @ApiResponse(responseCode = "422", description = "Invalid entity")
    })
    public ResponseEntity<Void> addSong() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "get song by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The song is retrieved"),
            @ApiResponse(responseCode = "404", description = "Song not found")
    })
    public ResponseEntity<Void> getSongById(@PathVariable long id) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "update song by id")
    public ResponseEntity<Void> updateSongById(@PathVariable long id) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete song by id")
    public ResponseEntity<Void> deleteArtistById(@PathVariable long id) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("_list")
    @Operation(summary = "get a list of songs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of songs retrieved")
    })
    public ResponseEntity<Void> getAllSongs() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("_report")
    @Operation(summary = "get report list of songs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report list of songs retrieved")
    })
    public ResponseEntity<Void> getAllSongsReport() {
        return ResponseEntity.ok().build();
    }
}
