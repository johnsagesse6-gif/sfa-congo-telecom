package com.sfa.congo_telecom.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_client", unique = true, nullable = false)
    private String numClient;

    @Column(name = "nom_client")
    private String nomClient;

    private String adresse;

    // Relation 1..* du schéma : Un client a plusieurs factures
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Facture> factures = new ArrayList<>();

    // Constructeurs
    public Client() {}

    public Client(String numClient, String nomClient, String adresse) {
        this.numClient = numClient;
        this.nomClient = nomClient;
        this.adresse = adresse;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public String getNumClient() { return numClient; }
    public void setNumClient(String numClient) { this.numClient = numClient; }
    public String getNomClient() { return nomClient; }
    public void setNomClient(String nomClient) { this.nomClient = nomClient; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public List<Facture> getFactures() { return factures; }
}