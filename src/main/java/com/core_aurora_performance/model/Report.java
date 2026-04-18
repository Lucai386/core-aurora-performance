package com.core_aurora_performance.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "report", schema = "aurora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    public enum ReportStatus {
        GENERATO, APPROVATO, ARCHIVIATO, ANNULLATO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice_istat", nullable = false, length = 20)
    private String codiceIstat;

    @Column(name = "tipo", nullable = false, length = 60)
    private String tipo;

    @Column(name = "titolo", nullable = false, length = 300)
    private String titolo;

    @Column(name = "descrizione", columnDefinition = "TEXT")
    private String descrizione;

    @Column(name = "anno", nullable = false)
    private Integer anno;

    @Column(name = "trimestre")
    private Integer trimestre;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato", nullable = false, length = 20)
    @Builder.Default
    private ReportStatus stato = ReportStatus.GENERATO;

    @Column(name = "utente_id", nullable = false)
    private Long utenteId;

    @Column(name = "utente_nome", length = 200)
    private String utenteNome;

    @Column(name = "struttura_id")
    private Long strutturaId;

    @Column(name = "struttura_nome", length = 200)
    private String strutturaNome;

    @Column(name = "template_id")
    private Long templateId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dati_report", columnDefinition = "jsonb")
    private String datiReport;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "parametri", columnDefinition = "jsonb")
    private String parametri;

    @Column(name = "lingua", length = 5)
    @Builder.Default
    private String lingua = "it";

    @Column(name = "dimensione_bytes")
    private Long dimensioneBytes;

    @Column(name = "generato_il", nullable = false, updatable = false)
    private LocalDateTime generatoIl;

    @Column(name = "approvato_il")
    private LocalDateTime approvatoIl;

    @Column(name = "approvato_da_id")
    private Long approvatoDaId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (generatoIl == null) generatoIl = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
