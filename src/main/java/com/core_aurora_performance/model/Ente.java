package com.core_aurora_performance.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "enti", schema = "aurora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ente {

    @Id
    @Column(name = "codice_istat", length = 6)
    private String codiceIstat;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
