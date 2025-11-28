package com.guillermo.pruebaspring.pruebaspring_poke.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.guillermo.pruebaspring.pruebaspring_poke.domain.dto.ErrorResponseDTO;
import com.guillermo.pruebaspring.pruebaspring_poke.domain.exception.PokemonFilterException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PokemonFilterException.class)
    public ResponseEntity<ErrorResponseDTO> handlePokemonFilterException(
            PokemonFilterException ex,
            WebRequest request) {
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            ex.getErrorCode(),
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        HttpStatus status = determineHttpStatus(ex.getErrorCode());
        @SuppressWarnings("null")
        ResponseEntity<ErrorResponseDTO> response = new ResponseEntity<>(errorResponse, status);
        return response;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex,
            WebRequest request) {
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            "INTERNAL_ERROR",
            "Ha ocurrido un error inesperado",
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpStatus determineHttpStatus(String errorCode) {
        return switch (errorCode) {
            case "INVALID_PARAMETER" -> HttpStatus.BAD_REQUEST;
            case "NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "EXTERNAL_API_ERROR" -> HttpStatus.SERVICE_UNAVAILABLE;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}

