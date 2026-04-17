package com.core_aurora_performance.controller.internal;

import com.core_aurora_performance.model.Lpm;
import com.core_aurora_performance.model.LpmNote;
import com.core_aurora_performance.service.LpmCoreService;
import com.core_aurora_performance.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal/lpm")
@RequiredArgsConstructor
@Slf4j
public class LpmCoreController {

    private final LpmCoreService lpmService;

    @GetMapping
    public ResponseEntity<List<Lpm>> list() {
        return ResponseEntity.ok(lpmService.findActiveByCodiceIstat(TenantContext.require()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lpm> getById(@PathVariable Long id) {
        return lpmService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Lpm> create(@RequestBody Lpm lpm) {
        lpm.setCodiceIstat(TenantContext.require());
        return ResponseEntity.ok(lpmService.save(lpm));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lpm> update(@PathVariable Long id, @RequestBody Lpm lpm) {
        lpm.setId(id);
        return ResponseEntity.ok(lpmService.save(lpm));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id,
                                           @RequestParam(required = false) Long deletedBy) {
        boolean ok = lpmService.softDelete(id, deletedBy);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{lpmId}/dup/{dupId}")
    public ResponseEntity<Void> linkDup(@PathVariable Long lpmId, @PathVariable Long dupId) {
        boolean ok = lpmService.linkDup(lpmId, dupId);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{lpmId}/dup")
    public ResponseEntity<Void> unlinkDup(@PathVariable Long lpmId) {
        lpmService.unlinkDup(lpmId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{lpmId}/note")
    public ResponseEntity<List<LpmNote>> getNotes(@PathVariable Long lpmId) {
        return ResponseEntity.ok(lpmService.findNotesByLpmId(lpmId));
    }

    @PostMapping("/{lpmId}/note")
    public ResponseEntity<LpmNote> addNote(@PathVariable Long lpmId, @RequestBody LpmNote note) {
        note.setLpmId(lpmId);
        return ResponseEntity.ok(lpmService.saveNote(note));
    }
}
