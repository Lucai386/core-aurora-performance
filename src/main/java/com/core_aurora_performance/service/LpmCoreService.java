package com.core_aurora_performance.service;

import com.core_aurora_performance.model.Dup;
import com.core_aurora_performance.model.Lpm;
import com.core_aurora_performance.model.LpmNote;
import com.core_aurora_performance.repository.DupRepository;
import com.core_aurora_performance.repository.LpmNoteRepository;
import com.core_aurora_performance.repository.LpmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LpmCoreService {

    private final LpmRepository lpmRepository;
    private final LpmNoteRepository lpmNoteRepository;
    private final DupRepository dupRepository;

    public List<Lpm> findActiveByCodiceIstat(String codiceIstat) {
        return lpmRepository.findByCodiceIstatActive(codiceIstat);
    }

    public Optional<Lpm> findById(Long id) {
        return lpmRepository.findById(id);
    }

    @Transactional
    public Lpm save(Lpm lpm) {
        return lpmRepository.save(lpm);
    }

    @Transactional
    public boolean softDelete(Long id, Long deletedBy) {
        Optional<Lpm> opt = lpmRepository.findById(id);
        if (opt.isEmpty()) return false;
        Lpm lpm = opt.get();
        lpm.setDeletedAt(LocalDateTime.now());
        lpm.setDeletedBy(deletedBy.intValue());
        lpmRepository.save(lpm);
        return true;
    }

    @Transactional
    public boolean linkDup(Long lpmId, Long dupId) {
        Optional<Lpm> lpmOpt = lpmRepository.findById(lpmId);
        Optional<Dup> dupOpt = dupRepository.findById(dupId);
        if (lpmOpt.isEmpty() || dupOpt.isEmpty()) return false;
        Lpm lpm = lpmOpt.get();
        lpm.setDupId(dupId);
        lpmRepository.save(lpm);
        return true;
    }

    @Transactional
    public boolean unlinkDup(Long lpmId) {
        Optional<Lpm> lpmOpt = lpmRepository.findById(lpmId);
        if (lpmOpt.isEmpty()) return false;
        Lpm lpm = lpmOpt.get();
        lpm.setDupId(null);
        lpmRepository.save(lpm);
        return true;
    }

    public List<LpmNote> findNotesByLpmId(Long lpmId) {
        return lpmNoteRepository.findByLpmIdOrderByCreatedAtDesc(lpmId);
    }

    @Transactional
    public LpmNote saveNote(LpmNote note) {
        return lpmNoteRepository.save(note);
    }
}
