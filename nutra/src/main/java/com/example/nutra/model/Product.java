package com.example.nutra.model;

import com.example.nutra.config.Base64ImageDeserializer;
import com.example.nutra.config.Base64ImageSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
    @CollectionTable(name = "product_featured_images", joinColumns = @JoinColumn(name = "product_id"))
    @Lob
    @Column(name = "feature_image_data", columnDefinition = "LONGBLOB")
    @JsonDeserialize(contentUsing = Base64ImageDeserializer.class)
    @JsonSerialize(contentUsing = Base64ImageSerializer.class)
    private List<byte[]> featuredImages;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @JsonDeserialize(using = Base64ImageDeserializer.class)
    @JsonSerialize(using = Base64ImageSerializer.class)
    private byte[] singleProductImage;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @JsonDeserialize(using = Base64ImageDeserializer.class)
    @JsonSerialize(using = Base64ImageSerializer.class)
    private byte[] twoProductImage;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @JsonDeserialize(using = Base64ImageDeserializer.class)
    @JsonSerialize(using = Base64ImageSerializer.class)
    private byte[] threeProductImage;

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
    @Column(name = "freebie", columnDefinition = "TEXT")
    private List<String> freebies;

    @ElementCollection
    @CollectionTable(name = "product_how_to_use", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "step", columnDefinition = "TEXT")
    private List<String> howToUse;

    @ElementCollection
    @CollectionTable(name = "product_faqs", joinColumns = @JoinColumn(name = "product_id"))
    private List<FAQ> faqs;

    public void addReview(Review review) {
        if (reviews == null) {
            reviews = new java.util.ArrayList<>();
        }
        reviews.add(review);
        review.setProduct(this);
    }
}
