package com.guillermo.pruebaspring.pruebaspring_poke.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Entidad de pokemon")
public class Pokemon {

    @Schema(description = "ID único del pokemon", example = "1")
    private Integer id;

    @Schema(description = "Nombre del pokemon", example = "bulbasaur")
    private String name;

    @Schema(description = "Lista de tipos elemental del pokemon")
    private List<String> types;

    @Schema(description = "Peso del pokemon en hectogramos", example = "69")
    private Integer weight;

    @Schema(description = "Experiencia base del pokemon", example = "64")
    private Integer baseExperience;

    @Schema(description = "Estadísticas del pokemon")
    private Stats stats;

}
