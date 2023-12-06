package com.app.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorDetail {

    private String error;
    private String details;
    private LocalDateTime timestamp;
}
