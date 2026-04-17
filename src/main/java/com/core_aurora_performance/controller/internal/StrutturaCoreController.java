package com.core_aurora_performance.controller.internal;

import com.core_aurora_performance.model.Struttura;
import com.core_aurora_performance.model.StrutturaStaff;
import com.core_aurora_performance.model.User;
import com.core_aurora_performance.service.StrutturaCoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal/strutture")
@RequiredArgsConstructor
@Slf4j
public class StrutturaCoreController {

    private final StrutturaCoreService strutturaService;

    @GetMapping
    public ResponseEntity<List<Struttura>> list(@RequestParam String codiceIstat) {
        return ResponseEntity.ok(strutturaService.findByCodiceIstat(codiceIstat));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Struttura> getById(@PathVariable Integer id) {
        return strutturaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Struttura> create(@RequestBody Struttura struttura) {
        return ResponseEntity.ok(strutturaService.save(struttura));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Struttura> update(@PathVariable Integer id, @RequestBody Struttura struttura) {
        struttura.setId(id);
        return ResponseEntity.ok(strutturaService.save(struttura));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (strutturaService.hasChildren(id)) {
            return ResponseEntity.status(409).build(); // 409 Conflict
        }
        strutturaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/staff")
    public ResponseEntity<StrutturaStaff> addStaff(@PathVariable Integer id,
                                                    @RequestBody Map<String, Object> payload) {
        Long userId = Long.parseLong(payload.get("userId").toString());
        String ruolo = payload.getOrDefault("ruoloStruttura", "").toString();
        Integer ordine = payload.containsKey("ordine") ? Integer.parseInt(payload.get("ordine").toString()) : 0;

        if (strutturaService.existsStaff(id, userId)) {
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.ok(strutturaService.addStaff(id, userId, ruolo, ordine));
    }

    @DeleteMapping("/{id}/staff/{userId}")
    public ResponseEntity<Void> removeStaff(@PathVariable Integer id, @PathVariable Long userId) {
        strutturaService.removeStaff(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/utenti")
    public ResponseEntity<List<User>> getUtenti(@PathVariable Integer id) {
        return ResponseEntity.ok(strutturaService.findUtentiByStrutturaId(id));
    }
}
