package com.core_aurora_performance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.ObiettivoProgressivo;

@Repository
public interface ObiettivoProgressivoRepository extends JpaRepository<ObiettivoProgressivo, Long> {

    List<ObiettivoProgressivo> findByObiettivoIdOrderByDataRegistrazioneDesc(Long obiettivoId);

    @Query("SELECT op FROM ObiettivoProgressivo op " +
           "LEFT JOIN FETCH op.registratoDa " +
           "WHERE op.obiettivoId = :obiettivoId " +
           "ORDER BY op.dataRegistrazione DESC")
    List<ObiettivoProgressivo> findByObiettivoIdWithDetails(@Param("obiettivoId") Long obiettivoId);

    /** Ultimo progressivo registrato per un obiettivo */
    @Query("SELECT op FROM ObiettivoProgressivo op " +
           "WHERE op.obiettivoId = :obiettivoId " +
           "ORDER BY op.dataRegistrazione DESC LIMIT 1")
    ObiettivoProgressivo findLatestByObiettivoId(@Param("obiettivoId") Long obiettivoId);

    void deleteByObiettivoId(Long obiettivoId);
}
