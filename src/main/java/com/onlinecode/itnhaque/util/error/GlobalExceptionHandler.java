package com.onlinecode.itnhaque.util.error;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.onlinecode.itnhaque.domain.response.RestResponse;
import com.onlinecode.itnhaque.domain.response.error.ResError;

// import com.onlinecode.itnhaque.domain.response.RestResponse;
// import com.onlinecode.itnhaque.domain.response.error.ResError;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IdInvalidException.class)
    public ResponseEntity<Map<String, Object>> handleIdInvalidException(IdInvalidException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("statusCode", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
}
