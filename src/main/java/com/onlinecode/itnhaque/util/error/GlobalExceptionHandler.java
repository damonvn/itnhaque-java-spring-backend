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

import jakarta.validation.constraints.Null;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IdInvalidException.class)
    public ResponseEntity<ResError> handleIdInvalidException(IdInvalidException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", ex.getMessage());
        errorDetails.put("statusCode", HttpStatus.BAD_REQUEST.value()); // Trả về mã 400

        ResError errRes = new ResError();
        RestResponse<NullPointerException> data = new RestResponse<NullPointerException>();
        data.setStatusCode(HttpStatus.BAD_REQUEST.value());
        data.setMessage(ex.getMessage());
        data.setData(null);
        data.setError(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errRes.setData(data);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errRes);
    }
}
