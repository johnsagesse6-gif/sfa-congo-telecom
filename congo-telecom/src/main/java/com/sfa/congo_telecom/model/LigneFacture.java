package com.sfa.congo_telecom.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lignes_facture")
public class LigneFacture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String article;
    private int quantite;
    private double prixUnitaireHT;

    @ManyToOne
    @JoinColumn(name = "facture_id")
    private Facture facture;

    public LigneFacture() {}

    // Getters et Setters
    public Long getId() { return id; }
    public String getArticle() { return article; }
    public void setArticle(String article) { this.article = article; }
    public Facture getFacture() { return facture; }
    public void setFacture(Facture facture) { this.facture = facture; }
}