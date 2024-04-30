package com.profitsoft.profitsoft.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Entity to get list of T data
 *
 * @param <T> - entity type
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Result<T> {
    private List<T> data;
    private int counter;
}