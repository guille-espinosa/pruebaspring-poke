package com.guillermo.pruebaspring.pruebaspring_poke.application.service;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guillermo.pruebaspring.pruebaspring_poke.domain.dto.PokemonDTO;
import com.guillermo.pruebaspring.pruebaspring_poke.domain.exception.PokemonFilterException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PokemonTypeFilterService {

    private static final Logger logger = Logger.getLogger(PokemonTypeFilterService.class.getName());

    @Autowired ProducerTemplate producerTemplate;

    @Autowired
    private PokemonFilterValidationService validationService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<PokemonDTO> filterByType(String type, Integer limit) {
        List<PokemonDTO> result = new ArrayList<>();
        
        if (type == null || type.trim().isEmpty()) {
            logger.warning("Tipo de pokemon vacío o nulo");
            throw new PokemonFilterException(
                "El tipo de pokemon no puede estar vacío",
                "INVALID_PARAMETER"
            );
        }
        
        int maxResults = validationService.validateLimit(limit);
        
        try {
            Object typeInfo = producerTemplate
                .requestBodyAndHeader("direct:getPokemonType", null, "typeName", type.toLowerCase(), Object.class);
            JsonNode jsonNode = objectMapper.convertValue(typeInfo, JsonNode.class);
            
            JsonNode pokemon = jsonNode.get("pokemon");
            if (pokemon != null && pokemon.isArray()) {
                for (JsonNode pokeNode : pokemon) {
                    if (result.size() >= maxResults) break;
                    
                    String pokemonName = pokeNode.get("pokemon").get("name").asText();
                    try {
                        PokemonDTO dto = producerTemplate
                            .requestBodyAndHeader("direct:getPokemon", null, "pokemonId", pokemonName, PokemonDTO.class);
                        result.add(dto);
                    } catch (Exception e) {
                        logger.warning("Error al obtener datos del pokemon: " + pokemonName + ". " + e.getMessage());
                    }
                }
            }
            
            if (result.isEmpty()) {
                logger.info("No se encontraron pokemon del tipo: " + type);
                throw new PokemonFilterException(
                    "No se encontraron pokemon del tipo: " + type,
                    "NOT_FOUND"
                );
            }
            
        } catch (PokemonFilterException e) {
            throw e;
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("404")) {
                logger.warning("Tipo de pokemon no válido: " + type);
                throw new PokemonFilterException(
                    "El tipo de pokemon '" + type + "' no existe",
                    "INVALID_PARAMETER",
                    e
                );
            }
            logger.severe("Error al filtrar pokemon por tipo: " + e.getMessage());
            throw new PokemonFilterException(
                "Error al filtrar pokemon por tipo",
                "EXTERNAL_API_ERROR",
                e
            );
        }
        return result;
    }
}
