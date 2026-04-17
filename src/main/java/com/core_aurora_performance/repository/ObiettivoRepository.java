package com.core_aurora_performance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.Obiettivo;

@Repository
public interface ObiettivoRepository extends JpaRepository<Obiettivo, Long> {

    List<Obiettivo> findByCodiceIstatOrderByAnnoDescCreatedAtDesc(String codiceIstat);

    List<Obiettivo> findByCodiceIstatAndAnnoOrderByCreatedAtDesc(String codiceIstat, Integer anno);

    @Query("SELECT o FROM Obiettivo o " +
           "LEFT JOIN FETCH o.utenteAssegnato " +
           "LEFT JOIN FETCH o.creatoDa " +
           "LEFT JOIN FETCH o.struttura " +
           "WHERE o.codiceIstat = :codiceIstat " +
           "ORDER BY o.anno DESC, o.createdAt DESC")
    List<Obiettivo> findByCodiceIstatWithDetails(@Param("codiceIstat") String codiceIstat);

    @Query("SELECT o FROM Obiettivo o " +
           "LEFT JOIN FETCH o.utenteAssegnato " +
           "LEFT JOIN FETCH o.creatoDa " +
           "LEFT JOIN FETCH o.struttura " +
           "WHERE o.codiceIstat = :codiceIstat AND o.anno = :anno " +
           "ORDER BY o.createdAt DESC")
    List<Obiettivo> findByCodiceIstatAndAnnoWithDetails(@Param("codiceIstat") String codiceIstat, @Param("anno") Integer anno);

    /** Obiettivi assegnati a un utente specifico */
    @Query("SELECT o FROM Obiettivo o " +
           "LEFT JOIN FETCH o.creatoDa " +
           "LEFT JOIN FETCH o.struttura " +
           "WHERE o.utenteAssegnatoId = :userId " +
           "ORDER BY o.anno DESC, o.createdAt DESC")
    List<Obiettivo> findByUtenteAssegnatoId(@Param("userId") Long userId);

    /** Obiettivi creati da un utente specifico */
    List<Obiettivo> findByCreatoDaIdOrderByAnnoDescCreatedAtDesc(Long creatoDaId);

    /** Obiettivi per struttura */
    @Query("SELECT o FROM Obiettivo o " +
           "LEFT JOIN FETCH o.utenteAssegnato " +
           "LEFT JOIN FETCH o.creatoDa " +
           "WHERE o.strutturaId = :strutturaId " +
           "ORDER BY o.anno DESC, o.createdAt DESC")
    List<Obiettivo> findByStrutturaId(@Param("strutturaId") Integer strutturaId);

    /** Conteggi per dashboard */
    @Query("SELECT COUNT(o) FROM Obiettivo o WHERE o.codiceIstat = :codiceIstat")
    Long countByCodiceIstat(@Param("codiceIstat") String codiceIstat);

    @Query("SELECT COUNT(o) FROM Obiettivo o WHERE o.codiceIstat = :codiceIstat AND o.stato = 'ATTIVO'")
    Long countAttiviByCodiceIstat(@Param("codiceIstat") String codiceIstat);

    @Query("SELECT COUNT(o) FROM Obiettivo o WHERE o.codiceIstat = :codiceIstat AND o.stato = 'COMPLETATO'")
    Long countCompletatiByCodiceIstat(@Param("codiceIstat") String codiceIstat);
    
    /** Count globale per stato */
    Long countByStato(Obiettivo.StatoObiettivo stato);
}
