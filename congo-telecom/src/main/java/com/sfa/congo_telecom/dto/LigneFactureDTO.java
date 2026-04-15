package com.sfa.congo_telecom.dto;

import lombok.Data;

@Data
public class LigneFactureDTO {
    private Long articleId; // Doit correspondre exactement à l'appel getArticleId()
    private int quantite;
    private String description;
}