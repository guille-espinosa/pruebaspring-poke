package com.guillermo.pruebaspring.pruebaspring_poke.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.guillermo.pruebaspring.pruebaspring_poke.application.service.*;
import com.guillermo.pruebaspring.pruebaspring_poke.domain.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pokemon")
@Tag(name = "Pokemon Filter API", description = "API para filtrar pokemon por diferentes criterios")
public class PokemonController {

    @Autowired
    private PokemonTypeFilterService pokemonTypeFilterService;

    @Autowired
    private PokemonDefenseFilterService pokemonDefenseFilterService;

    @Autowired
    private PokemonWeightFilterService pokemonWeightFilterService;

    @Autowired
    private PokemonExperienceFilterService pokemonExperienceFilterService;

    @Operation(
        summary = "Filtrar pokemon por tipo elemental",
        description = "Obtiene una lista de pokemon del tipo especificado. Tipos válidos: normal, fire, water, electric, grass, ice, fighting, poison, ground, flying, psychic, bug, rock, ghost, dragon, dark, steel, fairy"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pokemon encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "array"))),
        @ApiResponse(responseCode = "400", description = "Tipo de pokemon inválido o parámetros inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron pokemon del tipo especificado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "503", description = "Error en la API externa de PokéAPI",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/type/{type}")
    public ResponseEntity<List<PokemonDTO>> filterByType(
            @Parameter(description = "Tipo de pokemon (ej: fire, water, grass)", required = true, example = "fire")
            @PathVariable String type,
            @Parameter(description = "Límite máximo de resultados (1-200, por defecto 50)", example = "20")
            @RequestParam(required = false) Integer limit) {
        List<PokemonDTO> result = pokemonTypeFilterService.filterByType(type, limit);
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Filtrar pokemon por defensa mínima",
        description = "Obtiene una lista de pokemon que tienen al menos el valor de defensa especificado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pokemon con defensa mínima",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "array"))),
        @ApiResponse(responseCode = "400", description = "Parámetro de defensa inválido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron pokemon con la defensa especificada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "503", description = "Error en la API externa",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/defense")
    public ResponseEntity<List<PokemonDTO>> filterByDefense(
            @Parameter(description = "Valor mínimo de defensa (no negativo)", required = true, example = "50")
            @RequestParam(required = true) Integer min,
            @Parameter(description = "Límite máximo de resultados (1-200, por defecto 50)", example = "20")
            @RequestParam(required = false) Integer limit) {
        List<PokemonDTO> result = pokemonDefenseFilterService.filterByMinDefense(min, limit);
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Filtrar pokemon por rango de peso",
        description = "Obtiene una lista de pokemon dentro del rango de peso especificado (en hectogramos)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pokemon dentro del rango",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "array"))),
        @ApiResponse(responseCode = "400", description = "Parámetros de peso inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron pokemon en el rango especificado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "503", description = "Error en la API externa",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/weight")
    public ResponseEntity<List<PokemonDTO>> filterByWeight(
            @Parameter(description = "Peso mínimo en hectogramos (no negativo)", example = "60")
            @RequestParam(required = false) Integer min,
            @Parameter(description = "Peso máximo en hectogramos (no negativo)", example = "100")
            @RequestParam(required = false) Integer max,
            @Parameter(description = "Límite máximo de resultados (1-200, por defecto 50)", example = "20")
            @RequestParam(required = false) Integer limit) {
        List<PokemonDTO> result = pokemonWeightFilterService.filterByWeight(min, max, limit);
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Filtrar pokemon por experiencia base mínima",
        description = "Obtiene una lista de pokemon que tienen al menos el valor de experiencia base especificado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pokemon con experiencia mínima",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "array"))),
        @ApiResponse(responseCode = "400", description = "Parámetro de experiencia inválido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron pokemon con la experiencia especificada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "503", description = "Error en la API externa",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/exp")
    public ResponseEntity<List<PokemonDTO>> filterByExp(
            @Parameter(description = "Valor mínimo de experiencia base (no negativo)", required = true, example = "150")
            @RequestParam(required = true) Integer min,
            @Parameter(description = "Límite máximo de resultados (1-200, por defecto 50)", example = "20")
            @RequestParam(required = false) Integer limit) {
        List<PokemonDTO> result = pokemonExperienceFilterService.filterByMinExp(min, limit);
        return ResponseEntity.ok(result);
    }
}
