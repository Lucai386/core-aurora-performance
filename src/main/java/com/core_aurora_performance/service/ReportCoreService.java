package com.core_aurora_performance.service;

import com.core_aurora_performance.model.Report;
import com.core_aurora_performance.model.ReportTemplate;
import com.core_aurora_performance.repository.ReportRepository;
import com.core_aurora_performance.repository.ReportTemplateRepository;
import com.core_aurora_performance.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportCoreService {

    private final ReportRepository reportRepository;
    private final ReportTemplateRepository templateRepository;

    // ─── Templates ──────────────────────────────────────────────────────

    public List<ReportTemplate> findAllActiveTemplates() {
        return templateRepository.findByAttivoTrue();
    }

    public Optional<ReportTemplate> findTemplateByCodice(String codice) {
        return templateRepository.findByCodice(codice);
    }

    public Optional<ReportTemplate> findTemplateById(Long id) {
        return templateRepository.findById(id);
    }

    // ─── Reports ────────────────────────────────────────────────────────

    public List<Report> findByUtente(Long utenteId) {
        return reportRepository.findByCodiceIstatAndUtenteIdOrderByGeneratoIlDesc(
                TenantContext.require(), utenteId);
    }

    public List<Report> findByCodiceIstat() {
        return reportRepository.findByCodiceIstatOrderByGeneratoIlDesc(TenantContext.require());
    }

    public List<Report> findByStruttura(Long strutturaId) {
        return reportRepository.findByCodiceIstatAndStrutturaIdOrderByGeneratoIlDesc(
                TenantContext.require(), strutturaId);
    }

    public Optional<Report> findById(Long id) {
        return reportRepository.findById(id);
    }

    public Report save(Report report) {
        return reportRepository.save(report);
    }

    public void delete(Long id) {
        reportRepository.deleteById(id);
    }
}
