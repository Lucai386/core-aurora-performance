package com.core_aurora_performance.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Assegnazione di un utente a un'attività.
 * Traccia le ore stimate e lavorate per ogni utente su ogni attività.
 */
@Entity
@Table(name = "attivita_assegnazioni", schema = "aurora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttivitaAssegnazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attivita_id", nullable = false)
    private Long attivitaId;

    @Column(name = "utente_id", nullable = false)
    private Integer utenteId;

    /** Ruolo dell'utente nell'attività (es. "Responsabile", "Collaboratore") */
    @Column(length = 50)
    private String ruolo;

    /** Ore stimate per questo utente su questa attività */
    @Column(name = "ore_stimate")
    @Builder.Default
    private BigDecimal oreStimate = BigDecimal.ZERO;

    /** Ore effettivamente lavorate da questo utente (calcolate dal timesheet) */
    @Column(name = "ore_lavorate")
    @Builder.Default
    private BigDecimal oreLavorate = BigDecimal.ZERO;

    @Column(name = "data_assegnazione")
    private LocalDate dataAssegnazione;

    @Column(name = "data_inizio")
    private LocalDate dataInizio;

    @Column(name = "data_fine")
    private LocalDate dataFine;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ==================== RELATIONSHIPS ====================

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attivita_id", insertable = false, updatable = false)
    private Attivita attivita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", insertable = false, updatable = false)
    private User utente;

    // ==================== LIFECYCLE ====================

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (dataAssegnazione == null) {
            dataAssegnazione = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
