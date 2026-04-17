package com.core_aurora_performance.model;

import java.math.BigDecimal;
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
 * Registrazione progressiva di un obiettivo.
 * Ogni volta che l'utente assegnato registra un avanzamento,
 * viene creata una nuova entry per mantenere lo storico.
 */
@Entity
@Table(name = "obiettivo_progressivo", schema = "aurora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObiettivoProgressivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "obiettivo_id", nullable = false)
    private Long obiettivoId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "obiettivo_id", insertable = false, updatable = false)
    private Obiettivo obiettivo;

    /** Valore registrato in questa entry (può essere incrementale o totale a seconda del contesto) */
    @Column(name = "valore_registrato", nullable = false, precision = 12, scale = 2)
    private BigDecimal valoreRegistrato;

    /** Valore precedente (per calcolo delta) */
    @Column(name = "valore_precedente", precision = 12, scale = 2)
    private BigDecimal valorePrecedente;

    /** Note o descrizione dell'aggiornamento */
    @Column(columnDefinition = "TEXT")
    private String note;

    /** Utente che ha registrato il progressivo */
    @Column(name = "registrato_da_id", nullable = false)
    private Long registratoDaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrato_da_id", insertable = false, updatable = false)
    private User registratoDa;

    @Column(name = "data_registrazione", nullable = false)
    private LocalDateTime dataRegistrazione;

    @PrePersist
    protected void onCreate() {
        if (dataRegistrazione == null) {
            dataRegistrazione = LocalDateTime.now();
        }
    }
}
