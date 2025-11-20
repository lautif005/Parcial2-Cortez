package org.example.controller;

import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @Operation(summary = "Verifica si una secuencia de ADN pertenece a un mutante",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Es Mutante"),
                    @ApiResponse(responseCode = "403", description = "Es Humano (No Mutante)", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Secuencia de ADN inválida")
            })
    @PostMapping("/mutant")
    public ResponseEntity<Void> checkMutant(@RequestBody @Valid DnaRequest request) {

        boolean isMutant = mutantService.isMutant(request.getDna());

        if (isMutant) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Obtiene las estadísticas de ADN analizado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente")
            })
    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }
}
