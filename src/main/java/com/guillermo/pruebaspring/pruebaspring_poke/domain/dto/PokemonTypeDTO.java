package com.guillermo.pruebaspring.pruebaspring_poke.domain.dto;

import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Data @NoArgsConstructor @AllArgsConstructor @Builder @JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Información de un tipo de pokemon")
public class PokemonTypeDTO {
    @Schema(description = "Nombre del tipo elemental", example = "fire")
    private String type;

    @Schema(description = "Lista de pokemon de este tipo")
    private List<PokemonDTO> pokemons;
}