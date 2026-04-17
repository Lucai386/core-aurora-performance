package com.core_aurora_performance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.Dup;

@Repository
public interface DupRepository extends JpaRepository<Dup, Long> {

    List<Dup> findByCodiceIstatOrderByAnnoDesc(String codiceIstat);

    List<Dup> findByCodiceIstatAndAnnoOrderBySezione(String codiceIstat, Integer anno);

    @Query("SELECT d FROM Dup d WHERE d.codiceIstat = :codiceIstat AND d.anno = :anno AND d.sezione = :sezione")
    List<Dup> findByCodiceIstatAndAnnoAndSezione(
            @Param("codiceIstat") String codiceIstat,
            @Param("anno") Integer anno,
            @Param("sezione") Dup.Sezione sezione);
}
