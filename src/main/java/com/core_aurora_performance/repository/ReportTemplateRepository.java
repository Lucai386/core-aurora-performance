package com.core_aurora_performance.repository;

import com.core_aurora_performance.model.ReportTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportTemplateRepository extends JpaRepository<ReportTemplate, Long> {
    Optional<ReportTemplate> findByCodice(String codice);
    List<ReportTemplate> findByAttivoTrue();
    List<ReportTemplate> findByTipo(String tipo);
}
