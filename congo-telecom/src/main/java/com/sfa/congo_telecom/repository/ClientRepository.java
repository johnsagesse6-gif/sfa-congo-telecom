package com.sfa.congo_telecom.repository;

import com.sfa.congo_telecom.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Cette ligne est cruciale pour corriger l'erreur dans ClientService
    Optional<Client> findByNumeroClient(String numeroClient);
}