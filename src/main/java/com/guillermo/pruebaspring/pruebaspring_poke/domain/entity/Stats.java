package com.guillermo.pruebaspring.pruebaspring_poke.domain.entity;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Estadísticas base de un pokemon")
public class Stats {

    @Schema(description = "Puntos de salud (HP)", example = "45")
    private Integer hp;

    @Schema(description = "Estadística de ataque", example = "49")
    private Integer attack;

    @Schema(description = "Estadística de defensa", example = "49")
    private Integer defense;

    @Schema(description = "Ataque especial", example = "65")
    private Integer specialAttack;

    @Schema(description = "Defensa especial", example = "65")
    private Integer specialDefense;

    @Schema(description = "Velocidad", example = "45")
    private Integer speed;
    
}
