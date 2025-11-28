package com.guillermo.pruebaspring.pruebaspring_poke.domain.dto;

import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.guillermo.pruebaspring.pruebaspring_poke.domain.entity.Stats;
import io.swagger.v3.oas.annotations.media.Schema;

@Data @NoArgsConstructor @AllArgsConstructor @Builder @JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Información detallada de un pokemon")
public class PokemonDTO {

    @Schema(description = "ID único del pokemon", example = "1")
    private Integer id;

    @Schema(description = "Nombre del pokemon", example = "Bulbasaur")
    private String name;

    @Schema(description = "Lista de tipos elemental del pokemon", example = "[\"grass\", \"poison\"]")
    private List<String> types;

    @Schema(description = "Peso del pokemon en hectogramos", example = "69")
    private Integer weight;

    @Schema(description = "Experiencia base del pokemon", example = "64")
    private Integer baseExperience;

    @Schema(description = "Estadística de defensa del pokemon", example = "65")
    private Integer defense;

    @Schema(description = "Estadísticas detalladas del pokemon")
    private Stats stats;

}
