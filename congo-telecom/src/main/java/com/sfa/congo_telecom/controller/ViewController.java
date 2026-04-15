package com.sfa.congo_telecom.controller;

import com.sfa.congo_telecom.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final ArticleService articleService;

    // Constructeur pour l'injection des services nécessaires
    public ViewController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * Page d'accueil (index.html)
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }

    /**
     * Page de visualisation des articles (articles.html)
     */
    @GetMapping("/articles")
    public String viewArticles(Model model) {
        // On utilise la méthode de ton service : getAllArticles()
        model.addAttribute("articles", articleService.getAllArticles());
        return "articles";
    }

    /* NOTE : Les méthodes concernant la création de facture ont été déplacées 
       dans FactureController.java pour éviter l'erreur "Ambiguous mapping".
    */
}