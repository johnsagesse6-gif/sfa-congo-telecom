package com.sfa.congo_telecom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_client", unique = true, nullable = false)
    private String numeroClient;

    @Column(name = "raison_sociale", nullable = false)
    private String raisonSociale;

    private String rue;
    private String arrondissement;
    private String ville;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Facture> factures = new ArrayList<>();
}