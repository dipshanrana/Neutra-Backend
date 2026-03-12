package com.example.nutra.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Information {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String image;

    // As requested: information model which will be linked to all categories
    // Rather than mapping a many-to-many back to Category, usually an informational
    // article or text stands on its own and implicitly relates to the rest of the
    // store.
    // However, if a direct relation to a specific category is meant, we could link
    // it:

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

}
