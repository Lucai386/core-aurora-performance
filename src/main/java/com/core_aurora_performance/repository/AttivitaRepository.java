package com.core_aurora_performance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.Attivita;

@Repository
public interface AttivitaRepository extends JpaRepository<Attivita, Long> {

    List<Attivita> findByProgettoIdOrderByOrdine(Long progettoId);

    @Query("SELECT a FROM Attivita a " +
           "LEFT JOIN FETCH a.progetto p " +
           "LEFT JOIN FETCH a.assegnazioni ass " +
           "LEFT JOIN FETCH ass.utente " +
           "ORDER BY a.ordine")
    List<Attivita> findAllWithDetails();

    @Query("SELECT a FROM Attivita a " +
           "LEFT JOIN FETCH a.progetto p " +
           "LEFT JOIN FETCH a.assegnazioni ass " +
           "LEFT JOIN FETCH ass.utente " +
           "WHERE a.progettoId = :progettoId " +
           "ORDER BY a.ordine")
    List<Attivita> findByProgettoIdWithDetails(@Param("progettoId") Long progettoId);

    @Query("SELECT a FROM Attivita a " +
           "LEFT JOIN FETCH a.progetto p " +
           "LEFT JOIN FETCH a.assegnazioni ass " +
           "LEFT JOIN FETCH ass.utente " +
           "WHERE a.id = :id")
    Attivita findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT a FROM Attivita a " +
           "LEFT JOIN FETCH a.assegnazioni ass " +
           "WHERE ass.utenteId = :utenteId " +
           "ORDER BY a.ordine")
    List<Attivita> findByUtenteId(@Param("utenteId") Long utenteId);

    void deleteByProgettoId(Long progettoId);

    /** Trova attività per struttura */
    List<Attivita> findByStrutturaId(Integer strutturaId);

    /** Conta tutte le attività per generare codice autoincrementale */
    @Query("SELECT COUNT(a) FROM Attivita a")
    long countAll();
}
