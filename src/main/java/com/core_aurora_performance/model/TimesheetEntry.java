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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entry di timesheet: registra le ore lavorate da un utente
 * su un'attività in una data specifica.
 */
@Entity
@Table(name = "timesheet_entries", schema = "aurora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attivita_id", nullable = false)
    private Long attivitaId;

    @Column(name = "utente_id", nullable = false)
    private Integer utenteId;

    /** Data della registrazione ore */
    @Column(nullable = false)
    private LocalDate data;

    /** Ore lavorate in questa data */
    @Column(name = "ore_lavorate", nullable = false)
    private BigDecimal oreLavorate;

    /** Descrizione del lavoro svolto */
    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

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
    }
}
