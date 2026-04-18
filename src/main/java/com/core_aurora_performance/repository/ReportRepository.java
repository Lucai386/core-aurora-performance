package com.core_aurora_performance.repository;

import com.core_aurora_performance.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByCodiceIstatOrderByGeneratoIlDesc(String codiceIstat);
    List<Report> findByCodiceIstatAndUtenteIdOrderByGeneratoIlDesc(String codiceIstat, Long utenteId);
    List<Report> findByCodiceIstatAndTipoAndAnnoOrderByGeneratoIlDesc(String codiceIstat, String tipo, Integer anno);
    List<Report> findByCodiceIstatAndStrutturaIdOrderByGeneratoIlDesc(String codiceIstat, Long strutturaId);
}
