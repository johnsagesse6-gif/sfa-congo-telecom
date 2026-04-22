package com.sfa.congo_telecom.repository;

import com.sfa.congo_telecom.model.Facture;
import com.sfa.congo_telecom.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

    // Trouver une facture par son numéro unique (ex: FAC-A1B2C3D4)
    Optional<Facture> findByNumeroFacture(String numeroFacture);

    // Récupérer toutes les factures d'un client spécifique
    List<Facture> findByClient(Client client);

    // Récupérer les factures triées par date d'émission (la plus récente en premier)
    List<Facture> findAllByOrderByDateEmissionDesc();

    // Compter le nombre de factures (déjà inclus dans JpaRepository, mais utile à savoir)
    long count();
}