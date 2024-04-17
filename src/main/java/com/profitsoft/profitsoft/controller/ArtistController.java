package com.profitsoft.profitsoft.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {
    @GetMapping
    @Operation(summary = "get artist by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The artist is retrieved"),
            @ApiResponse(responseCode = "404", description = "Song not found")
    })
    public ResponseEntity<Void> getArtistById(long id) {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @Operation(summary = "add new artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The artist is added"),
            @ApiResponse(responseCode = "422", description = "Invalid entity")
    })
    public ResponseEntity<Void> addArtist() {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "update artist by id")
    public ResponseEntity<Void> updateArtistById(@PathVariable long id) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete artist by id")
    public ResponseEntity<Void> deleteArtistById(@PathVariable long id) {
        return ResponseEntity.ok().build();
    }
}

