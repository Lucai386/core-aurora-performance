package com.core_aurora_performance.service;

import com.core_aurora_performance.model.Obiettivo;
import com.core_aurora_performance.model.ObiettivoProgressivo;
import com.core_aurora_performance.repository.ObiettivoProgressivoRepository;
import com.core_aurora_performance.repository.ObiettivoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ObiettivoCoreService {

    private final ObiettivoRepository obiettivoRepository;
    private final ObiettivoProgressivoRepository progressivoRepository;

    public List<Obiettivo> findByCodiceIstat(String codiceIstat) {
        return obiettivoRepository.findByCodiceIstatWithDetails(codiceIstat);
    }

    public List<Obiettivo> findByUtenteId(Long utenteId) {
        return obiettivoRepository.findByUtenteAssegnatoId(utenteId);
    }

    public Optional<Obiettivo> findById(Long id) {
        return obiettivoRepository.findById(id);
    }

    @Transactional
    public Obiettivo save(Obiettivo obiettivo) {
        return obiettivoRepository.save(obiettivo);
    }

    @Transactional
    public void delete(Long id) {
        progressivoRepository.deleteByObiettivoId(id);
        obiettivoRepository.deleteById(id);
    }

    @Transactional
    public ObiettivoProgressivo registraProgressivo(Long obiettivoId, BigDecimal valore, Long registratoId) {
        Obiettivo obiettivo = obiettivoRepository.findById(obiettivoId)
                .orElseThrow(() -> new IllegalArgumentException("Obiettivo not found: " + obiettivoId));

        // Aggiorna il valore corrente
        obiettivo.setValoreCorrente(valore);
        // Verifica se completato
        if (obiettivo.getTipo() != null) {
            boolean completato = switch (obiettivo.getTipo()) {
                case CRESCENTE -> valore.compareTo(obiettivo.getValoreTarget()) >= 0;
                case DECRESCENTE -> valore.compareTo(obiettivo.getValoreTarget()) <= 0;
            };
            if (completato) {
                obiettivo.setStato(Obiettivo.StatoObiettivo.COMPLETATO);
            }
        }
        obiettivoRepository.save(obiettivo);

        ObiettivoProgressivo progressivo = ObiettivoProgressivo.builder()
                .obiettivoId(obiettivoId)
                .valoreRegistrato(valore)
                .dataRegistrazione(LocalDateTime.now())
                .registratoDaId(registratoId)
                .build();
        return progressivoRepository.save(progressivo);
    }

    public long countTotale(String codiceIstat) {
        return obiettivoRepository.countByCodiceIstat(codiceIstat);
    }

    public long countAttivi(String codiceIstat) {
        return obiettivoRepository.countAttiviByCodiceIstat(codiceIstat);
    }

    public long countCompletati(String codiceIstat) {
        return obiettivoRepository.countCompletatiByCodiceIstat(codiceIstat);
    }
}
