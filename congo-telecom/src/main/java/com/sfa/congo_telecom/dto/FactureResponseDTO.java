package com.sfa.congo_telecom.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FactureResponseDTO {
    private Long id;
    private String numeroFacture;
    private LocalDateTime dateEmission;
    private String raisonSocialeClient;
    private double totalHT;
    private double montantTVA; // Utile pour le récapitulatif fiscal
    private double totalTTC;
    private String statut; 
    private String modePaiement; // Pour savoir si c'est du CASH ou AIRTEL MONEY
}