package com.guillermo.pruebaspring.pruebaspring_poke.domain.exception;

public class PokemonFilterException extends RuntimeException {

    private final String errorCode;

    public PokemonFilterException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PokemonFilterException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
