package com.core_aurora_performance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.Lpm;

@Repository
public interface LpmRepository extends JpaRepository<Lpm, Long> {

    @Query("SELECT l FROM Lpm l WHERE l.deletedAt IS NULL ORDER BY l.priorita DESC, l.createdAt DESC")
    List<Lpm> findAllActive();

    @Query("SELECT l FROM Lpm l WHERE l.id = :id AND l.deletedAt IS NULL")
    Optional<Lpm> findByIdActive(@Param("id") Long id);

    @Query("SELECT l FROM Lpm l WHERE l.codiceIstat = :codiceIstat AND l.deletedAt IS NULL ORDER BY l.priorita DESC")
    List<Lpm> findByCodiceIstatActive(@Param("codiceIstat") String codiceIstat);

    @Query("SELECT l FROM Lpm l WHERE l.codiceIstat = :codiceIstat AND l.annoInizioMandato = :annoInizio AND l.annoFineMandato = :annoFine AND l.deletedAt IS NULL ORDER BY l.priorita DESC")
    List<Lpm> findByMandato(@Param("codiceIstat") String codiceIstat, @Param("annoInizio") Integer annoInizio, @Param("annoFine") Integer annoFine);

    @Query("SELECT l FROM Lpm l WHERE l.stato = :stato AND l.deletedAt IS NULL")
    List<Lpm> findByStatoActive(@Param("stato") String stato);
}
