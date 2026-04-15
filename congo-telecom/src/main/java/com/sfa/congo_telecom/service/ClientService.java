package com.sfa.congo_telecom.service;

import com.sfa.congo_telecom.model.Client;
import com.sfa.congo_telecom.repository.ClientRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    /**
     * Recherche un client par son identifiant métier (ex: 1000044501)
     * Très utile pour l'Agent Commercial lors de la saisie d'une facture.
     */
    public Optional<Client> findByNumeroClient(String numeroClient) {
        return clientRepository.findByNumeroClient(numeroClient);
    }
}