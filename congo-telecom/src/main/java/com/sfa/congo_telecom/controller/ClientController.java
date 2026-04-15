package com.sfa.congo_telecom.controller;

import com.sfa.congo_telecom.model.Client;
import com.sfa.congo_telecom.service.ClientService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin("*")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> list() {
        return clientService.getAllClients();
    }

    @PostMapping
    public Client save(@RequestBody Client client) {
        return clientService.saveClient(client);
    }

    @GetMapping("/search")
    public Client findByNum(@RequestParam String numeroClient) {
        return clientService.findByNumeroClient(numeroClient)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
    }
}