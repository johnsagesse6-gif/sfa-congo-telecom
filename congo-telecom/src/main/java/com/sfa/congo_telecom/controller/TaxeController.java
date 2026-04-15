package com.sfa.congo_telecom.controller;

import com.sfa.congo_telecom.model.Taxe;
import com.sfa.congo_telecom.service.TaxeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taxes")
public class TaxeController {

    private final TaxeService taxeService;

    public TaxeController(TaxeService taxeService) {
        this.taxeService = taxeService;
    }

    /**
     * Liste toutes les taxes enregistrées (TVA, DA, Timbre, etc.)
     */
    @GetMapping
    public List<Taxe> getAllTaxes() {
        return taxeService.getAllTaxes();
    }

    /**
     * Liste uniquement les taxes actuellement appliquées aux factures
     */
    @GetMapping("/actives")
    public List<Taxe> getActiveTaxes() {
        return taxeService.getTaxesActives();
    }

    /**
     * Met à jour la valeur d'une taxe (ex: modifier le taux de TVA)
     * Utile pour l'administration du SFA
     */
    @PutMapping("/{id}")
    public ResponseEntity<Taxe> updateTaxe(@PathVariable Long id, @RequestParam double valeur) {
        try {
            Taxe updatedTaxe = taxeService.updateTaxe(id, valeur);
            return ResponseEntity.ok(updatedTaxe);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}