package com.example.nutra.repository;

import com.example.nutra.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p.category.name FROM Product p")
    List<String> findAllCategories();

    @Query(value = "SELECT * FROM product ORDER BY RAND()", nativeQuery = true)
    List<Product> findAllRandomly();

    List<Product> findByCategoryNameIgnoreCase(String categoryName);

    Product findFirstByCategoryNameIgnoreCase(String categoryName);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findBySingleProductSpBetween(Double minPrice, Double maxPrice);

    List<Product> findByBadgeIgnoreCase(String badge);

    List<Product> findByCategoryBadgeIgnoreCaseAndCategoryNameIgnoreCase(String categoryBadge, String categoryName);
}
