package com.core_aurora_performance.model;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "strutture_staff", schema = "aurora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrutturaStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_struttura", nullable = false)
    private Integer idStruttura;

    @Column(name = "id_user", nullable = false)
    private Integer idUser;

    @Column(name = "ruolo_struttura", length = 50)
    private String ruoloStruttura;

    @Column(name = "ordine", nullable = false)
    @Builder.Default
    private Integer ordine = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // Relazione con Struttura
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_struttura", insertable = false, updatable = false)
    private Struttura struttura;

    // Relazione con User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", insertable = false, updatable = false)
    private User user;
}
