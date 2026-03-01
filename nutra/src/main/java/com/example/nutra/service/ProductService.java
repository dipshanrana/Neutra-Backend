package com.example.nutra.service;

import com.example.nutra.model.Category;
import com.example.nutra.model.Product;
import com.example.nutra.repository.ProductRepository;
import com.example.nutra.repository.CategoryRepository;
import com.example.nutra.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Product addProduct(Product product) {
        if (product.getCategory() != null) {
            Category cat = categoryRepository.findByNameIgnoreCase(product.getCategory().getName());
            if (cat == null) {
                cat = categoryRepository.save(product.getCategory());
            }
            product.setCategory(cat);
        }
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }

    public List<Product> getProductsFromThreeCategories() {
        List<String> categories = productRepository.findAllCategories();
        Collections.shuffle(categories);
        return categories.stream()
                .limit(3)
                .map(category -> productRepository.findFirstByCategoryNameIgnoreCase(category))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Product> getAllProductsRandomly() {
        return productRepository.findAllRandomly();
    }

    public List<Product> searchByCategory(String category) {
        return productRepository.findByCategoryNameIgnoreCase(category);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> searchByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findBySpBetween(minPrice, maxPrice);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(productDetails.getName());

        if (productDetails.getCategory() != null) {
            Category cat = categoryRepository.findByNameIgnoreCase(productDetails.getCategory().getName());
            if (cat == null) {
                cat = categoryRepository.save(productDetails.getCategory());
            }
            existingProduct.setCategory(cat);
        }

        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setMp(productDetails.getMp());
        existingProduct.setSp(productDetails.getSp());
        existingProduct.setDiscount(productDetails.getDiscount());
        if (productDetails.getImages() != null && !productDetails.getImages().isEmpty()) {
            existingProduct.setImages(productDetails.getImages());
        }
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
