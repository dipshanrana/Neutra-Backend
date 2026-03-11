package com.example.nutra.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductBenefit {

    @Column(columnDefinition = "TEXT")
    private String svg;

    @Column(columnDefinition = "TEXT")
    private String benefitDescription;
}
