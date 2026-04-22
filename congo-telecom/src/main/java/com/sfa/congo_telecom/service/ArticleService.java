package com.sfa.congo_telecom.service;

import com.sfa.congo_telecom.model.Article;
import com.sfa.congo_telecom.repository.ArticleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // Utilisé par le ViewController
    public List<Article> rechercherTous() {
        return articleRepository.findAll();
    }

    // AJOUTE CETTE MÉTHODE (Ligne 20 dans l'erreur)
    public void sauvegarder(Article article) {
        articleRepository.save(article);
    }

    // AJOUTE CETTE MÉTHODE (Ligne 26 dans l'erreur)
    public void supprimer(Long id) {
        articleRepository.deleteById(id);
    }

    public List<Article> getAllArticles() {
        return rechercherTous();
    }
}