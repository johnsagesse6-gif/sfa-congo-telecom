package com.sfa.congo_telecom.controller;

import com.sfa.congo_telecom.dto.FactureRequestDTO;
import com.sfa.congo_telecom.model.Facture;
import com.sfa.congo_telecom.service.FactureService;
import com.sfa.congo_telecom.service.ArticleService;
import com.sfa.congo_telecom.service.ClientService; // 1. Ajout du service client
import com.sfa.congo_telecom.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/factures")
public class FactureController {

    private final FactureService factureService;
    private final PdfService pdfService;
    private final ArticleService articleService;
    private final ClientService clientService; // 2. Déclaration du service client

    // 3. Mise à jour du constructeur
    public FactureController(FactureService factureService, 
                             PdfService pdfService, 
                             ArticleService articleService, 
                             ClientService clientService) {
        this.factureService = factureService;
        this.pdfService = pdfService;
        this.articleService = articleService;
        this.clientService = clientService;
    }

    @GetMapping("/creer")
    public String afficherFormulaire(Model model) {
        // 4. On envoie les clients ET les articles à la vue
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("articles", articleService.getAllArticles());
        model.addAttribute("factureDto", new FactureRequestDTO());
        return "creer-facture"; 
    }

    @PostMapping("/creer")
    public String create(@ModelAttribute("factureDto") FactureRequestDTO factureDto) {
        Facture factureGeneree = factureService.createFactureFromDTO(factureDto);
        // On redirige vers le PDF pour l'imprimer immédiatement
        return "redirect:/factures/" + factureGeneree.getId() + "/pdf";
    }

    @GetMapping("/{id}/pdf")
    public void downloadPdf(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Facture facture = factureService.getFactureById(id);
        if (facture != null) {
            response.setContentType("application/pdf");
            String fileName = "Facture_" + facture.getNumeroFacture() + ".pdf";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            pdfService.generate(facture, response);
        }
    }
}