package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Estadísticas de la verificación de ADN")
public class StatsResponse {

    @JsonProperty("count_mutant_dna")
    @Schema(description = "Cantidad de DNAs mutantes detectados")
    private long countMutantDna;

    @JsonProperty("count_human_dna")
    @Schema(description = "Cantidad de DNAs humanos detectados")
    private long countHumanDna;

    @JsonProperty("ratio")
    @Schema(description = "Ratio = count_mutant_dna / count_human_dna (double)")
    private double ratio;
}
