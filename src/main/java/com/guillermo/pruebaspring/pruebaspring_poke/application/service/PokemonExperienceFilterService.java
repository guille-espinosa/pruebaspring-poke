package com.guillermo.pruebaspring.pruebaspring_poke.application.service;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guillermo.pruebaspring.pruebaspring_poke.domain.dto.PokemonDTO;
import com.guillermo.pruebaspring.pruebaspring_poke.domain.exception.PokemonFilterException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class PokemonExperienceFilterService {

    private static final Logger logger = Logger.getLogger(PokemonExperienceFilterService.class.getName());

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private PokemonFilterValidationService validationService;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int BATCH_SIZE = 20;

    public List<PokemonDTO> filterByMinExp(Integer minExp, Integer limit) {
        List<PokemonDTO> result = new ArrayList<>();
        
        if (minExp == null || minExp < 0) {
            logger.warning("Valor de experiencia mínima inválido: " + minExp);
            throw new PokemonFilterException(
                "La experiencia mínima no puede ser negativa",
                "INVALID_PARAMETER"
            );
        }
        
        int maxResults = validationService.validateLimit(limit);
        
        try {
            for (int offset = 0; result.size() < maxResults; offset += BATCH_SIZE) {

                Map<String, Object> headers = new HashMap<>();
                headers.put("limit", BATCH_SIZE);
                headers.put("offset", offset);
                
                Object pokemonList = producerTemplate.requestBodyAndHeaders(
                    "direct:getPokemonListForFilter", null, headers, Object.class);
                
                JsonNode jsonNode = objectMapper.convertValue(pokemonList, JsonNode.class);
                
                JsonNode results = jsonNode.get("results");
                if (results == null || !results.isArray() || results.size() == 0) {
                    break;
                }

                for (JsonNode pokemonNode : results) {
                    if (result.size() >= maxResults) break;
                    
                    String pokemonName = pokemonNode.get("name").asText();
                    try {
                        PokemonDTO dto = producerTemplate
                            .requestBodyAndHeader("direct:getPokemon", null, "pokemonId", pokemonName, PokemonDTO.class);
                        if (dto.getBaseExperience() != null && dto.getBaseExperience() >= minExp) {
                            result.add(dto);
                        }
                    } catch (Exception e) {
                        logger.warning("Error al obtener datos del pokemon: " + pokemonName + ". " + e.getMessage());
                    }
                }
            }
            
            if (result.isEmpty()) {
                logger.info("No se encontraron pokemon con experiencia mínima: " + minExp);
                throw new PokemonFilterException(
                    "No se encontraron pokemon con experiencia mínima de " + minExp,
                    "NOT_FOUND"
                );
            }
            
        } catch (PokemonFilterException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error al filtrar pokemon por experiencia: " + e.getMessage());
            throw new PokemonFilterException(
                "Error al filtrar pokemon por experiencia",
                "EXTERNAL_API_ERROR",
                e
            );
        }
        return result;
    }
}
