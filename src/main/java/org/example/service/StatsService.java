package org.example.service;

import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository dnaRecordRepository;

    public StatsResponse getStats() {
        long countMutant = dnaRecordRepository.countByIsMutant(true); // Usando índice
        long countHuman = dnaRecordRepository.countByIsMutant(false); // Usando índice

        double ratio;

        if (countHuman == 0) {
            ratio = countMutant > 0 ? countMutant : 0.0;
        } else {
            ratio = (double) countMutant / countHuman;
        }

        return StatsResponse.builder()
                .countMutantDna(countMutant)
                .countHumanDna(countHuman)
                .ratio(ratio)
                .build();
    }
}
