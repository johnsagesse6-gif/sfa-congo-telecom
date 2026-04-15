package com.sfa.congo_telecom.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "certifications_arpce")
public class CertificationARPCE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String certificatNum; // Le numéro officiel délivré par l'ARPCE

    @Lob
    @Column(columnDefinition = "TEXT")
    private String qrCode; // Les données textuelles pour générer le QR Code fiscal

    private LocalDateTime dateCertification;

    @OneToOne
    @JoinColumn(name = "facture_id", nullable = false)
    private Facture facture;

    public CertificationARPCE() {}

    // Getters et Setters
    public Long getId() { return id; }
    
    public String getCertificatNum() { return certificatNum; }
    public void setCertificatNum(String certificatNum) { this.certificatNum = certificatNum; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public LocalDateTime getDateCertification() { return dateCertification; }
    public void setDateCertification(LocalDateTime dateCertification) { this.dateCertification = dateCertification; }

    public Facture getFacture() { return facture; }
    public void setFacture(Facture facture) { this.facture = facture; }
}