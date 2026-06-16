package com.uca.pokedexcapas012026.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Builder
@Data
public class ApiError {
    private String message;
    private int code;
    private LocalDate timestamp;
    private Object errors;
}
