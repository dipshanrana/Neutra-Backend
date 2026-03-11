package com.example.nutra.service;

import com.example.nutra.model.Category;
import com.example.nutra.repository.CategoryRepository;
import com.example.nutra.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    public Category addCategory(Category category, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            category.setImage(fileStorageService.storeFile(image));
        }
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    public Category updateCategory(Long id, Category categoryDetails, MultipartFile image) throws IOException {
        Category category = getCategoryById(id);
        category.setName(categoryDetails.getName());
        category.setSvg(categoryDetails.getSvg());
        category.setBadge(categoryDetails.getBadge());
        category.setShortDescription(categoryDetails.getShortDescription());

        if (image != null && !image.isEmpty()) {
            // Delete old image if it exists
            if (category.getImage() != null && !category.getImage().isEmpty()) {
                fileStorageService.deleteFile(category.getImage());
            }
            category.setImage(fileStorageService.storeFile(image));
        }
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        // Delete associated image file
        if (category.getImage() != null && !category.getImage().isEmpty()) {
            fileStorageService.deleteFile(category.getImage());
        }
        categoryRepository.deleteById(id);
    }
}
