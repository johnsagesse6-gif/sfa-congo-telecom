package com.sfa.congo_telecom.controller;

import com.sfa.congo_telecom.service.ClientService;
import com.sfa.congo_telecom.service.ArticleService;
import com.sfa.congo_telecom.service.FactureService; // Importé pour les stats
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final ClientService clientService;
    private final ArticleService articleService;
    private final FactureService factureService;

    public ViewController(ClientService clientService, ArticleService articleService, FactureService factureService) {
        this.clientService = clientService;
        this.articleService = articleService;
        this.factureService = factureService;
    }

    // 1. Dashboard avec compteurs en temps réel
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("currentPage", "index");
        
        // On récupère les tailles des listes pour le dashboard
        model.addAttribute("totalClients", clientService.rechercherTous().size());
        model.addAttribute("totalArticles", articleService.rechercherTous().size());
        // Assure-toi d'avoir une méthode compterToutes() dans FactureService (qui fait factureRepository.count())
        model.addAttribute("totalFactures", factureService.compterToutes()); 
        
        return "index";
    }

    // 2. Catalogue des Contacts Clients
    @GetMapping("/clients")
    public String catalogueClients(Model model) {
        model.addAttribute("clients", clientService.rechercherTous());
        model.addAttribute("currentPage", "clients");
        return "clients";
    }

    // 3. Catalogue des Services & Articles
    @GetMapping("/articles")
    public String catalogueArticles(Model model) {
        // C'est ici que l'on passe la liste pour th:each="a : ${articles}"
        model.addAttribute("articles", articleService.rechercherTous());
        model.addAttribute("currentPage", "articles");
        return "articles";
    }
}