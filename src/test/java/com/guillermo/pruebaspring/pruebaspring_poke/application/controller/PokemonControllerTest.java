package com.guillermo.pruebaspring.pruebaspring_poke.application.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.guillermo.pruebaspring.pruebaspring_poke.application.service.*;
import com.guillermo.pruebaspring.pruebaspring_poke.domain.dto.PokemonDTO;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("PokemonController Tests")
@WebMvcTest(PokemonController.class)
class PokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PokemonTypeFilterService pokemonTypeFilterService;

    @MockitoBean
    private PokemonDefenseFilterService pokemonDefenseFilterService;

    @MockitoBean
    private PokemonWeightFilterService pokemonWeightFilterService;

    @MockitoBean
    private PokemonExperienceFilterService pokemonExperienceFilterService;

    @Test
    void testFilterByTypeSuccess() throws Exception {
        List<PokemonDTO> mockResult = new ArrayList<>();
        PokemonDTO pokemon = new PokemonDTO();
        pokemon.setName("charmander");
        mockResult.add(pokemon);

        when(pokemonTypeFilterService.filterByType("fire", null))
            .thenReturn(mockResult);

        mockMvc.perform(get("/api/v1/pokemon/type/fire"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("charmander"));
    }

    @Test
    void testFilterByDefenseSuccess() throws Exception {
        List<PokemonDTO> mockResult = new ArrayList<>();
        PokemonDTO pokemon = new PokemonDTO();
        pokemon.setName("bulbasaur");
        pokemon.setDefense(65);
        mockResult.add(pokemon);

        when(pokemonDefenseFilterService.filterByMinDefense(50, null))
            .thenReturn(mockResult);

        mockMvc.perform(get("/api/v1/pokemon/defense?min=50"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("bulbasaur"))
            .andExpect(jsonPath("$[0].defense").value(65));
    }

    @Test
    void testFilterByWeightSuccess() throws Exception {
        List<PokemonDTO> mockResult = new ArrayList<>();
        PokemonDTO pokemon = new PokemonDTO();
        pokemon.setName("bulbasaur");
        pokemon.setWeight(69);
        mockResult.add(pokemon);

        when(pokemonWeightFilterService.filterByWeight(60, 80, null))
            .thenReturn(mockResult);

        mockMvc.perform(get("/api/v1/pokemon/weight?min=60&max=80"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("bulbasaur"))
            .andExpect(jsonPath("$[0].weight").value(69));
    }

    @Test
    void testFilterByExpSuccess() throws Exception {
        List<PokemonDTO> mockResult = new ArrayList<>();
        PokemonDTO pokemon = new PokemonDTO();
        pokemon.setName("charizard");
        pokemon.setBaseExperience(240);
        mockResult.add(pokemon);

        when(pokemonExperienceFilterService.filterByMinExp(200, null))
            .thenReturn(mockResult);

        mockMvc.perform(get("/api/v1/pokemon/exp?min=200"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("charizard"))
            .andExpect(jsonPath("$[0].baseExperience").value(240));
    }
}
