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
import java.util.List;

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
            taxeRepository.save(new Taxe(null, "TVA", 18.0, "POURCENTAGE", true));
            taxeRepository.save(new Taxe(null, "DA", 2.0, "POURCENTAGE", true));
            taxeRepository.save(new Taxe(null, "TIMBRE", 50.0, "FIXE", true));
            System.out.println(">>> Taxes initialisées.");
        }

        // 2. Initialisation des Clients
        if (clientRepository.count() == 0) {
            Client c1 = new Client();
            c1.setNumeroClient("CLI-001");
            c1.setRaisonSociale("Yoco Yoco");
            c1.setRue("Avenue Amilcar Cabral");
            c1.setArrondissement("1 Makélékélé");
            c1.setVille("Brazzaville");
            clientRepository.save(c1);

            Client c2 = new Client();
            c2.setNumeroClient("CLI-002");
            c2.setRaisonSociale("SFE Congo");
            c2.setRue("Rond-point Lumumba");
            c2.setArrondissement("1 Lumumba");
            c2.setVille("Pointe-Noire");
            clientRepository.save(c2);
            
            System.out.println(">>> Clients ajoutés.");
        }

        // 3. Initialisation des Articles
        if (articleRepository.count() == 0) {
            Article a1 = new Article();
            a1.setCodeArticle("FIBRE-100M");
            a1.setDesignation("Abonnement Fibre Optique 100 Mbps");
            a1.setPrixUnitaireHT(45000.0);
            articleRepository.save(a1);

            Article a2 = new Article();
            a2.setCodeArticle("INSTALL-FTTH");
            a2.setDesignation("Frais d'installation Fibre domicile");
            a2.setPrixUnitaireHT(15000.0);
            articleRepository.save(a2);

            System.out.println(">>> Articles ajoutés pour le test.");
        }
    }
}