package com.profitsoft.profitsoft.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.profitsoft.profitsoft.dto.UploadedResult;
import com.profitsoft.profitsoft.dto.song.SongSaveDto;
import com.profitsoft.profitsoft.exception.json.InvalidFileExtensionException;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class for converting data from json file to normal data
 */
public final class JSONParser {

    // default extension
    private static final String JSON_EXTENSION = ".json";
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    /**
     * To get UploadedResult with SongSaveDto type
     *
     * @param file - file with data
     * @return - result with data
     */
    @SneakyThrows
    public static UploadedResult<SongSaveDto> upload(MultipartFile file) {
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(JSON_EXTENSION)) {
            throw new InvalidFileExtensionException("Uploaded file is not a JSON file");
        }

        byte[] bytes = file.getBytes();
        List<SongSaveDto> songs = JSON_MAPPER.readValue(bytes, new TypeReference<>() {
        });
        List<SongSaveDto> validSongs = songs.stream()
                .filter(JSONParser::isValidSong)
                .collect(Collectors.toList());

        int successRecords = validSongs.size();
        int failedRecords = songs.size() - successRecords;

        return new UploadedResult<>(validSongs, successRecords, failedRecords);
    }

    private static boolean isValidSong(SongSaveDto songSaveDto) {
        return StringUtils.hasText(songSaveDto.getTitle()) &&
                StringUtils.hasText(songSaveDto.getAlbum()) &&
                StringUtils.hasText(songSaveDto.getGenre()) &&
                songSaveDto.getDuration() != null &&
                songSaveDto.getArtistId() > 0;
    }
}