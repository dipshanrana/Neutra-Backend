package com.example.nutra.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NutrientInfo {
    private String nutrientName;
    private String amountPerServing;
    private String amount;
}
