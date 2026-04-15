package com.sfa.congo_telecom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "articles")
@Data // Génère automatiquement Getters, Setters, toString, equals, hashCode
@NoArgsConstructor // Génère le constructeur vide
@AllArgsConstructor // Génère le constructeur avec tous les champs
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codeArticle;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private double prixUnitaireHT;

    @OneToMany(mappedBy = "article")
    private List<LigneFacture> lignes;
}