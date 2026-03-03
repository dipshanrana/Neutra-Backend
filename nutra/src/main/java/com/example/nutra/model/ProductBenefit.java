package com.example.nutra.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductBenefit {

    @Column(columnDefinition = "TEXT")
    private String svg;

    private String nutrientName;

    @Column(columnDefinition = "TEXT")
    private String benefitDescription;
}
