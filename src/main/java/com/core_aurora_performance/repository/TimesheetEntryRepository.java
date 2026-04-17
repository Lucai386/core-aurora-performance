package com.core_aurora_performance.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.TimesheetEntry;

@Repository
public interface TimesheetEntryRepository extends JpaRepository<TimesheetEntry, Long> {

    List<TimesheetEntry> findByAttivitaIdOrderByDataDesc(Long attivitaId);

    List<TimesheetEntry> findByUtenteIdOrderByDataDesc(Integer utenteId);

    @Query("SELECT t FROM TimesheetEntry t " +
           "WHERE t.utenteId = :utenteId " +
           "AND t.data BETWEEN :dataInizio AND :dataFine " +
           "ORDER BY t.data DESC")
    List<TimesheetEntry> findByUtenteIdAndPeriodo(
        @Param("utenteId") Integer utenteId,
        @Param("dataInizio") LocalDate dataInizio,
        @Param("dataFine") LocalDate dataFine
    );

    @Query("SELECT t FROM TimesheetEntry t " +
           "WHERE t.attivitaId = :attivitaId " +
           "AND t.data BETWEEN :dataInizio AND :dataFine " +
           "ORDER BY t.data DESC")
    List<TimesheetEntry> findByAttivitaIdAndPeriodo(
        @Param("attivitaId") Long attivitaId,
        @Param("dataInizio") LocalDate dataInizio,
        @Param("dataFine") LocalDate dataFine
    );

    @Query("SELECT SUM(t.oreLavorate) FROM TimesheetEntry t WHERE t.attivitaId = :attivitaId")
    BigDecimal sumOreLavorateByAttivitaId(@Param("attivitaId") Long attivitaId);

    @Query("SELECT SUM(t.oreLavorate) FROM TimesheetEntry t " +
           "WHERE t.attivitaId = :attivitaId AND t.utenteId = :utenteId")
    BigDecimal sumOreLavorateByAttivitaIdAndUtenteId(
        @Param("attivitaId") Long attivitaId,
        @Param("utenteId") Integer utenteId
    );

    void deleteByAttivitaId(Long attivitaId);

    /** Trova tutte le entry di un utente */
    List<TimesheetEntry> findByUtenteId(Integer utenteId);

    /** Trova entry di un utente in un range di date */
    @Query("SELECT t FROM TimesheetEntry t WHERE t.utenteId = :utenteId AND t.data BETWEEN :dataInizio AND :dataFine")
    List<TimesheetEntry> findByUtenteIdAndDataBetween(
        @Param("utenteId") Integer utenteId,
        @Param("dataInizio") LocalDate dataInizio,
        @Param("dataFine") LocalDate dataFine
    );

    /** Trova entry di un utente in una data specifica */
    List<TimesheetEntry> findByUtenteIdAndData(Integer utenteId, LocalDate data);

    /** Trova tutte le entry in una data specifica */
    List<TimesheetEntry> findByData(LocalDate data);
}
