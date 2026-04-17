package com.core_aurora_performance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", schema = "aurora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keycloak_id", nullable = false, unique = true, length = 36)
    private String keycloakId;

    @Column(name = "nome", length = 50)
    private String nome;

    @Column(name = "cognome", length = 50)
    private String cognome;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "codice_fiscale", length = 16, unique = true)
    private String codiceFiscale;

    @Column(name = "codice_istat", length = 6)
    private String codiceIstat;

    @Column(name = "ruolo", length = 2)
    private String ruolo;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "assegnazioni", columnDefinition = "jsonb")
    private Map<String, Object> assegnazioni;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public String getNomeCompleto() {
        if (nome == null && cognome == null) return null;
        if (nome == null) return cognome;
        if (cognome == null) return nome;
        return nome + " " + cognome;
    }
}
