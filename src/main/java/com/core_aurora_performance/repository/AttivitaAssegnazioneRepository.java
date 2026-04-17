package com.core_aurora_performance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.AttivitaAssegnazione;

@Repository
public interface AttivitaAssegnazioneRepository extends JpaRepository<AttivitaAssegnazione, Long> {

    List<AttivitaAssegnazione> findByAttivitaId(Long attivitaId);

    List<AttivitaAssegnazione> findByUtenteId(Integer utenteId);

    Optional<AttivitaAssegnazione> findByAttivitaIdAndUtenteId(Long attivitaId, Integer utenteId);

    void deleteByAttivitaId(Long attivitaId);

    @Query("SELECT a FROM AttivitaAssegnazione a " +
           "LEFT JOIN FETCH a.utente " +
           "WHERE a.attivitaId = :attivitaId")
    List<AttivitaAssegnazione> findByAttivitaIdWithUtente(@Param("attivitaId") Long attivitaId);

    boolean existsByAttivitaIdAndUtenteId(Long attivitaId, Integer utenteId);

    void deleteByAttivitaIdAndUtenteId(Long attivitaId, Integer utenteId);
}
