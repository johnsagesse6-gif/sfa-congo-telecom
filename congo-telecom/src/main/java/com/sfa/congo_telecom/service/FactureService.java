package com.sfa.congo_telecom.service;

import com.sfa.congo_telecom.dto.FactureRequestDTO;
import com.sfa.congo_telecom.dto.LigneFactureDTO;
import com.sfa.congo_telecom.model.*;
import com.sfa.congo_telecom.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FactureService {

    private final FactureRepository factureRepository;
    private final TaxeRepository taxeRepository;
    private final ClientRepository clientRepository;
    private final ArticleRepository articleRepository;

    public FactureService(FactureRepository factureRepository, TaxeRepository taxeRepository, 
                          ClientRepository clientRepository, ArticleRepository articleRepository) {
        this.factureRepository = factureRepository;
        this.taxeRepository = taxeRepository;
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepository;
    }

    /**
     * Crée une nouvelle facture à partir du DTO reçu du formulaire
     */
    @Transactional
    public Facture creerFacture(FactureRequestDTO dto) {
        Facture facture = new Facture();
        
        // 1. Récupération du client
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));
        facture.setClient(client);
        facture.setModePaiement(dto.getModePaiement());

        // 2. Traitement des lignes de facture
        List<LigneFacture> lignesEntite = new ArrayList<>();
        double totalHT = 0;

        for (LigneFactureDTO ligneDto : dto.getLignes()) {
            Article article = articleRepository.findById(ligneDto.getArticleId())
                    .orElseThrow(() -> new RuntimeException("Article introuvable"));
            
            LigneFacture ligne = new LigneFacture();
            ligne.setArticle(article);
            ligne.setQuantite(ligneDto.getQuantite());
            ligne.setPrixAppliqueHT(article.getPrixUnitaireHT());
            ligne.setFacture(facture);
            
            totalHT += ligne.getPrixAppliqueHT() * ligne.getQuantite();
            lignesEntite.add(ligne);
        }
        
        facture.setLignes(lignesEntite);
        facture.setTotalHT(totalHT);

        // 3. Calcul automatique des taxes (TVA, DA, TE)
        double cumuleTaxes = 0;
        List<Taxe> taxesActives = taxeRepository.findAll(); 

        for (Taxe taxe : taxesActives) {
            double montant = (taxe.getTypeTaxe() != null && taxe.getTypeTaxe().equalsIgnoreCase("POURCENTAGE")) 
                             ? totalHT * (taxe.getValeur() / 100) 
                             : taxe.getValeur();

            // Attribution aux colonnes spécifiques de la table Facture
            if ("TVA".equalsIgnoreCase(taxe.getLibelle())) facture.setMontantTVA(montant);
            else if ("DA".equalsIgnoreCase(taxe.getLibelle())) facture.setMontantDA(montant);
            else if ("TE".equalsIgnoreCase(taxe.getLibelle())) facture.setMontantTE(montant);

            cumuleTaxes += montant;
        }

        // 4. Calcul final et métadonnées
        facture.setTotalTTC(totalHT + cumuleTaxes);
        facture.setNumeroFacture("FAC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        // Données pour le futur QR Code
        facture.setQrCodeData(String.format("CT-SFA|%s|%s|TTC:%.0f", 
                facture.getNumeroFacture(), client.getRaisonSociale(), facture.getTotalTTC()));

        return factureRepository.save(facture);
    }

    // --- MÉTHODES POUR LE DASHBOARD & VUES ---

    /**
     * Retourne la liste de toutes les factures
     */
    public List<Facture> rechercherTous() {
        return factureRepository.findAll();
    }

    /**
     * Recherche une facture spécifique pour le PDF
     */
    public Facture rechercherParId(Long id) {
        return factureRepository.findById(id).orElse(null);
    }

    /**
     * Compte le nombre total de factures (Temps Réel)
     */
    public long compterToutes() {
        return factureRepository.count();
    }

    /**
     * Calcule la somme totale générée (Chiffre d'Affaires)
     */
    public double calculerChiffreAffaireTotal() {
        return factureRepository.findAll().stream()
                .mapToDouble(Facture::getTotalTTC)
                .sum();
    }
}