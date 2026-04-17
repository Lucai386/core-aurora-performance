package com.core_aurora_performance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.LpmNote;

@Repository
public interface LpmNoteRepository extends JpaRepository<LpmNote, Long> {

    List<LpmNote> findByLpmIdOrderByCreatedAtDesc(Long lpmId);
}
