package com.sfa.congo_telecom.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sfa.congo_telecom.model.Facture;
import com.sfa.congo_telecom.model.LigneFacture;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfService {

    public void generate(Facture facture, HttpServletResponse response) throws IOException {
        // 1. Création du document A4
        Document document = new Document(PageSize.A4);
        
        try {
            // Liaison du document au flux de sortie de la réponse HTTP
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // --- STYLE DES POLICES ---
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.BOLD);
            Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Font fontMini = FontFactory.getFont(FontFactory.HELVETICA, 8);

            // --- EN-TÊTE ---
            Paragraph title = new Paragraph("CONGO TELECOM - SFA", fontTitle);
            title.setSpacingAfter(5);
            document.add(title);
            document.add(new Paragraph("Direction Commerciale - Brazzaville, République du Congo", fontNormal));
            document.add(new Paragraph("Support client : +242 06 600 00 00", fontMini));
            document.add(new Paragraph(" ")); // Saut de ligne

            // --- BLOC INFOS (TABLEAU SANS BORDURES) ---
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);

            // Gauche : Détails Facture
            PdfPCell cellFacture = new PdfPCell();
            cellFacture.setBorder(Rectangle.NO_BORDER);
            cellFacture.addElement(new Paragraph("FACTURE N° : " + facture.getNumeroFacture(), fontBold));
            cellFacture.addElement(new Paragraph("Date d'émission : " + facture.getDateEmission(), fontNormal));
            cellFacture.addElement(new Paragraph("Mode de règlement : " + facture.getModePaiement(), fontNormal));
            infoTable.addCell(cellFacture);

            // Droite : Détails Client
            PdfPCell cellClient = new PdfPCell();
            cellClient.setBorder(Rectangle.NO_BORDER);
            cellClient.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellClient.addElement(new Paragraph("DESTINATAIRE :", fontBold));
            cellClient.addElement(new Paragraph(facture.getClient().getRaisonSociale(), fontBold));
            cellClient.addElement(new Paragraph("ID Client : " + facture.getClient().getNumeroClient(), fontNormal));
            cellClient.addElement(new Paragraph(facture.getClient().getRue() + ", " + facture.getClient().getVille(), fontNormal));
            infoTable.addCell(cellClient);

            document.add(infoTable);
            document.add(new Paragraph(" "));

            // --- TABLEAU DES ARTICLES ---
            PdfPTable table = new PdfPTable(new float[]{4, 1, 2, 2}); // Proportions des colonnes
            table.setWidthPercentage(100);
            
            // Header du tableau
            String[] headers = {"Désignation", "Qté", "P.U HT (FCFA)", "Total HT (FCFA)"};
            for (String h : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(h, fontBold));
                headerCell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
                headerCell.setPadding(5);
                table.addCell(headerCell);
            }

            // Contenu du tableau
            for (LigneFacture ligne : facture.getLignes()) {
                table.addCell(new Phrase(ligne.getArticle().getDesignation(), fontNormal));
                table.addCell(new Phrase(String.valueOf(ligne.getQuantite()), fontNormal));
                table.addCell(new Phrase(String.format("%,.0f", ligne.getPrixAppliqueHT()), fontNormal));
                
                double totalLigne = (double)ligne.getQuantite() * ligne.getPrixAppliqueHT();
                table.addCell(new Phrase(String.format("%,.0f", totalLigne), fontNormal));
            }
            document.add(table);

            // --- RÉCAPITULATIF FISCAL ---
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(45);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.setSpacingBefore(15f);

            addTotalLine(totalTable, "TOTAL HORS TAXE", facture.getTotalHT(), fontNormal);
            addTotalLine(totalTable, "TVA (18%)", facture.getMontantTVA(), fontNormal);
            addTotalLine(totalTable, "DA (5%)", facture.getMontantDA(), fontNormal);
            addTotalLine(totalTable, "DROIT DE TIMBRE", 50.0, fontNormal); // Montant fixe
            addTotalLine(totalTable, "TOTAL TTC À PAYER", facture.getTotalTTC(), fontBold);

            document.add(totalTable);

            // --- ZONE DE SÉCURITÉ (QR CODE ARPCE) ---
            try {
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Certification Numérique ARPCE :", fontMini));
                
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                // On inclut le montant TTC dans le QR Code pour la vérification de fraude
                String qrData = "REF:" + facture.getNumeroFacture() + 
                                "|CLI:" + facture.getClient().getRaisonSociale() + 
                                "|TTC:" + facture.getTotalTTC() + " FCFA";
                
                BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, 90, 90);
                
                ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
                
                Image qrCodeImage = Image.getInstance(pngOutputStream.toByteArray());
                document.add(qrCodeImage);
                
                Paragraph footer = new Paragraph("Ce document est certifié par le système CT-Guardian. Toute falsification est passible de poursuites.", fontMini);
                footer.setFont(FontFactory.getFont(FontFactory.HELVETICA, 6, java.awt.Color.GRAY));
                document.add(footer);

            } catch (Exception e) {
                System.err.println("Erreur génération QR Code : " + e.getMessage());
            }

        } catch (DocumentException de) {
            throw new IOException(de.getMessage());
        } finally {
            // TRÈS IMPORTANT : Toujours fermer le document
            if(document.isOpen()) {
                document.close();
            }
        }
    }

    private void addTotalLine(PdfPTable table, String label, double value, Font font) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, font));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setPadding(3);
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(String.format("%,.0f FCFA", value), font));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellValue.setPadding(3);
        table.addCell(cellValue);
    }
}