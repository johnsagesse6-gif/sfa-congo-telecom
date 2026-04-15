package com.sfa.congo_telecom.service;

import com.sfa.congo_telecom.model.Taxe;
import com.sfa.congo_telecom.repository.TaxeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaxeService {

    private final TaxeRepository taxeRepository;

    public TaxeService(TaxeRepository taxeRepository) {
        this.taxeRepository = taxeRepository;
    }

    public List<Taxe> getTaxesActives() {
        return taxeRepository.findByActifTrue();
    }

    public List<Taxe> getAllTaxes() {
        return taxeRepository.findAll();
    }

    public Taxe updateTaxe(Long id, double nouvelleValeur) {
        Taxe taxe = taxeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Taxe introuvable"));
        taxe.setValeur(nouvelleValeur);
        return taxeRepository.save(taxe);
    }
}