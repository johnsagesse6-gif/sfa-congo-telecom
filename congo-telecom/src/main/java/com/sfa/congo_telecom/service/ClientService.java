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

    // Utilisé par le ViewController pour afficher la liste des contacts
    public List<Client> rechercherTous() {
        return clientRepository.findAll();
    }

    // Utilisé par le ClientController pour l'ajout
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    // AJOUTE CETTE MÉTHODE : Elle manquait et causait l'erreur de compilation
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public Optional<Client> findByNumeroClient(String numeroClient) {
        return clientRepository.findByNumeroClient(numeroClient);
    }
    
    // Alias pour la compatibilité
    public List<Client> getAllClients() {
        return rechercherTous();
    }
}