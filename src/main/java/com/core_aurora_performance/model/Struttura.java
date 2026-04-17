package com.core_aurora_performance.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "strutture", schema = "aurora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Struttura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "codice_istat_comune", length = 6)
    private String codiceIstatComune;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "id_parent")
    private Integer idParent;

    @Column(name = "id_responsabile")
    private Integer idResponsabile;

    @Column(name = "ruolo_label", length = 100)
    private String ruoloLabel;

    @Column(name = "colore", length = 7)
    private String colore;

    @Column(name = "ordine", nullable = false)
    @Builder.Default
    private Integer ordine = 0;

    // Relazione con il responsabile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsabile", insertable = false, updatable = false)
    private User responsabile;

    // Staff della struttura
    @OneToMany(mappedBy = "struttura", fetch = FetchType.LAZY)
    @OrderBy("ordine ASC")
    @Builder.Default
    private List<StrutturaStaff> staff = new ArrayList<>();
}
