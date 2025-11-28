package com.guillermo.pruebaspring.pruebaspring_poke.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.camel.ProducerTemplate;

import com.guillermo.pruebaspring.pruebaspring_poke.domain.dto.PokemonDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("PokemonTypeFilterService Tests")
@ExtendWith(MockitoExtension.class)
class PokemonTypeFilterServiceTest {

    @InjectMocks
    private PokemonTypeFilterService typeFilterService;

    @Mock
    private ProducerTemplate producerTemplate;

    @Mock
    private PokemonFilterValidationService validationService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testFilterByTypeSuccess() {
        when(validationService.validateLimit(anyInt())).thenReturn(50);

        ObjectNode typeInfo = objectMapper.createObjectNode();
        ArrayNode pokemonArray = objectMapper.createArrayNode();
        
        ObjectNode pokemonEntry = objectMapper.createObjectNode();
        ObjectNode pokemon = objectMapper.createObjectNode();
        pokemon.put("name", "charmander");
        pokemonEntry.set("pokemon", pokemon);
        pokemonArray.add(pokemonEntry);
        
        typeInfo.set("pokemon", pokemonArray);

        when(producerTemplate.requestBodyAndHeader(
            eq("direct:getPokemonType"), 
            isNull(), 
            eq("typeName"), 
            eq("fire"), 
            eq(Object.class)
        )).thenReturn(typeInfo);

        PokemonDTO pokemonDTO = new PokemonDTO();
        pokemonDTO.setName("charmander");
        
        when(producerTemplate.requestBodyAndHeader(
            eq("direct:getPokemon"),
            isNull(),
            eq("pokemonId"),
            eq("charmander"),
            eq(PokemonDTO.class)
        )).thenReturn(pokemonDTO);

        var result = typeFilterService.filterByType("fire", 10);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("charmander", result.get(0).getName());
    }
}
