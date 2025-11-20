package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    @Test
    @DisplayName("MUTANTE 1: 2 Secuencias Horizontales")
    void testMutantWithTwoHorizontalSequences() {
        String[] dna = {
                "ATGCGA",
                "CCCCGC",
                "TTATGT",
                "AGAAGG",
                "AAAATA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("MUTANTE 2: 2 Secuencias Verticales")
    void testMutantWithTwoVerticalSequences() {
        String[] dna = {
                "GTGCGA",
                "GTGTGC",
                "GTATGT",
                "GAAGGG",
                "CCCCCG",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("MUTANTE 3: 1 Horizontal y 1 Diagonal Descendente (↘)")
    void testMutantWithHorizontalAndDescendingDiagonal() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTAGAT",
                "AGAXGA",
                "CGCCTA",
                "TCCCTA"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("MUTANTE 4: 1 Horizontal y 1 Diagonal Ascendente (↗)")
    void testMutantWithHorizontalAndAscendingDiagonal() {
        String[] dna = {
                "GTAGTA", // 5 (A)
                "GTACTA", // 4 (A)
                "GTAGCA", // 3 (A)
                "GTACAA", // 2 (A)
                "CCCCCT", // Horizontal: CCCC
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("MUTANTE 5: Matriz de 4x4 (Mínimo Tamaño) - Diagonales")
    void testSmallestMatrixMutant() {
        String[] dna = {
                "GTGC", // C
                "CATT", // C
                "ATCA", // C
                "CAGA"  // C - Diagonal Ascendente: CCCC
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("MUTANTE 6: Matriz Grande 10x10 con Secuencias Lejanas")
    void testLargeMatrixMutant() {
        String[] largeDna = {
                "ATGCGAATGC",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AGAAAAATAA", // Seq 1: AAAA
                "CCCCTACCCC", // Seq 2: CCCC
                "TCACTGTCAC",
                "ATGCGAATGC",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AGAAGGATAA"
        };
        assertTrue(mutantDetector.isMutant(largeDna));
    }

    // --- CASOS HUMANOS (Debe retornar FALSE) ---

    @Test
    @DisplayName("HUMANO 1: Sin Secuencias")
    void testHumanWithNoSequences() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAG",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("HUMANO 2: Solo 1 Secuencia Horizontal (Caso Borde)")
    void testHumanWithExactlyOneSequence() {
        String[] dna = {
                "AAAAGA", // Solo 1 secuencia: AAAA
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCACTA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("HUMANO 3: Secuencia de 3 y 5 (No de 4)")
    void testHumanWithSequencesOfThreeAndFive() {
        String[] dna = {
                "AAAAAG", // Secuencia de 5
                "CAGTGC",
                "TTTGT", // Secuencia de 3
                "AGAAGG",
                "CCACTA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    // --- CASOS DE VALIDACIÓN Y RENDIMIENTO ---

    @Test
    @DisplayName("VALIDACIÓN 1: Matriz Nula")
    void testValidationNullDna() {
        assertFalse(mutantDetector.isMutant(null));
    }

    @Test
    @DisplayName("VALIDACIÓN 2: Matriz Vacía")
    void testValidationEmptyDna() {
        String[] dna = {};
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("VALIDACIÓN 3: Matriz No Cuadrada (6x5)")
    void testValidationNonSquareMatrix() {
        String[] dna = {
                "ATGCG", // 5
                "CAGTG", // 5
                "TTATG", // 5
                "AGAAG", // 5
                "CCCCT", // 5
                "TCACT"  // 5, pero son 6 filas. La validación falla antes.
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("VALIDACIÓN 4: Carácter Inválido 'X'")
    void testValidationInvalidCharacters() {
        String[] dna = {
                "ATGCGA",
                "CAGTXC", // 'X' inválido
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("VALIDACIÓN 5: Matriz Menor a 4x4 (3x3)")
    void testValidationTooSmallMatrix() {
        String[] dna = {
                "ATG",
                "CAG",
                "TTA"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("VALIDACIÓN 6: Fila Nula Dentro del Array")
    void testValidationNullRow() {
        String[] dna = {
                "ATGC",
                "CAGT",
                null, // Fila nula
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("OPTIMIZACIÓN: Early Termination (Tiempo)")
    void testEarlyTerminationIsUsed() {
        // Matriz muy grande 100x100
        String[] largeMutantDna = new String[100];
        String row = "AAAAAAAA" + "T".repeat(92); // Genera la primera secuencia de 8 A's
        for (int i = 0; i < 100; i++) {
            largeMutantDna[i] = row; // Cada fila tiene al menos 2 secuencias (8 A's = 5 secuencias de 4)
        }

        long startTime = System.nanoTime();
        mutantDetector.isMutant(largeMutantDna);
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Si la terminación temprana funciona, el tiempo debe ser muy bajo,
        // ya que solo revisa las primeras filas (Objetivo: < 10ms)
        assertTrue(durationMs < 10, "El algoritmo no terminó temprano. Duración: " + durationMs + "ms");
    }
}