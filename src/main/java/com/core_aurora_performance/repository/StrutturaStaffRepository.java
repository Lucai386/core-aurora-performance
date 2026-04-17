package com.core_aurora_performance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.StrutturaStaff;

@Repository
public interface StrutturaStaffRepository extends JpaRepository<StrutturaStaff, Integer> {

    List<StrutturaStaff> findByIdStruttura(Integer idStruttura);

    List<StrutturaStaff> findByIdUser(Integer idUser);

    @Query("SELECT ss FROM StrutturaStaff ss LEFT JOIN FETCH ss.user WHERE ss.idStruttura = :idStruttura ORDER BY ss.ordine")
    List<StrutturaStaff> findByIdStrutturaWithUser(@Param("idStruttura") Integer idStruttura);

    @Modifying
    @Query("DELETE FROM StrutturaStaff ss WHERE ss.idStruttura = :idStruttura AND ss.idUser = :idUser")
    void deleteByIdStrutturaAndIdUser(@Param("idStruttura") Integer idStruttura, @Param("idUser") Integer idUser);

    boolean existsByIdStrutturaAndIdUser(Integer idStruttura, Integer idUser);
}
