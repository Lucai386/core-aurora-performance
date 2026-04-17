package com.core_aurora_performance.service;

import com.core_aurora_performance.model.Dup;
import com.core_aurora_performance.model.DupProgetto;
import com.core_aurora_performance.model.Lpm;
import com.core_aurora_performance.repository.DupProgettoRepository;
import com.core_aurora_performance.repository.DupRepository;
import com.core_aurora_performance.repository.LpmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DupCoreService {

    private final DupRepository dupRepository;
    private final DupProgettoRepository progettoRepository;
    private final LpmRepository lpmRepository;

    public List<Dup> findByCodiceIstat(String codiceIstat) {
        return dupRepository.findByCodiceIstatOrderByAnnoDesc(codiceIstat);
    }

    public Optional<Dup> findById(Long id) {
        return dupRepository.findById(id);
    }

    @Transactional
    public Dup save(Dup dup) {
        return dupRepository.save(dup);
    }

    @Transactional
    public void delete(Long id) {
        dupRepository.deleteById(id);
    }

    public boolean hasProgetti(Long dupId) {
        return progettoRepository.existsByDupId(dupId);
    }

    // ─── Progetti di un DUP ───────────────────────────────────────────────────

    public List<DupProgetto> findProgettiByDupId(Long dupId) {
        return progettoRepository.findByDupIdWithDetails(dupId);
    }

    public Optional<DupProgetto> findProgettoById(Long id) {
        return progettoRepository.findById(id);
    }

    @Transactional
    public DupProgetto saveProgetto(DupProgetto progetto) {
        return progettoRepository.save(progetto);
    }

    @Transactional
    public void deleteProgetto(Long id) {
        progettoRepository.deleteById(id);
    }

    @Transactional
    public boolean linkLpm(Long progettoId, Long lpmId) {
        Optional<DupProgetto> progettoOpt = progettoRepository.findById(progettoId);
        Optional<Lpm> lpmOpt = lpmRepository.findById(lpmId);
        if (progettoOpt.isEmpty() || lpmOpt.isEmpty()) return false;
        DupProgetto progetto = progettoOpt.get();
        progetto.setLpmId(lpmId);
        progettoRepository.save(progetto);
        return true;
    }

    @Transactional
    public boolean unlinkLpm(Long progettoId) {
        Optional<DupProgetto> progettoOpt = progettoRepository.findById(progettoId);
        if (progettoOpt.isEmpty()) return false;
        DupProgetto progetto = progettoOpt.get();
        progetto.setLpmId(null);
        progettoRepository.save(progetto);
        return true;
    }
}
