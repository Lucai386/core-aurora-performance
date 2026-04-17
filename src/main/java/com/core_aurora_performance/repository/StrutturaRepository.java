package com.core_aurora_performance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.Struttura;

@Repository
public interface StrutturaRepository extends JpaRepository<Struttura, Integer> {

    Optional<Struttura> findByNome(String nome);

    List<Struttura> findByCodiceIstatComune(String codiceIstatComune);

    List<Struttura> findByIdParent(Integer idParent);

    @Query("SELECT s FROM Struttura s " +
           "LEFT JOIN FETCH s.responsabile " +
           "LEFT JOIN FETCH s.staff st " +
           "LEFT JOIN FETCH st.user " +
           "WHERE s.codiceIstatComune = :codiceIstat " +
           "ORDER BY s.ordine ASC, s.id ASC")
    List<Struttura> findByCodiceIstatWithStaff(@Param("codiceIstat") String codiceIstat);

    /** Trova la struttura di cui un utente è responsabile */
    Optional<Struttura> findByResponsabileId(Integer responsabileId);

    /** Trova tutte le strutture di cui un utente è responsabile, ordinate per id_parent (radici prima) */
    @Query("SELECT s FROM Struttura s WHERE s.idResponsabile = :responsabileId ORDER BY s.idParent ASC NULLS FIRST, s.ordine ASC")
    List<Struttura> findAllByIdResponsabile(@Param("responsabileId") Integer responsabileId);
}
