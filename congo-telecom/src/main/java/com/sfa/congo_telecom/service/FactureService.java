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

    @Transactional
    public Facture createFactureFromDTO(FactureRequestDTO dto) {
        Facture facture = new Facture();
        
        // 1. Récupération du Client (YOCO YOCO)
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));
        facture.setClient(client);
        facture.setModePaiement(dto.getModePaiement());

        // 2. Transformation des LignesDTO en Lignes d'entité
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

        // 3. Calcul des Taxes (Dynamique via la 7ème classe)
        double cumuleTaxes = 0;
        List<Taxe> taxesActives = taxeRepository.findAll(); // Ou findByActifTrue()

        for (Taxe taxe : taxesActives) {
            double montant = (taxe.getTypeTaxe().equalsIgnoreCase("POURCENTAGE")) 
                             ? totalHT * (taxe.getValeur() / 100) 
                             : taxe.getValeur();

            if ("TVA".equalsIgnoreCase(taxe.getLibelle())) facture.setMontantTVA(montant);
            else if ("DA".equalsIgnoreCase(taxe.getLibelle())) facture.setMontantDA(montant);
            else if ("TE".equalsIgnoreCase(taxe.getLibelle())) facture.setMontantTE(montant);

            cumuleTaxes += montant;
        }

        facture.setTotalTTC(totalHT + cumuleTaxes);
        facture.setNumeroFacture("FAC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // 4. Génération des données QR Code
        facture.setQrCodeData(String.format("CT-SFA|%s|%s|TTC:%.0f", 
                facture.getNumeroFacture(), client.getRaisonSociale(), facture.getTotalTTC()));

        return factureRepository.save(facture);
    }

    public List<Facture> getAllFactures() {
        return factureRepository.findAll();
    }

    public Facture getFactureById(Long id) {
        return factureRepository.findById(id).orElse(null);
    }
}