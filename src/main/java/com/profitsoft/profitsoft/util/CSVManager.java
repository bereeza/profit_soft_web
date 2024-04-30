package com.profitsoft.profitsoft.util;

import com.profitsoft.profitsoft.dto.song.SongInfoDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// Class for writing data to file
public final class CSVManager {

    // default content type
    private final static String CONTENT_TYPE = "text/csv";
    private final static String DEFAULT_FILE_NAME = "data.csv";

    /**
     * To save list of SongInfoDto to csv file
     *
     * @param data - list of data
     * @param response - response from HttpServletResponse
     */
    @SneakyThrows
    public static void save(List<SongInfoDto> data, HttpServletResponse response) {
        response.setContentType(CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=" + DEFAULT_FILE_NAME);

        try (PrintWriter writer = response.getWriter()) {
            writer.write("Title,Album,Genre\n");

            data.stream()
                    .map(song -> String.format(
                            "%s,%s,%s\n",
                            song.getTitle(),
                            song.getAlbum(),
                            song.getGenre()
                    ))
                    .forEach(writer::write);
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }

}
