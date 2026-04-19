package com.core_aurora_performance.controller.internal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.core_aurora_performance.model.Attivita;
import com.core_aurora_performance.model.AttivitaAssegnazione;
import com.core_aurora_performance.model.AttivitaStep;
import com.core_aurora_performance.service.AttivitaCoreService;
import com.core_aurora_performance.service.CodiceService;
import com.core_aurora_performance.tenant.TenantContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/internal/attivita")
@RequiredArgsConstructor
@Slf4j
public class AttivitaCoreController {

    private final AttivitaCoreService attivitaService;
    private final CodiceService codiceService;

    @GetMapping
    public ResponseEntity<List<Attivita>> list(
            @RequestParam(required = false) Long progettoId,
            @RequestParam(required = false) Long utenteId,
            @RequestParam(required = false) Long strutturaId) {
        if (progettoId != null) return ResponseEntity.ok(attivitaService.findByProgettoId(progettoId));
        if (utenteId != null) return ResponseEntity.ok(attivitaService.findByUtenteId(utenteId));
        if (strutturaId != null) return ResponseEntity.ok(attivitaService.findByStrutturaId(strutturaId));
        String codiceIstat = TenantContext.require();
        return ResponseEntity.ok(attivitaService.findByCodiceIstat(codiceIstat));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attivita> getById(@PathVariable Long id) {
        return attivitaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Attivita> create(@RequestBody Attivita attivita) {
        if (attivita.getCodice() == null || attivita.getCodice().isBlank()) {
            String codiceIstat = TenantContext.require();
            attivita.setCodice(codiceService.generateNextAttivitaCodice(codiceIstat));
        }
        return ResponseEntity.ok(attivitaService.save(attivita));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attivita> update(@PathVariable Long id, @RequestBody Attivita attivita) {
        attivita.setId(id);
        return ResponseEntity.ok(attivitaService.save(attivita));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attivitaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/duplica")
    public ResponseEntity<Attivita> duplicate(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(attivitaService.duplicate(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/percentuale")
    public ResponseEntity<Attivita> updatePercentuale(@PathVariable Long id,
                                                        @RequestBody Map<String, Object> payload) {
        BigDecimal perc = new BigDecimal(payload.get("percentuale").toString());
        try {
            return ResponseEntity.ok(attivitaService.updatePercentuale(id, perc));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ─── Assegnazioni ─────────────────────────────────────────────────────────

    @PostMapping("/{id}/assegnazioni")
    public ResponseEntity<AttivitaAssegnazione> addAssegnazione(@PathVariable Long id,
                                                                  @RequestBody AttivitaAssegnazione ass) {
        ass.setAttivitaId(id);
        if (attivitaService.findAssegnazione(id, Long.valueOf(ass.getUtenteId())).isPresent()) {
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.ok(attivitaService.addAssegnazione(ass));
    }

    @DeleteMapping("/{id}/assegnazioni/{utenteId}")
    public ResponseEntity<Void> removeAssegnazione(@PathVariable Long id, @PathVariable Long utenteId) {
        attivitaService.removeAssegnazione(id, utenteId);
        return ResponseEntity.noContent().build();
    }

    // ─── Steps ────────────────────────────────────────────────────────────────

    @PostMapping("/{id}/steps")
    public ResponseEntity<AttivitaStep> addStep(@PathVariable Long id, @RequestBody AttivitaStep step) {
        step.setAttivitaId(id);
        return ResponseEntity.ok(attivitaService.addStep(step));
    }

    @PutMapping("/{id}/steps/{stepId}")
    public ResponseEntity<AttivitaStep> updateStep(@PathVariable Long id,
                                                    @PathVariable Long stepId,
                                                    @RequestBody AttivitaStep step) {
        step.setId(stepId);
        step.setAttivitaId(id);
        return attivitaService.toggleStep(stepId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/steps/{stepId}")
    public ResponseEntity<Void> removeStep(@PathVariable Long id, @PathVariable Long stepId) {
        attivitaService.removeStep(stepId);
        return ResponseEntity.noContent().build();
    }
}
