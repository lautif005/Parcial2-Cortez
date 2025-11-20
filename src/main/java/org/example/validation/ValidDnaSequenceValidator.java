package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    private static final int MIN_SIZE = 4;
    private static final String VALID_CHARS = "ATCG";

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        if (dna == null || dna.length == 0 || dna.length < MIN_SIZE) {
            return false;
        }

        final int N = dna.length;

        for (String row : dna) {
            if (row == null || row.length() != N) {
                return false;
            }

            for (char c : row.toCharArray()) {
                if (VALID_CHARS.indexOf(c) == -1) {
                    return false;
                }
            }
        }

        return true;
    }
}
