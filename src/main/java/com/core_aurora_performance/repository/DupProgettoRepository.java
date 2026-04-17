package com.core_aurora_performance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.DupProgetto;

@Repository
public interface DupProgettoRepository extends JpaRepository<DupProgetto, Long> {

    List<DupProgetto> findByDupIdOrderByOrdine(Long dupId);

    List<DupProgetto> findByLpmId(Long lpmId);

    void deleteByDupId(Long dupId);

    @Query("SELECT p FROM DupProgetto p LEFT JOIN FETCH p.lpm LEFT JOIN FETCH p.responsabile LEFT JOIN FETCH p.struttura WHERE p.dupId = :dupId ORDER BY p.ordine")
    List<DupProgetto> findByDupIdWithDetails(@Param("dupId") Long dupId);

    @Query("SELECT p FROM DupProgetto p LEFT JOIN FETCH p.lpm LEFT JOIN FETCH p.responsabile LEFT JOIN FETCH p.struttura WHERE p.id = :id")
    DupProgetto findByIdWithDetails(@Param("id") Long id);

    /** Conta tutti i progetti per generare codice autoincrementale */
    @Query("SELECT COUNT(p) FROM DupProgetto p")
    long countAll();

    boolean existsByDupId(Long dupId);
}
