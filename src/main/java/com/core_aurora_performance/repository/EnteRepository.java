package com.core_aurora_performance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.core_aurora_performance.model.Ente;

public interface EnteRepository extends JpaRepository<Ente, String> {
    Optional<Ente> findByCodiceIstat(String codiceIstat);
}
