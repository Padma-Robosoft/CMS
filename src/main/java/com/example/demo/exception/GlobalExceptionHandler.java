package com.example.demo.exception;

import com.example.demo.dto.APIResponseDTO;
import com.example.demo.util.APIResponseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${error.generic-message}")
    private String genericErrorMessage;


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponseDTO<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(APIResponseUtil.createFailureResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<APIResponseDTO<String>> handleInvalidInputException(InvalidInputException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponseUtil.createFailureResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponseDTO<String>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIResponseUtil.createFailureResponse(genericErrorMessage));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponseDTO<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = "Invalid request body: " + ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponseUtil.createFailureResponse(errorMessage));
    }
}
