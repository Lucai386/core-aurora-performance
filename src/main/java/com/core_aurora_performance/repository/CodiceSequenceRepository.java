package com.core_aurora_performance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.CodiceSequence;
import com.core_aurora_performance.model.CodiceSequence.EntityType;

import jakarta.persistence.LockModeType;

@Repository
public interface CodiceSequenceRepository extends JpaRepository<CodiceSequence, Long> {

    /**
     * Trova la sequenza per ente e tipo entità con lock pessimistico.
     * Usato per garantire l'atomicità nell'incremento del codice.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM CodiceSequence s WHERE s.codiceIstat = :codiceIstat AND s.entityType = :entityType")
    Optional<CodiceSequence> findByCodiceIstatAndEntityTypeForUpdate(
            @Param("codiceIstat") String codiceIstat,
            @Param("entityType") EntityType entityType);

    Optional<CodiceSequence> findByCodiceIstatAndEntityType(String codiceIstat, EntityType entityType);
}
