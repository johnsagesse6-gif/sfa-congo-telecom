package com.sfa.congo_telecom.controller;

import com.sfa.congo_telecom.model.Article;
import com.sfa.congo_telecom.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/ajouter")
    public String ajouter(@ModelAttribute Article article) {
        articleService.sauvegarder(article);
        return "redirect:/articles";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        articleService.supprimer(id);
        return "redirect:/articles";
    }
}