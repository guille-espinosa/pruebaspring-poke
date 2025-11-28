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
public class PokemonWeightFilterService {

    private static final Logger logger = Logger.getLogger(PokemonWeightFilterService.class.getName());

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private PokemonFilterValidationService validationService;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int BATCH_SIZE = 20;

    public List<PokemonDTO> filterByWeight(Integer minWeight, Integer maxWeight, Integer limit) {
        List<PokemonDTO> result = new ArrayList<>();
        
        if ((minWeight != null && minWeight < 0) || (maxWeight != null && maxWeight < 0)) {
            logger.warning("Valores de peso inválidos - Min: " + minWeight + ", Max: " + maxWeight);
            throw new PokemonFilterException(
                "Los valores de peso no pueden ser negativos",
                "INVALID_PARAMETER"
            );
        }
        
        if (minWeight != null && maxWeight != null && minWeight > maxWeight) {
            logger.warning("Rango de peso inválido - Min: " + minWeight + ", Max: " + maxWeight);
            throw new PokemonFilterException(
                "El peso mínimo no puede ser mayor que el peso máximo",
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
                        if (dto.getWeight() != null) {
                            if ((minWeight == null || dto.getWeight() >= minWeight) &&
                                (maxWeight == null || dto.getWeight() <= maxWeight)) {
                                result.add(dto);
                            }
                        }
                    } catch (Exception e) {
                        logger.warning("Error al obtener datos del pokemon: " + pokemonName + ". " + e.getMessage());
                    }
                }
            }
            
            if (result.isEmpty()) {
                logger.info("No se encontraron pokemon en el rango de peso - Min: " + minWeight + ", Max: " + maxWeight);
                throw new PokemonFilterException(
                    "No se encontraron pokemon en el rango de peso especificado",
                    "NOT_FOUND"
                );
            }
            
        } catch (PokemonFilterException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error al filtrar pokemon por peso: " + e.getMessage());
            throw new PokemonFilterException(
                "Error al filtrar pokemon por peso",
                "EXTERNAL_API_ERROR",
                e
            );
        }
        return result;
    }
}
