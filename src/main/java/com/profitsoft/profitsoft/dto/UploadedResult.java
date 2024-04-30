package com.profitsoft.profitsoft.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Result entity to upload
 *
 * @param <T> - entity type
 */
@Getter
@Setter
public class UploadedResult<T> extends Result<T> {
    private int failedRecords;

    public UploadedResult(List<T> data, int counter, int failedRecords) {
        super(data, counter);
        this.failedRecords = failedRecords;
    }
}
