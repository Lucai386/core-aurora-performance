package com.core_aurora_performance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.core_aurora_performance.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKeycloakId(String keycloakId);

    Optional<User> findByCodiceFiscale(String codiceFiscale);

    List<User> findByCodiceIstat(String codiceIstat);

    List<User> findByCodiceIstatAndRuolo(String codiceIstat, String ruolo);

    boolean existsByKeycloakId(String keycloakId);
}
