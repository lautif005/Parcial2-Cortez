package org.example.service;

import org.springframework.stereotype.Service;

@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;

    public boolean isMutant(String[] dna) {
        if (!isValidDnaMatrix(dna)) {
            return false;
        }

        final int N = dna.length;
        char[][] matrix = convertToCharMatrix(dna, N);

        int sequenceCount = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                if (j <= N - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, i, j)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                if (i <= N - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, i, j)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                if (i <= N - SEQUENCE_LENGTH && j <= N - SEQUENCE_LENGTH) {
                    if (checkDiagonalDescending(matrix, i, j)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                if (i >= SEQUENCE_LENGTH - 1 && j <= N - SEQUENCE_LENGTH) {
                    if (checkDiagonalAscending(matrix, i, j)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }
            }
        }

        return false;
    }

    private char[][] convertToCharMatrix(String[] dna, int N) {
        char[][] matrix = new char[N][];
        for (int i = 0; i < N; i++) {
            matrix[i] = dna[i].toCharArray();
        }
        return matrix;
    }

    private boolean isValidDnaMatrix(String[] dna) {
        if (dna == null || dna.length < SEQUENCE_LENGTH) return false;
        final int N = dna.length;
        final String VALID_CHARS = "ATCG";

        for (String row : dna) {
            if (row == null || row.length() != N) return false;
            for (char c : row.toCharArray()) {
                if (VALID_CHARS.indexOf(c) == -1) return false;
            }
        }
        return true;
    }

    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
                matrix[row][col + 2] == base &&
                matrix[row][col + 3] == base;
    }

    private boolean checkVertical(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col] == base &&
                matrix[row + 2][col] == base &&
                matrix[row + 3][col] == base;
    }

    private boolean checkDiagonalDescending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col + 1] == base &&
                matrix[row + 2][col + 2] == base &&
                matrix[row + 3][col + 3] == base;
    }

    private boolean checkDiagonalAscending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row - 1][col + 1] == base &&
                matrix[row - 2][col + 2] == base &&
                matrix[row - 3][col + 3] == base;
    }
}
