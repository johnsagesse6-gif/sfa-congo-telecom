package com.sfa.congo_telecom.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "factures")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroFacture;
    private LocalDateTime dateEmission = LocalDateTime.now();
    private double totalTTC;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL)
    private List<LigneFacture> lignes = new ArrayList<>();

    public Facture() {}

    // Getters et Setters
    public Long getId() { return id; }
    public String getNumeroFacture() { return numeroFacture; }
    public void setNumeroFacture(String numeroFacture) { this.numeroFacture = numeroFacture; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public List<LigneFacture> getLignes() { return lignes; }
}