package com.core_aurora_performance.service;

import com.core_aurora_performance.model.Attivita;
import com.core_aurora_performance.model.AttivitaAssegnazione;
import com.core_aurora_performance.model.AttivitaStep;
import com.core_aurora_performance.model.TimesheetEntry;
import com.core_aurora_performance.repository.AttivitaAssegnazioneRepository;
import com.core_aurora_performance.repository.AttivitaRepository;
import com.core_aurora_performance.repository.AttivitaStepRepository;
import com.core_aurora_performance.repository.TimesheetEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttivitaCoreService {

    private final AttivitaRepository attivitaRepository;
    private final AttivitaAssegnazioneRepository assegnazioneRepository;
    private final AttivitaStepRepository stepRepository;
    private final TimesheetEntryRepository timesheetRepository;

    // ─── Attività ─────────────────────────────────────────────────────────────

    public List<Attivita> findAll() {
        return attivitaRepository.findAllWithDetails();
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
        timesheetRepository.deleteByAttivitaId(id);
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
                    .oreStimate(orig.getOreStimate())
                    .oreLavorate(BigDecimal.ZERO)
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
        return stepRepository.save(step);
    }

    @Transactional
    public Optional<AttivitaStep> toggleStep(Long stepId) {
        return stepRepository.findById(stepId).map(step -> {
            step.setCompletato(!Boolean.TRUE.equals(step.getCompletato()));
            return stepRepository.save(step);
        });
    }

    @Transactional
    public AttivitaStep updateStep(AttivitaStep step) {
        return stepRepository.save(step);
    }

    @Transactional
    public void removeStep(Long stepId) {
        stepRepository.deleteById(stepId);
    }

    // ─── Timesheet ────────────────────────────────────────────────────────────

    @Transactional
    public TimesheetEntry logOre(TimesheetEntry entry) {
        TimesheetEntry saved = timesheetRepository.save(entry);

        // Aggiorna le ore lavorate nella assegnazione
        assegnazioneRepository.findByAttivitaIdAndUtenteId(entry.getAttivitaId(), entry.getUtenteId())
                .ifPresent(ass -> {
                    BigDecimal nuoveOre = (ass.getOreLavorate() != null ? ass.getOreLavorate() : BigDecimal.ZERO)
                            .add(entry.getOreLavorate());
                    ass.setOreLavorate(nuoveOre);
                    assegnazioneRepository.save(ass);
                });

        return saved;
    }

    public List<TimesheetEntry> findTimesheetByAttivita(Long attivitaId) {
        return timesheetRepository.findByAttivitaIdOrderByDataDesc(attivitaId);
    }

    public List<TimesheetEntry> findTimesheetByUtente(Long utenteId, LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            return timesheetRepository.findByUtenteIdAndDataBetween(utenteId.intValue(), from, to);
        }
        return timesheetRepository.findByUtenteId(utenteId.intValue());
    }

    @Transactional
    public void deleteTimesheetEntry(Long id) {
        timesheetRepository.deleteById(id);
    }
}
