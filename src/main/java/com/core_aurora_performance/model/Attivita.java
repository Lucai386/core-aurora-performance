package com.core_aurora_performance.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Attività (sotto-attività di un Progetto).
 * Contiene peso per la valutazione delle performance e step per il completamento.
 * Può essere assegnata a uno o più utenti.
 */
@Entity
@Table(name = "attivita", schema = "aurora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attivita {

    public enum Stato {
        TODO, IN_CORSO, COMPLETATA, SOSPESA
    }

    public enum Priorita {
        BASSA, MEDIA, ALTA, CRITICA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Progetto di appartenenza */
    @Column(name = "progetto_id", nullable = false)
    private Long progettoId;

    @Column(nullable = false, length = 20)
    private String codice;

    @Column(nullable = false, length = 255)
    private String titolo;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Stato stato = Stato.TODO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Priorita priorita = Priorita.MEDIA;

    // ==================== PESO ====================

    /**
     * Peso (0-100).
     * Indica quanto questa attività pesa sull'avanzamento complessivo
     * del progetto.
     */
    @Column(name = "peso")
    @Builder.Default
    private Integer peso = 100;

    // ==================== METRICHE TEMPORALI ====================

    /** Ore stimate per completare l'attività */
    @Column(name = "ore_stimate")
    @Builder.Default
    private BigDecimal oreStimate = BigDecimal.ZERO;

    /** Ore effettivamente lavorate (calcolate dal timesheet) */
    @Column(name = "ore_lavorate")
    @Builder.Default
    private BigDecimal oreLavorate = BigDecimal.ZERO;

    /** Data di inizio prevista */
    @Column(name = "data_inizio")
    private LocalDate dataInizio;

    /** Data di fine stimata */
    @Column(name = "data_fine_stimata")
    private LocalDate dataFineStimata;

    /** Data di fine effettiva */
    @Column(name = "data_fine_effettiva")
    private LocalDate dataFineEffettiva;

    /** Percentuale di completamento (0-100) - gestita manualmente, indipendente dalle ore */
    @Column(name = "percentuale_completamento")
    @Builder.Default
    private Integer percentualeCompletamento = 0;

    @Column(columnDefinition = "TEXT")
    private String note;

    /** Struttura assegnata all'attività (opzionale, eredita dal progetto se null) */
    @Column(name = "struttura_id")
    private Integer strutturaId;

    @Column
    @Builder.Default
    private Integer ordine = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ==================== RELATIONSHIPS ====================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progetto_id", insertable = false, updatable = false)
    private DupProgetto progetto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "struttura_id", insertable = false, updatable = false)
    private Struttura struttura;

    @OneToMany(mappedBy = "attivita", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AttivitaAssegnazione> assegnazioni = new ArrayList<>();

    @OneToMany(mappedBy = "attivita", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TimesheetEntry> timesheetEntries = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "attivita_id")
    @Builder.Default
    private List<AttivitaStep> steps = new ArrayList<>();

    // ==================== LIFECYCLE ====================

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ==================== COMPUTED ====================

    /**
     * Calcola le ore mancanti per completare l'attività.
     */
    public BigDecimal getOreMancanti() {
        if (oreStimate == null) return BigDecimal.ZERO;
        if (oreLavorate == null) return oreStimate;
        BigDecimal diff = oreStimate.subtract(oreLavorate);
        return diff.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : diff;
    }

    /**
     * Calcola la percentuale di ore lavorate rispetto alle stimate (può superare 100%).
     * Questo è diverso dalla percentualeCompletamento che è gestita manualmente.
     */
    public Integer getPercentualeOreLavorate() {
        if (oreStimate == null || oreStimate.compareTo(BigDecimal.ZERO) == 0) return 0;
        if (oreLavorate == null) return 0;
        BigDecimal percentuale = oreLavorate.multiply(BigDecimal.valueOf(100)).divide(oreStimate, 0, java.math.RoundingMode.HALF_UP);
        return percentuale.intValue(); // Può superare 100% (overflow)
    }

    /**
     * Calcola lo scostamento in giorni rispetto alla data di fine stimata.
     * Positivo = ritardo, negativo = anticipo.
     */
    public Integer getScostamentoGiorni() {
        if (dataFineStimata == null) return 0;
        LocalDate riferimento = dataFineEffettiva != null ? dataFineEffettiva : LocalDate.now();
        return (int) java.time.temporal.ChronoUnit.DAYS.between(dataFineStimata, riferimento);
    }
}
