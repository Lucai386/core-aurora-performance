package com.core_aurora_performance.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "lpm_note", schema = "aurora")
public class LpmNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lpm_id", nullable = false)
    private Long lpmId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String testo;

    @Column(name = "autore_id")
    private Integer autoreId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLpmId() { return lpmId; }
    public void setLpmId(Long lpmId) { this.lpmId = lpmId; }

    public String getTesto() { return testo; }
    public void setTesto(String testo) { this.testo = testo; }

    public Integer getAutoreId() { return autoreId; }
    public void setAutoreId(Integer autoreId) { this.autoreId = autoreId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
