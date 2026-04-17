package com.core_aurora_performance.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Progetto all'interno di un DUP.
 * Un DUP è un insieme di progetti.
 * I progetti possono essere collegati opzionalmente a una LPM.
 */
@Entity
@Table(name = "dup_progetti", schema = "aurora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DupProgetto {

    public enum Stato {
        TODO, IN_CORSO, COMPLETATO
    }

    public enum Priorita {
        BASSA, MEDIA, ALTA, CRITICA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** DUP di appartenenza */
    @Column(name = "dup_id", nullable = false)
    private Long dupId;

    @Column(length = 20)
    private String codice;

    @Column(nullable = false, length = 255)
    private String titolo;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    /** Collegamento opzionale a una LPM (prima si crea LPM, poi si può agganciare) */
    @Column(name = "lpm_id")
    private Long lpmId;

    /** Responsabile del progetto */
    @Column(name = "responsabile_id")
    private Integer responsabileId;

    /** Struttura assegnata al progetto */
    @Column(name = "struttura_id")
    private Integer strutturaId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Stato stato = Stato.TODO;

    @Column
    @Builder.Default
    private Integer progresso = 0;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Priorita priorita = Priorita.MEDIA;

    @Column(name = "data_inizio")
    private LocalDate dataInizio;

    @Column(name = "data_fine")
    private LocalDate dataFine;

    @Column(precision = 15, scale = 2)
    private BigDecimal budget;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column
    @Builder.Default
    private Integer ordine = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dup_id", insertable = false, updatable = false)
    private Dup dup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lpm_id", insertable = false, updatable = false)
    private Lpm lpm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsabile_id", insertable = false, updatable = false)
    private User responsabile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "struttura_id", insertable = false, updatable = false)
    private Struttura struttura;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
