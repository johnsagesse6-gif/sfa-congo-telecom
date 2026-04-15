package com.sfa.congo_telecom.config;

import com.sfa.congo_telecom.model.Taxe;
import com.sfa.congo_telecom.model.Client;
import com.sfa.congo_telecom.model.Article;
import com.sfa.congo_telecom.repository.TaxeRepository;
import com.sfa.congo_telecom.repository.ClientRepository;
import com.sfa.congo_telecom.repository.ArticleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TaxeRepository taxeRepository;
    private final ClientRepository clientRepository;
    private final ArticleRepository articleRepository;

    @Override
    public void run(String... args) throws Exception {
        
        // 1. Initialisation des Taxes
        if (taxeRepository.count() == 0) {
            taxeRepository.save(new Taxe("TVA", 18.0, "POURCENTAGE"));
            taxeRepository.save(new Taxe("DA", 2.0, "POURCENTAGE"));
            taxeRepository.save(new Taxe("TIMBRE", 50.0, "FIXE"));
            System.out.println(">>> Taxes initialisées.");
        }

        // 2. Initialisation des Clients (Correction ici)
        if (clientRepository.count() == 0) {
            // Client 1 : Yoco Yoco
            Client c1 = new Client();
            c1.setNumeroClient("CLI-001"); // OBLIGATOIRE
            c1.setRaisonSociale("Yoco Yoco"); // OBLIGATOIRE
            c1.setRue("Avenue Amilcar Cabral");
            c1.setArrondissement("1 Makélékélé");
            c1.setVille("Brazzaville");
            c1.setFactures(new ArrayList<>());
            clientRepository.save(c1);

            // Client 2 : SFE Congo
            Client c2 = new Client();
            c2.setNumeroClient("CLI-002"); // OBLIGATOIRE
            c2.setRaisonSociale("SFE Congo"); // OBLIGATOIRE
            c2.setRue("Rond-point Lumumba");
            c2.setArrondissement("1 Lumumba");
            c2.setVille("Pointe-Noire");
            c2.setFactures(new ArrayList<>());
            clientRepository.save(c2);
            
            System.out.println(">>> Clients ajoutés avec succès.");
        }

        // 3. Initialisation des Articles
        if (articleRepository.count() == 0) {
            // Adapte ici selon tes champs réels dans Article.java
            // Si tu as des erreurs ici, utilise aussi les setters
            System.out.println(">>> Initialisation des articles...");
        }
    }
}