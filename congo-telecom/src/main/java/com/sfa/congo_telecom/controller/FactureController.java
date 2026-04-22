package com.sfa.congo_telecom.controller;

import com.sfa.congo_telecom.dto.FactureRequestDTO; // Corrigé : suppression du "ESTDto" qui traînait
import com.sfa.congo_telecom.model.Facture;
import com.sfa.congo_telecom.service.FactureService;
import com.sfa.congo_telecom.service.ClientService;
import com.sfa.congo_telecom.service.ArticleService;
import com.sfa.congo_telecom.service.PdfService; 
import jakarta.servlet.http.HttpServletResponse; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/factures")
public class FactureController {

    @Autowired
    private FactureService factureService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private PdfService pdfService; 

    @GetMapping("/creer")
    public String afficherFormulaire(Model model) {
        // Utilisation de FactureRequestDTO (nom correct selon ton tree)
        model.addAttribute("factureDto", new FactureRequestDTO());
        model.addAttribute("clients", clientService.rechercherTous());
        model.addAttribute("articles", articleService.rechercherTous());
        model.addAttribute("currentPage", "facture"); 
        return "creer-facture";
    }

    @PostMapping("/creer")
    public String enregistrerFacture(@ModelAttribute("factureDto") FactureRequestDTO dto, Model model) {
        try {
            Facture f = factureService.creerFacture(dto);
            
            model.addAttribute("factureId", f.getId());
            model.addAttribute("factureRef", f.getNumeroFacture());
            model.addAttribute("clientNom", f.getClient().getRaisonSociale());
            model.addAttribute("currentPage", "facture");
            
            return "confirmation";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur : " + e.getMessage());
            model.addAttribute("clients", clientService.rechercherTous());
            model.addAttribute("articles", articleService.rechercherTous());
            return "creer-facture";
        }
    }

    @GetMapping("/telecharger/{id}")
    public void telechargerPdf(@PathVariable Long id, HttpServletResponse response) {
        try {
            Facture facture = factureService.rechercherParId(id);
            if (facture != null) {
                // 1. Configurer la réponse pour un fichier PDF
                response.setContentType("application/pdf");
                String headerKey = "Content-Disposition";
                String headerValue = "attachment; filename=Facture_CT_" + facture.getNumeroFacture() + ".pdf";
                response.setHeader(headerKey, headerValue);

                // 2. Utiliser ton PdfService pour écrire dans le flux de la réponse
                pdfService.generate(facture, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}