package org.example.service;

import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @Mock
    private MutantDetector mutantDetector;

    @InjectMocks
    private MutantService mutantService;

    private final String[] mutantDna = {"AAAA", "AAAA", "AAAA", "AAAA"};
    private final String[] humanDna = {"ATGC", "CAGT", "TTAT", "AGAC"};


    @Test
    @DisplayName("CACHÉ 1: Debe retornar resultado cacheado y NO llamar al Detector")
    void testReturnCachedResult() {
        DnaRecord cachedRecord = new DnaRecord("somehash", true);
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(cachedRecord));

        boolean result = mutantService.isMutant(mutantDna);

        assertTrue(result, "Debería retornar TRUE desde la caché");
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
        verify(dnaRecordRepository, times(1)).findByDnaHash(anyString());
    }

    @Test
    @DisplayName("GUARDADO 1: Debe analizar ADN Mutante, guardarlo, y retornar TRUE")
    void testAnalyzeAndSaveMutantDna() {
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(mutantDna)).thenReturn(true);

        boolean result = mutantService.isMutant(mutantDna);

        assertTrue(result, "Debería ser mutante");
        verify(mutantDetector, times(1)).isMutant(mutantDna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("GUARDADO 2: Debe analizar ADN Humano, guardarlo, y retornar FALSE")
    void testAnalyzeAndSaveHumanDna() {
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(humanDna)).thenReturn(false);

        boolean result = mutantService.isMutant(humanDna);

        assertFalse(result, "Debería ser humano");
        verify(mutantDetector, times(1)).isMutant(humanDna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }


    @Test
    @DisplayName("DEDUPLICACIÓN: Debe generar Hash consistente para el mismo ADN")
    void testConsistentHashGenerationAndDeduplication() {
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any())).thenReturn(true);

        mutantService.isMutant(mutantDna);
        mutantService.isMutant(mutantDna);

        verify(mutantDetector, times(2)).isMutant(mutantDna);
        verify(dnaRecordRepository, times(2)).findByDnaHash(anyString());
        verify(dnaRecordRepository, times(2)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("EXCEPCIÓN 1: Debe propagar DnaHashCalculationException")
    void testHashCalculationExceptionIsPropagated() {
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(humanDna)).thenReturn(false);

        assertDoesNotThrow(() -> mutantService.isMutant(humanDna));
    }

    @Test
    @DisplayName("CACHÉ 2: Debe retornar FALSE si está cacheado como humano")
    void testReturnCachedResultForHuman() {
        DnaRecord cachedRecord = new DnaRecord("somehash", false);
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(cachedRecord));

        boolean result = mutantService.isMutant(humanDna);

        assertFalse(result, "Debería retornar FALSE desde la caché");
        verify(mutantDetector, never()).isMutant(any());
    }
}