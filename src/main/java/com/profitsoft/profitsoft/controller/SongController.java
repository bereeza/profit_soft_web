package com.profitsoft.profitsoft.controller;

import com.profitsoft.profitsoft.dto.Result;
import com.profitsoft.profitsoft.dto.Response;
import com.profitsoft.profitsoft.dto.UploadedResult;
import com.profitsoft.profitsoft.dto.song.SongDetailInfoDto;
import com.profitsoft.profitsoft.dto.song.SongInfoDto;
import com.profitsoft.profitsoft.dto.song.SongQueryDto;
import com.profitsoft.profitsoft.dto.song.SongSaveDto;
import com.profitsoft.profitsoft.exception.song.SongNotFoundException;
import com.profitsoft.profitsoft.service.SongService;
import com.profitsoft.profitsoft.util.CSVManager;
import com.profitsoft.profitsoft.util.JSONParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * The main controller that works with the main entity - Song
 */
@RestController
@RequestMapping("/api/song")
@RequiredArgsConstructor
public class SongController {

    /**
     * Song Service for interaction between the controller & DB
     */
    private final SongService songService;

    /**
     * To create a new valid song. Incorrect entity = 400 Bad request
     *
     * @param songSaveDto - valid entity of songSaveDto
     * @return - response with SongDetailInfoDto data
     */
    @PostMapping
    @Operation(summary = "add new song")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The song is added"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<SongDetailInfoDto> addSong(@Valid @RequestBody SongSaveDto songSaveDto) {
        return ResponseEntity
                .ok(songService.save(songSaveDto));
    }

    /**
     * To get a song by id
     *
     * @param id - song id
     * @return - response with SongDetailInfoDto data
     */
    @GetMapping("/{id}")
    @Operation(summary = "get song by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The song is retrieved"),
            @ApiResponse(responseCode = "404", description = "Song not found")
    })
    public ResponseEntity<Optional<SongDetailInfoDto>> getSongById(@PathVariable long id) {
        return ResponseEntity
                .ok(songService.getSongById(id));
    }

    /**
     * To update a song with valid entity.
     * User can put a completely new entity or partial param.
     * Incorrect entity = 400 Bad request
     *
     * @param id   - song id
     * @param song - new valid entity
     * @return - response with SongDetailInfoDto data
     */
    @PutMapping("/{id}")
    @Operation(summary = "update song by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Song updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<SongDetailInfoDto> updateSongById(
            @PathVariable long id, @Valid @RequestBody SongSaveDto song) {
        return ResponseEntity
                .ok(songService.updateSongById(id, song));
    }

    /**
     * To delete an existing song.
     *
     * @param id - song id
     * @return - response with successful message or 404 Not found.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "delete song by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The song is deleted"),
            @ApiResponse(responseCode = "404", description = "Song not found")
    })
    public ResponseEntity<Response> deleteSongById(@PathVariable long id) {
        try {
            songService.deleteSongById(id);
            return ResponseEntity.ok(new Response("Song deleted successfully"));
        } catch (SongNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(ex.getMessage()));
        }
    }

    /**
     * To get all songs by criteria
     *
     * @param query - criteria for filtering
     * @return - result entity with data & total_page (counter)
     */
    @PostMapping("_list")
    @Operation(summary = "get a list of songs")
    @ApiResponse(responseCode = "200", description = "List of songs filtered by criteria")
    public Result<SongInfoDto> getAllSongs(@RequestBody SongQueryDto query) {
        List<SongInfoDto> songs = songService.getSongsByCriteria(query);
        int startIndex = query.getStartPage() * query.getSize();
        int endIndex = Math.min(startIndex + query.getSize(), songs.size());

        int totalPages = (int) Math.ceil((double) songs.size() / query.getSize());
        return new Result<>(songs.subList(startIndex, endIndex), totalPages);
    }

    /**
     * To get all songs by criteria & save result
     *
     * @param query    - criteria for filtering
     * @param response - response from HttpServletResponse (header, type, etc.)
     */
    @PostMapping("_report")
    @Operation(summary = "get report list of songs")
    @ApiResponse(responseCode = "200", description = "Report list of songs retrieved")
    public void getAllSongsReport(@RequestBody SongQueryDto query, HttpServletResponse response) {
        List<SongInfoDto> songs = songService.getSongsByCriteria(query);
        CSVManager.save(songs, response);
    }

    /**
     * To upload a data file to the service
     *
     * @param file - json file with data
     * @return - response with result ( Successfully & Failed)
     */
    @PostMapping("/upload")
    @Operation(summary = "upload .json data file")
    @ApiResponse(responseCode = "200", description = "Json file of songs uploaded")
    public Response uploadSong(@RequestParam("file") MultipartFile file) {
        UploadedResult<SongSaveDto> result = JSONParser.upload(file);
        songService.saveAllSongs(result.getData());
        return new Response(String.format("Successfully uploaded: %d. Failed: %d",
                result.getCounter(), result.getFailedRecords()));
    }
}
