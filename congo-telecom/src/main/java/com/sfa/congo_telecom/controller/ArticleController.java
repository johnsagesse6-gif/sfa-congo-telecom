package com.sfa.congo_telecom.controller;

import com.sfa.congo_telecom.model.Article;
import com.sfa.congo_telecom.service.ArticleService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin("*") // Utile si tu connectes un front-end plus tard
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<Article> list() {
        return articleService.getAllArticles();
    }
}