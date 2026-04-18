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
@Table(name = "report_template", schema = "aurora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice", nullable = false, unique = true, length = 80)
    private String codice;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "descrizione", columnDefinition = "TEXT")
    private String descrizione;

    @Column(name = "tipo", nullable = false, length = 60)
    private String tipo;

    @Column(name = "template_html", columnDefinition = "TEXT")
    private String templateHtml;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "struttura_campi", columnDefinition = "jsonb")
    private String strutturaCampi;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "labels_i18n", columnDefinition = "jsonb")
    private String labelsI18n;

    @Column(name = "versione", nullable = false)
    @Builder.Default
    private Integer versione = 1;

    @Column(name = "attivo", nullable = false)
    @Builder.Default
    private Boolean attivo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
