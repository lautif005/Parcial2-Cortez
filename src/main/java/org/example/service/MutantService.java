package org.example.service;

import org.example.entity.DnaRecord;
import org.example.exception.DnaHashCalculationException;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Genera constructor con final fields (DI)
public class MutantService {

    private final DnaRecordRepository dnaRecordRepository;
    private final MutantDetector mutantDetector;

    public boolean isMutant(String[] dna) {
        String dnaHash = calculateDnaHash(dna);

        Optional<DnaRecord> cachedResult = dnaRecordRepository.findByDnaHash(dnaHash);

        if (cachedResult.isPresent()) {
            return cachedResult.get().isMutant();
        }

        boolean isMutant = mutantDetector.isMutant(dna);

        DnaRecord newRecord = new DnaRecord(dnaHash, isMutant);
        dnaRecordRepository.save(newRecord);

        return isMutant;
    }

    private String calculateDnaHash(String[] dna) {
        String dnaString = String.join("", dna);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dnaString.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new DnaHashCalculationException("Error calculating SHA-256 hash for DNA sequence", e);
        }
    }
}
