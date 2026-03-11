package com.example.nutra.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import com.example.nutra.config.Base64ImageDeserializer;
import com.example.nutra.config.Base64ImageSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Lob;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "image")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String svg;

    private String badge;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @JsonSerialize(using = Base64ImageSerializer.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private byte[] image;
}
