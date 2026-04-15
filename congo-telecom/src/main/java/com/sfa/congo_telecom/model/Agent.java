package com.sfa.congo_telecom.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "agents")
@Data
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomComplet; // Ex: Nathalie OBAMBI

    @Column(unique = true)
    private String matricule; // Identifiant interne de l'agent

    @OneToMany(mappedBy = "agent")
    private List<Facture> factures;

    public Agent() {}
}