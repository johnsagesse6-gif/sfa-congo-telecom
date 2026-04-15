package com.sfa.congo_telecom.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell; // Import ajouté
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
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // --- STYLE DES POLICES ---
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);

        // --- EN-TÊTE ---
        Paragraph title = new Paragraph("CONGO TELECOM - SFA", fontTitle);
        title.setAlignment(Element.ALIGN_LEFT);
        document.add(title);
        document.add(new Paragraph("Direction Commerciale - Brazzaville", fontNormal));
        document.add(new Paragraph(" "));

        // --- INFOS FACTURE ET CLIENT ---
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        // Suppression de setBorderWidth qui n'existe pas sur PdfPTable

        // Colonne Gauche : Détails Facture
        PdfPCell cellFacture = new PdfPCell();
        cellFacture.setBorder(Rectangle.NO_BORDER);
        cellFacture.addElement(new Paragraph("Reçu N° : " + facture.getNumeroFacture(), fontBold));
        cellFacture.addElement(new Paragraph("Date : " + facture.getDateEmission(), fontNormal));
        cellFacture.addElement(new Paragraph("Mode : " + facture.getModePaiement(), fontNormal));
        infoTable.addCell(cellFacture);

        // Colonne Droite : Infos Client
        PdfPCell cellClient = new PdfPCell();
        cellClient.setBorder(Rectangle.NO_BORDER);
        cellClient.addElement(new Paragraph("CLIENT : " + facture.getClient().getRaisonSociale(), fontBold));
        cellClient.addElement(new Paragraph("N° Client : " + facture.getClient().getNumeroClient(), fontNormal));
        cellClient.addElement(new Paragraph("Adresse : " + facture.getClient().getRue() + ", " + facture.getClient().getVille(), fontNormal));
        infoTable.addCell(cellClient);

        document.add(infoTable);
        document.add(new Paragraph(" "));

        // --- TABLEAU DES ARTICLES ---
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        
        table.addCell(new Phrase("Désignation", fontBold));
        table.addCell(new Phrase("Qté", fontBold));
        table.addCell(new Phrase("P.U HT", fontBold));
        table.addCell(new Phrase("Total HT", fontBold));

        for (LigneFacture ligne : facture.getLignes()) {
            table.addCell(new Phrase(ligne.getArticle().getDesignation(), fontNormal));
            table.addCell(new Phrase(String.valueOf(ligne.getQuantite()), fontNormal));
            table.addCell(new Phrase(String.format("%,.0f", ligne.getPrixAppliqueHT()), fontNormal));
            table.addCell(new Phrase(String.format("%,.0f", (double)ligne.getQuantite() * ligne.getPrixAppliqueHT()), fontNormal));
        }
        document.add(table);

        // --- RÉCAPITULATIF FISCAL ---
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(40);
        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalTable.setSpacingBefore(20f);

        addTotalLine(totalTable, "TOTAL HT", facture.getTotalHT(), fontNormal);
        addTotalLine(totalTable, "TVA (18%)", facture.getMontantTVA(), fontNormal);
        addTotalLine(totalTable, "DA (5%)", facture.getMontantDA(), fontNormal);
        addTotalLine(totalTable, "TIMBRE (T.E)", 50.0, fontNormal);
        addTotalLine(totalTable, "TOTAL TTC", facture.getTotalTTC(), fontBold);

        document.add(totalTable);

        // --- SÉCURITÉ ARPCE (QR CODE) ---
        try {
            document.add(new Paragraph(" "));
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            // Utilisation d'une méthode de secours si getQrCodeData() n'est pas encore généré par Lombok
            String qrData = "Facture:" + facture.getNumeroFacture() + "|Client:" + facture.getClient().getRaisonSociale();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, 100, 100);
            
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            
            Image qrCodeImage = Image.getInstance(pngOutputStream.toByteArray());
            qrCodeImage.setAlignment(Element.ALIGN_LEFT);
            document.add(qrCodeImage);
            document.add(new Paragraph("Certifié par l'ARPCE", FontFactory.getFont(FontFactory.HELVETICA, 7)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        document.close();
    }

    private void addTotalLine(PdfPTable table, String label, double value, Font font) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, font));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(String.format("%,.0f FCFA", value), font));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellValue);
    }
}