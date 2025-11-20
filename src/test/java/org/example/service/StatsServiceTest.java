package org.example.service;

import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    @DisplayName("RATIO 1: Cálculo normal (Mutantes < Humanos)")
    void testRatioNormal() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(100L);

        StatsResponse stats = statsService.getStats();

        assertEquals(40, stats.getCountMutantDna());
        assertEquals(100, stats.getCountHumanDna());
        assertEquals(0.4, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("RATIO 2: Mutantes = Humanos (Ratio = 1.0)")
    void testRatioEqualCounts() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(50L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(50L);

        StatsResponse stats = statsService.getStats();

        assertEquals(1.0, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("RATIO 3: Mutantes > Humanos (Ratio > 1.0)")
    void testRatioMutantGreaterThanHuman() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(100L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(40L);

        StatsResponse stats = statsService.getStats();

        assertEquals(2.5, stats.getRatio(), 0.001);
    }


    @Test
    @DisplayName("BORDE 1: Cero Humanos (Mutantes > 0)")
    void testRatioZeroHumans() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(10L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        StatsResponse stats = statsService.getStats();

        assertEquals(10, stats.getCountMutantDna());
        assertEquals(0, stats.getCountHumanDna());
        assertEquals(10.0, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("BORDE 2: Cero Humanos y Cero Mutantes (BD Vacía)")
    void testRatioNoData() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(0L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        StatsResponse stats = statsService.getStats();

        assertEquals(0, stats.getCountMutantDna());
        assertEquals(0, stats.getCountHumanDna());
        assertEquals(0.0, stats.getRatio(), 0.001);
    }

    @Test
    @DisplayName("RATIO 4: Cálculo con decimales largos (1/3)")
    void testRatioDecimalCalculations() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(1L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(3L);

        StatsResponse stats = statsService.getStats();

        assertEquals(0.3333333333333333, stats.getRatio(), 0.000000000000001);
    }
}
