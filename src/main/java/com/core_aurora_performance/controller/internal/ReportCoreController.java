package com.core_aurora_performance.controller.internal;

import com.core_aurora_performance.model.Report;
import com.core_aurora_performance.model.ReportTemplate;
import com.core_aurora_performance.service.ReportCoreService;
import com.core_aurora_performance.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportCoreController {

    private final ReportCoreService reportService;

    @GetMapping
    public ResponseEntity<List<Report>> list(
            @RequestParam(required = false) Long utenteId,
            @RequestParam(required = false) Long strutturaId) {
        if (utenteId != null) return ResponseEntity.ok(reportService.findByUtente(utenteId));
        if (strutturaId != null) return ResponseEntity.ok(reportService.findByStruttura(strutturaId));
        return ResponseEntity.ok(reportService.findByCodiceIstat());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getById(@PathVariable Long id) {
        return reportService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Report> create(@RequestBody Report report) {
        report.setCodiceIstat(TenantContext.require());
        return ResponseEntity.ok(reportService.save(report));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Report> update(@PathVariable Long id, @RequestBody Report report) {
        report.setId(id);
        return ResponseEntity.ok(reportService.save(report));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Templates ──────────────────────────────────────────────────────

    @GetMapping("/templates")
    public ResponseEntity<List<ReportTemplate>> listTemplates() {
        return ResponseEntity.ok(reportService.findAllActiveTemplates());
    }

    @GetMapping("/templates/{id}")
    public ResponseEntity<ReportTemplate> getTemplateById(@PathVariable Long id) {
        return reportService.findTemplateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/templates/codice/{codice}")
    public ResponseEntity<ReportTemplate> getTemplateByCodice(@PathVariable String codice) {
        return reportService.findTemplateByCodice(codice)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
