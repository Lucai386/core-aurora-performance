package com.core_aurora_performance.controller.internal;

import com.core_aurora_performance.model.User;
import com.core_aurora_performance.model.UserLog;
import com.core_aurora_performance.service.UserCoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Endpoint interni per la gestione degli utenti.
 * Accessibile solo dai BFF tramite service-account token.
 */
@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
@Slf4j
public class UserCoreController {

    private final UserCoreService userService;

    @GetMapping
    public ResponseEntity<List<User>> listUsers(
            @RequestParam(required = false) String codiceIstat,
            @RequestParam(required = false) String ruolo) {
        if (codiceIstat != null && ruolo != null) {
            return ResponseEntity.ok(userService.findByCodiceIstatAndRuolo(codiceIstat, ruolo));
        }
        if (codiceIstat != null) {
            return ResponseEntity.ok(userService.findByCodiceIstat(codiceIstat));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/keycloak/{keycloakId}")
    public ResponseEntity<User> getByKeycloakId(@PathVariable String keycloakId) {
        return userService.findByKeycloakId(keycloakId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createOrUpdate(@RequestBody User user) {
        User saved = userService.save(user);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User saved = userService.save(user);
        return ResponseEntity.ok(saved);
    }

    @PatchMapping("/{id}/codice-istat")
    public ResponseEntity<User> patchCodiceIstat(@PathVariable Long id,
                                                  @RequestBody Map<String, String> payload) {
        return userService.findById(id).map(u -> {
            u.setCodiceIstat(payload.get("codiceIstat"));
            return ResponseEntity.ok(userService.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logs")
    public ResponseEntity<UserLog> saveLog(@RequestBody UserLog log) {
        return ResponseEntity.ok(userService.saveLog(log));
    }
}
