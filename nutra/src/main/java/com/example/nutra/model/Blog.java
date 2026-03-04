package com.example.nutra.model;

import com.example.nutra.config.Base64ImageSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String author;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @JsonSerialize(using = Base64ImageSerializer.class)
    private byte[] image;

    // A blog can be related to single or multiple products
    @ManyToMany
    @JoinTable(name = "blog_product", joinColumns = @JoinColumn(name = "blog_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> relatedProducts;
}
