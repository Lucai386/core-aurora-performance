package com.core_aurora_performance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Gestisce le sequenze dei codici autoincrementali per ente.
 * Ogni ente (codice_istat) ha sequenze separate per DUP, Progetti e Attività.
 */
@Entity
@Table(name = "codice_sequence", schema = "aurora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodiceSequence {

    public enum EntityType {
        DUP, PRJ, ATT, OBJ
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice_istat", nullable = false, length = 6)
    private String codiceIstat;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, length = 10)
    private EntityType entityType;

    @Column(name = "last_number", nullable = false)
    @Builder.Default
    private Long lastNumber = 0L;
}
