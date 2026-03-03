package com.example.nutra.service;

import com.example.nutra.model.Category;
import com.example.nutra.model.Product;
import com.example.nutra.model.Review;

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
        if (product.getReviews() != null) {
            for (Review r : product.getReviews()) {
                r.setProduct(product);
            }
        }
        if (product.getFeaturedImages() != null && product.getFeaturedImages().size() != 5) {
            throw new IllegalArgumentException("There must be exactly 5 featured images.");
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
        return productRepository.findBySingleProductSpBetween(minPrice, maxPrice);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(productDetails.getName());
        existingProduct.setLink(productDetails.getLink());

        if (productDetails.getCategory() != null) {
            Category cat = categoryRepository.findByNameIgnoreCase(productDetails.getCategory().getName());
            if (cat == null) {
                cat = categoryRepository.save(productDetails.getCategory());
            }
            existingProduct.setCategory(cat);
        }

        existingProduct.setDescription(productDetails.getDescription());

        existingProduct.setSingleProductMp(productDetails.getSingleProductMp());
        existingProduct.setSingleProductSp(productDetails.getSingleProductSp());

        existingProduct.setTwoProductMp(productDetails.getTwoProductMp());
        existingProduct.setTwoProductSp(productDetails.getTwoProductSp());

        existingProduct.setThreeProductMp(productDetails.getThreeProductMp());
        existingProduct.setThreeProductSp(productDetails.getThreeProductSp());

        existingProduct.setDiscount(productDetails.getDiscount());
        existingProduct.setImages(productDetails.getImages());
        existingProduct.setBenefits(productDetails.getBenefits());

        if (productDetails.getFeaturedImages() != null && productDetails.getFeaturedImages().size() != 5) {
            throw new IllegalArgumentException("There must be exactly 5 featured images.");
        }
        existingProduct.setFeaturedImages(productDetails.getFeaturedImages());
        existingProduct.setSingleProductImage(productDetails.getSingleProductImage());
        existingProduct.setTwoProductImage(productDetails.getTwoProductImage());
        existingProduct.setThreeProductImage(productDetails.getThreeProductImage());

        existingProduct.setServingSize(productDetails.getServingSize());
        existingProduct.setCapsulesPerContainer(productDetails.getCapsulesPerContainer());
        existingProduct.setSupplementFacts(productDetails.getSupplementFacts());
        existingProduct.setFreebies(productDetails.getFreebies());

        if (productDetails.getReviews() != null) {
            if (existingProduct.getReviews() == null) {
                existingProduct.setReviews(new java.util.ArrayList<>());
            } else {
                existingProduct.getReviews().clear();
            }
            for (Review r : productDetails.getReviews()) {
                r.setProduct(existingProduct);
                existingProduct.getReviews().add(r);
            }
        } else {
            if (existingProduct.getReviews() != null) {
                existingProduct.getReviews().clear();
            }
        }
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
