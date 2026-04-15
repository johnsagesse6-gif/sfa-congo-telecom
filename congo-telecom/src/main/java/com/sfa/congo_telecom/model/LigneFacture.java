package com.sfa.congo_telecom.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "lignes_facture")
@Data
@NoArgsConstructor // Crucial pour Hibernate
@AllArgsConstructor // Pour faciliter les tests
public class LigneFacture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    private int quantite;

    private double prixAppliqueHT;

    private String descriptionAdditionnelle;

    @ManyToOne
    @JoinColumn(name = "facture_id")
    private Facture facture;

    // Ton constructeur métier personnalisé
    public LigneFacture(Article article, int quantite, String descriptionAdditionnelle) {
        this.article = article;
        this.quantite = quantite;
        if (article != null) {
            this.prixAppliqueHT = article.getPrixUnitaireHT();
        }
        this.descriptionAdditionnelle = descriptionAdditionnelle;
    }

    public double getSousTotal() {
        return this.prixAppliqueHT * this.quantite;
    }
}