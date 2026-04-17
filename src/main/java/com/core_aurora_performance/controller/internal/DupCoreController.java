package com.core_aurora_performance.controller.internal;

import com.core_aurora_performance.model.Dup;
import com.core_aurora_performance.model.DupProgetto;
import com.core_aurora_performance.service.CodiceService;
import com.core_aurora_performance.service.DupCoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@Slf4j
public class DupCoreController {

    private final DupCoreService dupService;
    private final CodiceService codiceService;

    // ─── DUP ──────────────────────────────────────────────────────────────────

    @GetMapping("/dup")
    public ResponseEntity<List<Dup>> listDup(@RequestParam String codiceIstat) {
        return ResponseEntity.ok(dupService.findByCodiceIstat(codiceIstat));
    }

    @GetMapping("/dup/{id}")
    public ResponseEntity<Dup> getDup(@PathVariable Long id) {
        return dupService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/dup")
    public ResponseEntity<Dup> createDup(@RequestBody Dup dup) {
        if (dup.getCodice() == null || dup.getCodice().isBlank()) {
            dup.setCodice(codiceService.generateNextDupCodice(dup.getCodiceIstat()));
        }
        return ResponseEntity.ok(dupService.save(dup));
    }

    @PutMapping("/dup/{id}")
    public ResponseEntity<Dup> updateDup(@PathVariable Long id, @RequestBody Dup dup) {
        dup.setId(id);
        return ResponseEntity.ok(dupService.save(dup));
    }

    @DeleteMapping("/dup/{id}")
    public ResponseEntity<Void> deleteDup(@PathVariable Long id) {
        if (dupService.hasProgetti(id)) {
            return ResponseEntity.status(409).build();
        }
        dupService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Progetti ─────────────────────────────────────────────────────────────

    @GetMapping("/progetti")
    public ResponseEntity<List<DupProgetto>> listProgetti(@RequestParam Long dupId) {
        return ResponseEntity.ok(dupService.findProgettiByDupId(dupId));
    }

    @GetMapping("/progetti/{id}")
    public ResponseEntity<DupProgetto> getProgetto(@PathVariable Long id) {
        return dupService.findProgettoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/progetti")
    public ResponseEntity<DupProgetto> createProgetto(@RequestBody DupProgetto progetto) {
        if (progetto.getCodice() == null || progetto.getCodice().isBlank()) {
            // Ottieni codiceIstat dal DUP
            dupService.findById(progetto.getDupId()).ifPresent(dup ->
                    progetto.setCodice(codiceService.generateNextProgettoCodice(dup.getCodiceIstat()))
            );
        }
        return ResponseEntity.ok(dupService.saveProgetto(progetto));
    }

    @PutMapping("/progetti/{id}")
    public ResponseEntity<DupProgetto> updateProgetto(@PathVariable Long id, @RequestBody DupProgetto progetto) {
        progetto.setId(id);
        return ResponseEntity.ok(dupService.saveProgetto(progetto));
    }

    @DeleteMapping("/progetti/{id}")
    public ResponseEntity<Void> deleteProgetto(@PathVariable Long id) {
        dupService.deleteProgetto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/progetti/{id}/lpm/{lpmId}")
    public ResponseEntity<Void> linkLpm(@PathVariable Long id, @PathVariable Long lpmId) {
        boolean ok = dupService.linkLpm(id, lpmId);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/progetti/{id}/lpm")
    public ResponseEntity<Void> unlinkLpm(@PathVariable Long id) {
        dupService.unlinkLpm(id);
        return ResponseEntity.ok().build();
    }

    // ─── Generazione codici ───────────────────────────────────────────────────

    @PostMapping("/codici/dup")
    public ResponseEntity<Map<String, String>> nextDupCodice(@RequestBody Map<String, String> payload) {
        String codice = codiceService.generateNextDupCodice(payload.get("codiceIstat"));
        return ResponseEntity.ok(Map.of("codice", codice));
    }

    @PostMapping("/codici/progetto")
    public ResponseEntity<Map<String, String>> nextProgettoCodice(@RequestBody Map<String, String> payload) {
        String codice = codiceService.generateNextProgettoCodice(payload.get("codiceIstat"));
        return ResponseEntity.ok(Map.of("codice", codice));
    }

    @PostMapping("/codici/attivita")
    public ResponseEntity<Map<String, String>> nextAttivitaCodice(@RequestBody Map<String, String> payload) {
        String codice = codiceService.generateNextAttivitaCodice(payload.get("codiceIstat"));
        return ResponseEntity.ok(Map.of("codice", codice));
    }

    @PostMapping("/codici/obiettivo")
    public ResponseEntity<Map<String, String>> nextObiettivoCodice(@RequestBody Map<String, String> payload) {
        String codice = codiceService.generateNextObiettivoCodice(payload.get("codiceIstat"));
        return ResponseEntity.ok(Map.of("codice", codice));
    }
}
