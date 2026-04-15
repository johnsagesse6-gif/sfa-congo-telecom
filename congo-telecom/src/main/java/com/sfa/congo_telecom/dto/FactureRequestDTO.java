package com.sfa.congo_telecom.dto;

import lombok.Data;
import java.util.List;

@Data
public class FactureRequestDTO {
    private Long clientId; // Assure-toi que c'est bien "clientId" et pas "idClient"
    private String modePaiement;
    private List<LigneFactureDTO> lignes;
}