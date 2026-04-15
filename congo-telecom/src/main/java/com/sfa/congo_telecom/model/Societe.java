package com.sfa.congo_telecom.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "societe")
@Data
public class Societe {
    @Id
    private Long id = 1L; // Souvent une seule ligne en base

    private String nom; // Congo Telecom
    private String siegeSocial;
    private String niu;
    private String rccm;
    private String telephone;
    private String email;
}