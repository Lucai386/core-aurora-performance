package com.core_aurora_performance.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rappresenta un sotto-step di un'attività.
 * Ogni step è una checkbox che contribuisce alla percentuale di completamento dell'attività.
 */
@Entity
@Table(name = "attivita_step")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttivitaStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attivita_id", nullable = false)
    private Long attivitaId;

    @Column(nullable = false)
    private String titolo;

    private String descrizione;

    @Column(nullable = false)
    @Builder.Default
    private Boolean completato = false;

    /**
     * Peso dello step (0-100).
     * Indica quanto incide questo step sulla percentuale di completamento dell'attività.
     * La somma dei pesi di tutti gli step di un'attività dovrebbe essere 100.
     */
    @Column(nullable = false)
    @Builder.Default
    private Integer peso = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer ordine = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
