package com.core_aurora_performance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.AttivitaStep;

@Repository
public interface AttivitaStepRepository extends JpaRepository<AttivitaStep, Long> {

    List<AttivitaStep> findByAttivitaIdOrderByOrdineAsc(Long attivitaId);

    long countByAttivitaId(Long attivitaId);

    long countByAttivitaIdAndCompletatoTrue(Long attivitaId);

    void deleteByAttivitaId(Long attivitaId);
}
