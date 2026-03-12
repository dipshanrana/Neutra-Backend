package com.example.nutra.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    private String image;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Information> informationPages;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Blog> blogs;
}
