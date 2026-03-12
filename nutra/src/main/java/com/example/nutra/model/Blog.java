package com.example.nutra.model;

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

    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    // A blog can be related to single or multiple products
    @ManyToMany
    @JoinTable(name = "blog_product", joinColumns = @JoinColumn(name = "blog_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> relatedProducts;
}
