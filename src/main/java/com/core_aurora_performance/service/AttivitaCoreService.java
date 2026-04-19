package com.core_aurora_performance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.core_aurora_performance.model.Attivita;
import com.core_aurora_performance.model.AttivitaAssegnazione;
import com.core_aurora_performance.model.AttivitaStep;
import com.core_aurora_performance.model.DupProgetto;
import com.core_aurora_performance.repository.AttivitaAssegnazioneRepository;
import com.core_aurora_performance.repository.AttivitaRepository;
import com.core_aurora_performance.repository.AttivitaStepRepository;
import com.core_aurora_performance.repository.DupProgettoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttivitaCoreService {

    private final AttivitaRepository attivitaRepository;
    private final AttivitaAssegnazioneRepository assegnazioneRepository;
    private final AttivitaStepRepository stepRepository;
    private final DupProgettoRepository progettoRepository;

    // ─── Attività ─────────────────────────────────────────────────────────────

    public List<Attivita> findAll() {
        return attivitaRepository.findAllWithDetails();
    }

    public List<Attivita> findByCodiceIstat(String codiceIstat) {
        return attivitaRepository.findByCodiceIstatWithDetails(codiceIstat);
    }

    public List<Attivita> findByProgettoId(Long progettoId) {
        return attivitaRepository.findByProgettoIdWithDetails(progettoId);
    }

    public List<Attivita> findByUtenteId(Long utenteId) {
        return attivitaRepository.findByUtenteId(utenteId);
    }

    public List<Attivita> findByStrutturaId(Long strutturaId) {
        return attivitaRepository.findByStrutturaId(strutturaId.intValue());
    }

    public Optional<Attivita> findById(Long id) {
        return attivitaRepository.findById(id);
    }

    @Transactional
    public Attivita save(Attivita attivita) {
        return attivitaRepository.save(attivita);
    }

    @Transactional
    public void delete(Long id) {
        assegnazioneRepository.deleteByAttivitaId(id);
        stepRepository.deleteByAttivitaId(id);
        attivitaRepository.deleteById(id);
    }

    @Transactional
    public Attivita duplicate(Long sourceId) {
        Attivita source = attivitaRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Attivita not found: " + sourceId));

        Attivita copy = Attivita.builder()
                .progettoId(source.getProgettoId())
                .codice(source.getCodice() + "-COPY")
                .titolo(source.getTitolo() + " (copia)")
                .descrizione(source.getDescrizione())
                .stato(Attivita.Stato.TODO)
                .priorita(source.getPriorita())
                .strutturaId(source.getStrutturaId())
                .percentualeCompletamento(0)
                .build();
        Attivita saved = attivitaRepository.save(copy);

        // Copia le assegnazioni
        List<AttivitaAssegnazione> origAssegnazioni = assegnazioneRepository.findByAttivitaId(sourceId);
        for (AttivitaAssegnazione orig : origAssegnazioni) {
            AttivitaAssegnazione newAss = AttivitaAssegnazione.builder()
                    .attivitaId(saved.getId())
                    .utenteId(orig.getUtenteId())
                    .ruolo(orig.getRuolo())
                    .dataInizio(orig.getDataInizio())
                    .dataFine(orig.getDataFine())
                    .note(orig.getNote())
                    .build();
            assegnazioneRepository.save(newAss);
        }

        return saved;
    }

    // ─── Assegnazioni ─────────────────────────────────────────────────────────

    @Transactional
    public AttivitaAssegnazione addAssegnazione(AttivitaAssegnazione assegnazione) {
        return assegnazioneRepository.save(assegnazione);
    }

    @Transactional
    public void removeAssegnazione(Long attivitaId, Long utenteId) {
        assegnazioneRepository.deleteByAttivitaIdAndUtenteId(attivitaId, utenteId.intValue());
    }

    public Optional<AttivitaAssegnazione> findAssegnazione(Long attivitaId, Long utenteId) {
        return assegnazioneRepository.findByAttivitaIdAndUtenteId(attivitaId, utenteId.intValue());
    }

    @Transactional
    public Attivita updatePercentuale(Long id, BigDecimal percentuale) {
        Attivita attivita = attivitaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attivita not found: " + id));
        attivita.setPercentualeCompletamento(percentuale.intValue());
        return attivitaRepository.save(attivita);
    }

    // ─── Steps ────────────────────────────────────────────────────────────────

    @Transactional
    public AttivitaStep addStep(AttivitaStep step) {
        AttivitaStep saved = stepRepository.save(step);
        updatePercentualeFromSteps(step.getAttivitaId());
        return saved;
    }

    @Transactional
    public Optional<AttivitaStep> toggleStep(Long stepId) {
        return stepRepository.findById(stepId).map(step -> {
            step.setCompletato(!Boolean.TRUE.equals(step.getCompletato()));
            AttivitaStep saved = stepRepository.save(step);
            updatePercentualeFromSteps(step.getAttivitaId());
            return saved;
        });
    }

    @Transactional
    public AttivitaStep updateStep(AttivitaStep step) {
        return stepRepository.save(step);
    }

    @Transactional
    public void removeStep(Long stepId) {
        stepRepository.findById(stepId).ifPresent(step -> {
            Long attivitaId = step.getAttivitaId();
            stepRepository.deleteById(stepId);
            updatePercentualeFromSteps(attivitaId);
        });
    }

    // ─── Auto-update percentuale & stato ──────────────────────────────────────

    private void updatePercentualeFromSteps(Long attivitaId) {
        List<AttivitaStep> steps = stepRepository.findByAttivitaIdOrderByOrdineAsc(attivitaId);
        if (steps.isEmpty()) return;

        final int percentuale = Math.min(steps.stream()
            .filter(s -> Boolean.TRUE.equals(s.getCompletato()))
            .mapToInt(s -> s.getPeso() != null ? s.getPeso() : 0)
            .sum(), 100);

        attivitaRepository.findById(attivitaId).ifPresent(attivita -> {
            attivita.setPercentualeCompletamento(percentuale);

            if (percentuale == 100 && attivita.getStato() != Attivita.Stato.COMPLETATA) {
                attivita.setStato(Attivita.Stato.COMPLETATA);
                attivita.setDataFineEffettiva(LocalDate.now());
            } else if (percentuale > 0 && percentuale < 100
                    && (attivita.getStato() == null || attivita.getStato() == Attivita.Stato.TODO)) {
                attivita.setStato(Attivita.Stato.IN_CORSO);
            }

            attivitaRepository.save(attivita);

            if (attivita.getProgettoId() != null) {
                updateProgettoProgresso(attivita.getProgettoId());
            }
        });
    }

    private void updateProgettoProgresso(Long progettoId) {
        List<Attivita> attivita = attivitaRepository.findByProgettoIdOrderByOrdine(progettoId);
        if (attivita.isEmpty()) return;

        int sommaPesi = attivita.stream()
            .mapToInt(a -> a.getPeso() != null ? a.getPeso() : 100)
            .sum();
        if (sommaPesi == 0) return;

        int progressoPesato = attivita.stream()
            .mapToInt(a -> {
                int peso = a.getPeso() != null ? a.getPeso() : 100;
                int perc = a.getPercentualeCompletamento() != null ? a.getPercentualeCompletamento() : 0;
                return peso * perc;
            })
            .sum();

        int progresso = progressoPesato / sommaPesi;

        progettoRepository.findById(progettoId).ifPresent(progetto -> {
            progetto.setProgresso(progresso);

            if (progresso == 100 && progetto.getStato() != DupProgetto.Stato.COMPLETATO) {
                progetto.setStato(DupProgetto.Stato.COMPLETATO);
                progetto.setDataFine(LocalDate.now());
            } else if (progresso > 0 && progresso < 100 && progetto.getStato() == DupProgetto.Stato.TODO) {
                progetto.setStato(DupProgetto.Stato.IN_CORSO);
            }

            progettoRepository.save(progetto);
        });
    }
}
