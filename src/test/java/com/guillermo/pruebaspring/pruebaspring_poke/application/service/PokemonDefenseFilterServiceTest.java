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

@DisplayName("PokemonDefenseFilterService Tests")
@ExtendWith(MockitoExtension.class)
class PokemonDefenseFilterServiceTest {

    @InjectMocks
    private PokemonDefenseFilterService defenseFilterService;

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
    void testFilterByDefenseSuccess() {
        when(validationService.validateLimit(anyInt())).thenReturn(50);

        ObjectNode pokemonList = objectMapper.createObjectNode();
        ArrayNode resultsArray = objectMapper.createArrayNode();
        
        ObjectNode pokemon1 = objectMapper.createObjectNode();
        pokemon1.put("name", "bulbasaur");
        resultsArray.add(pokemon1);
        
        pokemonList.set("results", resultsArray);
        
        ObjectNode emptyList = objectMapper.createObjectNode();
        emptyList.set("results", objectMapper.createArrayNode());

        when(producerTemplate.requestBodyAndHeaders(
            eq("direct:getPokemonListForFilter"), 
            isNull(), 
            anyMap(), 
            eq(Object.class)
        )).thenReturn(pokemonList).thenReturn(emptyList);

        PokemonDTO pokemonDTO = new PokemonDTO();
        pokemonDTO.setName("bulbasaur");
        pokemonDTO.setDefense(65);
        
        when(producerTemplate.requestBodyAndHeader(
            eq("direct:getPokemon"),
            isNull(),
            eq("pokemonId"),
            eq("bulbasaur"),
            eq(PokemonDTO.class)
        )).thenReturn(pokemonDTO);

        var result = defenseFilterService.filterByMinDefense(50, 10);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("bulbasaur", result.get(0).getName());
        assertEquals(65, result.get(0).getDefense());
    }
}
