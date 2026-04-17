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

@Entity
@Table(name = "lpm", schema = "aurora")
public class Lpm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice_istat", nullable = false, length = 6)
    private String codiceIstat;

    @Column(name = "anno_inizio_mandato", nullable = false)
    private Integer annoInizioMandato;

    @Column(name = "anno_fine_mandato", nullable = false)
    private Integer annoFineMandato;

    @Column(nullable = false)
    private String titolo;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Column(nullable = false, length = 20)
    private String stato = "todo";

    private Integer priorita = 0;

    private Integer progresso = 0;

    @Column(name = "responsabile_id")
    private Integer responsabileId;

    @Column(name = "dup_id")
    private Long dupId;

    @Column(name = "dup_titolo")
    private String dupTitolo;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private Integer deletedBy;

    @Column(name = "deleted_reason", columnDefinition = "TEXT")
    private String deletedReason;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodiceIstat() { return codiceIstat; }
    public void setCodiceIstat(String codiceIstat) { this.codiceIstat = codiceIstat; }

    public Integer getAnnoInizioMandato() { return annoInizioMandato; }
    public void setAnnoInizioMandato(Integer annoInizioMandato) { this.annoInizioMandato = annoInizioMandato; }

    public Integer getAnnoFineMandato() { return annoFineMandato; }
    public void setAnnoFineMandato(Integer annoFineMandato) { this.annoFineMandato = annoFineMandato; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public Integer getPriorita() { return priorita; }
    public void setPriorita(Integer priorita) { this.priorita = priorita; }

    public Integer getProgresso() { return progresso; }
    public void setProgresso(Integer progresso) { this.progresso = progresso; }

    public Integer getResponsabileId() { return responsabileId; }
    public void setResponsabileId(Integer responsabileId) { this.responsabileId = responsabileId; }

    public Long getDupId() { return dupId; }
    public void setDupId(Long dupId) { this.dupId = dupId; }

    public String getDupTitolo() { return dupTitolo; }
    public void setDupTitolo(String dupTitolo) { this.dupTitolo = dupTitolo; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Integer updatedBy) { this.updatedBy = updatedBy; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

    public Integer getDeletedBy() { return deletedBy; }
    public void setDeletedBy(Integer deletedBy) { this.deletedBy = deletedBy; }

    public String getDeletedReason() { return deletedReason; }
    public void setDeletedReason(String deletedReason) { this.deletedReason = deletedReason; }

    public boolean isDeleted() { return deletedAt != null; }
}
