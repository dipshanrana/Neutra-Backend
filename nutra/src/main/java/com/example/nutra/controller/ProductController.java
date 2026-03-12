package com.example.nutra.controller;

import com.example.nutra.model.Product;
import com.example.nutra.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> addProduct(
            @RequestPart("product") Product product,
            @RequestPart(value = "featuredImages", required = false) List<MultipartFile> featuredImages,
            @RequestPart(value = "singleProductImage", required = false) MultipartFile singleProductImage,
            @RequestPart(value = "twoProductImage", required = false) MultipartFile twoProductImage,
            @RequestPart(value = "threeProductImage", required = false) MultipartFile threeProductImage)
            throws IOException {
        System.out.println(">>> SAVING PRODUCT REQUEST: " + product);
        if (featuredImages != null)
            System.out.println(">>> FEATURED IMAGES: " + featuredImages.size() + " files");
        Product savedProduct = productService.addProduct(product, featuredImages, singleProductImage, twoProductImage,
                threeProductImage);
        System.out.println("<<< SAVED PRODUCT RESPONSE ID: " + savedProduct.getId());
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }

    @GetMapping("/categories/sample-products")
    public ResponseEntity<List<Product>> getProductsFromThreeCategories() {
        return ResponseEntity.ok(productService.getProductsFromThreeCategories());
    }

    @GetMapping("/random")
    public ResponseEntity<List<Product>> getRandomProducts() {
        return ResponseEntity.ok(productService.getAllProductsRandomly());
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<Product>> searchByCategory(@RequestParam String category) {
        return ResponseEntity.ok(productService.searchByCategory(category));
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Product>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchByName(name));
    }

    @GetMapping("/search/price")
    public ResponseEntity<List<Product>> searchByPrice(@RequestParam Double min, @RequestParam Double max) {
        return ResponseEntity.ok(productService.searchByPriceRange(min, max));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") Product product,
            @RequestPart(value = "featuredImages", required = false) List<MultipartFile> featuredImages,
            @RequestPart(value = "singleProductImage", required = false) MultipartFile singleProductImage,
            @RequestPart(value = "twoProductImage", required = false) MultipartFile twoProductImage,
            @RequestPart(value = "threeProductImage", required = false) MultipartFile threeProductImage)
            throws IOException {
        System.out.println(">>> UPDATING PRODUCT ID: " + id + " | REQUEST: " + product);
        Product updated = productService.updateProduct(id, product, featuredImages, singleProductImage,
                twoProductImage, threeProductImage);
        System.out.println("<<< UPDATED PRODUCT RESPONSE ID: " + updated.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/badge")
    public ResponseEntity<List<Product>> searchByBadge(@RequestParam String badge) {
        return ResponseEntity.ok(productService.searchByBadge(badge));
    }

    @GetMapping("/search/category-badge")
    public ResponseEntity<List<Product>> searchByCategoryBadge(
            @RequestParam String badge,
            @RequestParam String category) {
        return ResponseEntity.ok(productService.searchByCategoryBadge(badge, category));
    }
}
