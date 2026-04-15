package com.sfa.congo_telecom.repository;

import com.sfa.congo_telecom.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    
    // Une méthode pratique pour retrouver un article par son code (ex: FIBRE-PRO-50M)
    Optional<Article> findByCodeArticle(String codeArticle);
}