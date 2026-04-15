package com.sfa.congo_telecom.repository;

import com.sfa.congo_telecom.model.Taxe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface TaxeRepository extends JpaRepository<Taxe, Long> {
    
    // Pour la recherche par nom (ex: TVA, DA)
    Optional<Taxe> findByLibelle(String libelle);

    // INDISPENSABLE : Pour récupérer toutes les taxes applicables (TVA, DA, TE)
    // Cela corrigera l'erreur dans TaxeService.java
    List<Taxe> findByActifTrue();
}