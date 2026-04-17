package com.core_aurora_performance.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.core_aurora_performance.model.CodiceSequence;
import com.core_aurora_performance.model.CodiceSequence.EntityType;
import com.core_aurora_performance.repository.CodiceSequenceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodiceService {

    private final CodiceSequenceRepository sequenceRepository;

    @Transactional
    public String generateNextDupCodice(String codiceIstat) {
        return generateNextCodice(codiceIstat, EntityType.DUP, "DUP");
    }

    @Transactional
    public String generateNextProgettoCodice(String codiceIstat) {
        return generateNextCodice(codiceIstat, EntityType.PRJ, "PRJ");
    }

    @Transactional
    public String generateNextAttivitaCodice(String codiceIstat) {
        return generateNextCodice(codiceIstat, EntityType.ATT, "ATT");
    }

    @Transactional
    public String generateNextObiettivoCodice(String codiceIstat) {
        return generateNextCodice(codiceIstat, EntityType.OBJ, "OBJ");
    }

    private String generateNextCodice(String codiceIstat, EntityType entityType, String prefix) {
        CodiceSequence sequence = sequenceRepository
                .findByCodiceIstatAndEntityTypeForUpdate(codiceIstat, entityType)
                .orElseGet(() -> {
                    CodiceSequence newSeq = CodiceSequence.builder()
                            .codiceIstat(codiceIstat)
                            .entityType(entityType)
                            .lastNumber(0L)
                            .build();
                    return sequenceRepository.save(newSeq);
                });

        long nextNumber = sequence.getLastNumber() + 1;
        sequence.setLastNumber(nextNumber);
        sequenceRepository.save(sequence);

        String codice = String.format("%s%03d", prefix, nextNumber);
        log.debug("Generato codice {} per ente {} tipo {}", codice, codiceIstat, entityType);
        return codice;
    }
}
