package com.sfa.congo_telecom.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "taxes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Taxe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String libelle; // Ex: "TVA", "DA", "TE"

    @Column(nullable = false)
    private double valeur; // Ex: 18.0, 5.0, 50.0

    @Column(nullable = false)
    private String typeTaxe; // "POURCENTAGE" ou "FIXE"

    private boolean actif = true;

    // Constructeur pratique pour le DataInitializer
    public Taxe(String libelle, double valeur, String typeTaxe) {
        this.libelle = libelle;
        this.valeur = valeur;
        this.typeTaxe = typeTaxe;
        this.actif = true;
    }
}