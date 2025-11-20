package org.example.dto;

import org.example.validation.ValidDnaSequence;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class DnaRequest {

    @NonNull
    @Schema(description = "Arreglo de strings que representa la matriz NxN de ADN.", example = "[\"ATGCGA\", \"CAGTGC\", \"TTATGT\", \"AGAAGG\", \"CCCCTA\", \"TCACTG\"]")
    @ValidDnaSequence
    private String[] dna;
}
