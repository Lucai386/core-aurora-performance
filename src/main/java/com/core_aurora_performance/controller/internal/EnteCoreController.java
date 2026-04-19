package com.core_aurora_performance.controller.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.core_aurora_performance.model.Ente;
import com.core_aurora_performance.repository.EnteRepository;
import com.core_aurora_performance.repository.StrutturaRepository;
import com.core_aurora_performance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/enti")
@RequiredArgsConstructor
public class EnteCoreController {

    private final UserRepository userRepository;
    private final StrutturaRepository strutturaRepository;
    private final EnteRepository enteRepository;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listEnti() {
        Map<String, Long> userCounts = new HashMap<>();
        for (Object[] row : userRepository.countGroupByCodiceIstat()) {
            userCounts.put((String) row[0], ((Number) row[1]).longValue());
        }

        Map<String, Long> strutturaCounts = new HashMap<>();
        for (Object[] row : strutturaRepository.countGroupByCodiceIstat()) {
            strutturaCounts.put((String) row[0], ((Number) row[1]).longValue());
        }

        // Load ente names from the enti table
        Map<String, Ente> entiByIstat = enteRepository.findAll().stream()
                .collect(Collectors.toMap(Ente::getCodiceIstat, Function.identity()));

        Set<String> allCodici = new TreeSet<>();
        allCodici.addAll(userCounts.keySet());
        allCodici.addAll(strutturaCounts.keySet());
        allCodici.addAll(entiByIstat.keySet());

        List<Map<String, Object>> result = new ArrayList<>();
        for (String codice : allCodici) {
            Map<String, Object> ente = new LinkedHashMap<>();
            ente.put("codiceIstat", codice);
            Ente enteEntity = entiByIstat.get(codice);
            ente.put("nome", enteEntity != null ? enteEntity.getNome() : null);
            ente.put("numUtenti", userCounts.getOrDefault(codice, 0L));
            ente.put("numStrutture", strutturaCounts.getOrDefault(codice, 0L));
            result.add(ente);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Ente> createOrUpdateEnte(@RequestBody Map<String, String> payload) {
        String codiceIstat = payload.get("codiceIstat");
        String nome = payload.get("nome");
        if (codiceIstat == null || codiceIstat.isBlank() || nome == null || nome.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Ente ente = enteRepository.findByCodiceIstat(codiceIstat.trim())
                .orElse(Ente.builder().codiceIstat(codiceIstat.trim()).build());
        ente.setNome(nome.trim());
        return ResponseEntity.ok(enteRepository.save(ente));
    }
}
