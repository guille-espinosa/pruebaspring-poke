package com.guillermo.pruebaspring.pruebaspring_poke.domain.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de error de la API")
public class ErrorResponseDTO {

    @Schema(description = "Código de error", example = "INVALID_PARAMETER")
    private String errorCode;

    @Schema(description = "Mensaje descriptivo del error", example = "El tipo de pokemon no puede estar vacío")
    private String message;

    @Schema(description = "Timestamp de cuando ocurrió el error")
    private LocalDateTime timestamp;

    @Schema(description = "Ruta del endpoint que causó el error", example = "/api/v1/pokemon/type/fire")
    private String path;

    public ErrorResponseDTO(String errorCode, String message, String path) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

