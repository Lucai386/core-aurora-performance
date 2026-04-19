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
 * Obiettivo operativo assegnabile a utenti.
 * Supporta obiettivi crescenti (es. raggiungere X verbali) o
 * decrescenti (es. ridurre incidenti da X a Y).
 */
@Entity
@Table(name = "obiettivo", schema = "aurora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Obiettivo {

    /**
     * Tipo di obiettivo:
     * - CRESCENTE: il valore deve aumentare verso il target (es. 0 -> 100 verbali)
     * - DECRESCENTE: il valore deve diminuire verso il target (es. 100 -> 10 incidenti)
     */
    public enum TipoObiettivo {
        CRESCENTE, DECRESCENTE
    }

    public enum StatoObiettivo {
        ATTIVO, COMPLETATO, SCADUTO, SOSPESO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice_istat", nullable = false, length = 20)
    private String codiceIstat;

    @Column(nullable = false, length = 20)
    private String codice;

    @Column(nullable = false, length = 255)
    private String titolo;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    /** Unità di misura (es. "verbali", "incidenti", "pratiche", "ore") */
    @Column(name = "unita_misura", length = 50)
    private String unitaMisura;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    @Builder.Default
    private TipoObiettivo tipo = TipoObiettivo.CRESCENTE;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private StatoObiettivo stato = StatoObiettivo.ATTIVO;

    /** Valore iniziale (punto di partenza) */
    @Column(name = "valore_iniziale", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal valoreIniziale = BigDecimal.ZERO;

    /** Valore target da raggiungere */
    @Column(name = "valore_target", nullable = false, precision = 12, scale = 2)
    private BigDecimal valoreTarget;

    /** Valore corrente (ultimo valore registrato) */
    @Column(name = "valore_corrente", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal valoreCorrente = BigDecimal.ZERO;

    /** Peso dell'obiettivo nella valutazione complessiva (0-100) */
    @Column(precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal peso = new BigDecimal("100.00");

    /** Data inizio validità obiettivo */
    @Column(name = "data_inizio")
    private LocalDate dataInizio;

    /** Data fine/scadenza obiettivo */
    @Column(name = "data_fine")
    private LocalDate dataFine;

    /** Anno di riferimento */
    @Column(nullable = false)
    private Integer anno;

    /** Struttura di riferimento (opzionale, per filtraggio) */
    @Column(name = "struttura_id")
    private Integer strutturaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "struttura_id", insertable = false, updatable = false)
    private Struttura struttura;

    /** Utente a cui è assegnato l'obiettivo */
    @Column(name = "utente_assegnato_id")
    private Long utenteAssegnatoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_assegnato_id", insertable = false, updatable = false)
    private User utenteAssegnato;

    /** Utente che ha creato l'obiettivo (responsabile) */
    @Column(name = "creato_da_id", nullable = false)
    private Long creatoDaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creato_da_id", insertable = false, updatable = false)
    private User creatoDa;

    /** Storico dei progressivi */
    @OneToMany(mappedBy = "obiettivo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ObiettivoProgressivo> progressivi = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (stato == null) {
            stato = StatoObiettivo.ATTIVO;
        }
        if (valoreCorrente == null) {
            valoreCorrente = valoreIniziale != null ? valoreIniziale : BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Calcola la percentuale di completamento dell'obiettivo.
     * Per obiettivi CRESCENTI: (valoreCorrente - valoreIniziale) / (valoreTarget - valoreIniziale) * 100
     * Per obiettivi DECRESCENTI: (valoreIniziale - valoreCorrente) / (valoreIniziale - valoreTarget) * 100
     */
    public BigDecimal calcolaPercentuale() {
        if (valoreTarget == null) return BigDecimal.ZERO;
        
        BigDecimal iniziale = valoreIniziale != null ? valoreIniziale : BigDecimal.ZERO;
        BigDecimal corrente = valoreCorrente != null ? valoreCorrente : iniziale;
        
        BigDecimal range;
        BigDecimal progresso;
        
        if (tipo == TipoObiettivo.CRESCENTE) {
            range = valoreTarget.subtract(iniziale);
            progresso = corrente.subtract(iniziale);
        } else {
            // DECRESCENTE: devo ridurre da iniziale a target
            range = iniziale.subtract(valoreTarget);
            progresso = iniziale.subtract(corrente);
        }
        
        if (range.compareTo(BigDecimal.ZERO) == 0) {
            return corrente.compareTo(valoreTarget) == 0 ? new BigDecimal("100") : BigDecimal.ZERO;
        }
        
        BigDecimal percentuale = progresso.multiply(new BigDecimal("100")).divide(range, 2, java.math.RoundingMode.HALF_UP);
        
        // Limita tra 0 e 100
        if (percentuale.compareTo(BigDecimal.ZERO) < 0) return BigDecimal.ZERO;
        if (percentuale.compareTo(new BigDecimal("100")) > 0) return new BigDecimal("100");
        
        return percentuale;
    }
}
