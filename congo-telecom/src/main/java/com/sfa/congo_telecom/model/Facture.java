package com.sfa.congo_telecom.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "factures")
@Data
@NoArgsConstructor // Remplace ton constructeur vide manuel
@AllArgsConstructor // Génère un constructeur avec tous les champs
public class Facture { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_facture", unique = true, nullable = false)
    private String numeroFacture;
    
    private LocalDateTime dateEmission = LocalDateTime.now();

    private double totalHT;
    private double montantTVA; 
    private double montantDA;  
    private double montantTE; 
    private double totalTTC;
    
    private String modePaiement;

    @Column(columnDefinition = "TEXT")
    private String qrCodeData; 

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL)
    private List<LigneFacture> lignes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
      name = "facture_taxes", 
      joinColumns = @JoinColumn(name = "facture_id"), 
      inverseJoinColumns = @JoinColumn(name = "taxe_id"))
    private List<Taxe> taxesAppliquees = new ArrayList<>();

    // Méthode utilitaire conservée pour la logique métier
    public void addLigne(LigneFacture ligne) {
        if (lignes == null) lignes = new ArrayList<>();
        lignes.add(ligne);
        ligne.setFacture(this);
    }
}