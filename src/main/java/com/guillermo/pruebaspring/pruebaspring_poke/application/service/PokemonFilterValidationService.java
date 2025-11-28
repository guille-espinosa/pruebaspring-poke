package com.guillermo.pruebaspring.pruebaspring_poke.application.service;

import org.springframework.stereotype.Service;

@Service
public class PokemonFilterValidationService {

    private static final int DEFAULT_LIMIT = 50;
    private static final int MAX_LIMIT = 200;

    public int validateLimit(Integer limit) {
        if (limit == null) {
            return DEFAULT_LIMIT;
        }
        if (limit < 1) {
            return 1;
        }
        if (limit > MAX_LIMIT) {
            return MAX_LIMIT;
        }
        return limit;
    }
}
