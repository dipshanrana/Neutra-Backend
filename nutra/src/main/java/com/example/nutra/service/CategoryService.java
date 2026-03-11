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

    public Category addCategory(Category category, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            category.setImage(imageFile.getBytes());
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

    public Category updateCategory(Long id, Category category, MultipartFile imageFile) throws IOException {
        Category existing = getCategoryById(id);
        existing.setName(category.getName());
        existing.setSvg(category.getSvg());
        existing.setBadge(category.getBadge());
        existing.setShortDescription(category.getShortDescription());
        if (imageFile != null && !imageFile.isEmpty()) {
            existing.setImage(imageFile.getBytes());
        }
        return categoryRepository.save(existing);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
