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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String link;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double singleProductMp; // Marked Price for Single Product
    private Double singleProductSp; // Selling Price for Single Product

    private Double twoProductMp; // Marked Price for Two Products
    private Double twoProductSp; // Selling Price for Two Products

    private Double threeProductMp; // Marked Price for Three Products
    private Double threeProductSp; // Selling Price for Three Products

    private Double discount;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private List<String> images;

    @ElementCollection
    @CollectionTable(name = "product_featured_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "feature_image_url", columnDefinition = "LONGTEXT")
    private List<String> featuredImages;

    @Column(columnDefinition = "LONGTEXT")
    private String singleProductImage;

    @Column(columnDefinition = "LONGTEXT")
    private String twoProductImage;

    @Column(columnDefinition = "LONGTEXT")
    private String threeProductImage;

    @ElementCollection
    @CollectionTable(name = "product_benefits", joinColumns = @JoinColumn(name = "product_id"))
    private List<ProductBenefit> benefits;

    private String servingSize;

    private String capsulesPerContainer;

    @ElementCollection
    @CollectionTable(name = "product_supplement_facts", joinColumns = @JoinColumn(name = "product_id"))
    private List<NutrientInfo> supplementFacts;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @ElementCollection
    @CollectionTable(name = "product_freebies", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "freebie")
    private List<String> freebies;

    public void addReview(Review review) {
        if (reviews == null) {
            reviews = new java.util.ArrayList<>();
        }
        reviews.add(review);
        review.setProduct(this);
    }
}
