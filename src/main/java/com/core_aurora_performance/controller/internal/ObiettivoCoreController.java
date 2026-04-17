package com.core_aurora_performance.controller.internal;

import com.core_aurora_performance.model.Obiettivo;
import com.core_aurora_performance.model.ObiettivoProgressivo;
import com.core_aurora_performance.service.CodiceService;
import com.core_aurora_performance.service.ObiettivoCoreService;
import com.core_aurora_performance.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal/obiettivi")
@RequiredArgsConstructor
@Slf4j
public class ObiettivoCoreController {

    private final ObiettivoCoreService obiettivoService;
    private final CodiceService codiceService;

    @GetMapping
    public ResponseEntity<List<Obiettivo>> list(
            @RequestParam(required = false) Long utenteId) {
        if (utenteId != null) return ResponseEntity.ok(obiettivoService.findByUtenteId(utenteId));
        return ResponseEntity.ok(obiettivoService.findByCodiceIstat(TenantContext.require()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Obiettivo> getById(@PathVariable Long id) {
        return obiettivoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Obiettivo> create(@RequestBody Obiettivo obiettivo) {
        obiettivo.setCodiceIstat(TenantContext.require());
        if (obiettivo.getCodice() == null || obiettivo.getCodice().isBlank()) {
            obiettivo.setCodice(codiceService.generateNextObiettivoCodice(obiettivo.getCodiceIstat()));
        }
        return ResponseEntity.ok(obiettivoService.save(obiettivo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Obiettivo> update(@PathVariable Long id, @RequestBody Obiettivo obiettivo) {
        obiettivo.setId(id);
        return ResponseEntity.ok(obiettivoService.save(obiettivo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        obiettivoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/progressivi")
    public ResponseEntity<ObiettivoProgressivo> registraProgressivo(@PathVariable Long id,
                                                                      @RequestBody Map<String, Object> payload) {
        BigDecimal valore = new BigDecimal(payload.get("valore").toString());
        Long registratoId = payload.containsKey("registratoDaId")
                ? Long.parseLong(payload.get("registratoDaId").toString()) : null;
        try {
            return ResponseEntity.ok(obiettivoService.registraProgressivo(id, valore, registratoId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> counts() {
        String codiceIstat = TenantContext.require();
        return ResponseEntity.ok(Map.of(
                "totale", obiettivoService.countTotale(codiceIstat),
                "attivi", obiettivoService.countAttivi(codiceIstat),
                "completati", obiettivoService.countCompletati(codiceIstat)
        ));
    }
}
