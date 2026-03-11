package com.example.nutra.controller;

import com.example.nutra.model.Category;
import com.example.nutra.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Category> addCategory(
            @RequestPart("category") Category category,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        System.out.println(">>> SAVING CATEGORY REQUEST: " + category);
        if (image != null)
            System.out
                    .println(">>> IMAGE RECEIVED: " + image.getOriginalFilename() + " (" + image.getSize() + " bytes)");
        Category saved = categoryService.addCategory(category, image);
        System.out.println("<<< SAVED CATEGORY RESPONSE: " + saved);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestPart("category") Category category,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        System.out.println(">>> UPDATING CATEGORY ID: " + id + " | REQUEST: " + category);
        if (image != null)
            System.out
                    .println(">>> IMAGE RECEIVED: " + image.getOriginalFilename() + " (" + image.getSize() + " bytes)");
        Category updated = categoryService.updateCategory(id, category, image);
        System.out.println("<<< UPDATED CATEGORY RESPONSE: " + updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
