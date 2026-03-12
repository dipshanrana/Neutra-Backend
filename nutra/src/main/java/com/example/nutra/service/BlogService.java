package com.example.nutra.service;

import com.example.nutra.Exception.ResourceNotFoundException;
import com.example.nutra.model.Blog;
import com.example.nutra.repository.BlogRepository;
import com.example.nutra.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    public Blog addBlog(Blog blog, MultipartFile image) throws IOException {
        if (blog.getCategory() != null) {
            com.example.nutra.model.Category cat = categoryRepository.findByNameIgnoreCase(blog.getCategory().getName());
            if (cat == null) {
                cat = categoryRepository.save(blog.getCategory());
            }
            blog.setCategory(cat);
        }
        if (image != null && !image.isEmpty()) {
            blog.setImage(fileStorageService.storeFile(image));
        }
        return blogRepository.save(blog);
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Blog getBlogById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with ID " + id));
    }

    public Blog updateBlog(Long id, Blog blogDetails, MultipartFile image) throws IOException {
        Blog blog = getBlogById(id);
        blog.setTitle(blogDetails.getTitle());
        blog.setContent(blogDetails.getContent());
        blog.setAuthor(blogDetails.getAuthor());
        blog.setRelatedProducts(blogDetails.getRelatedProducts());
        if (blogDetails.getCategory() != null) {
            com.example.nutra.model.Category cat = categoryRepository.findByNameIgnoreCase(blogDetails.getCategory().getName());
            if (cat == null) {
                cat = categoryRepository.save(blogDetails.getCategory());
            }
            blog.setCategory(cat);
        }

        if (image != null && !image.isEmpty()) {
            fileStorageService.deleteFile(blog.getImage());
            blog.setImage(fileStorageService.storeFile(image));
        }
        return blogRepository.save(blog);
    }

    public void deleteBlog(Long id) {
        Blog b = getBlogById(id);
        fileStorageService.deleteFile(b.getImage());
        blogRepository.deleteById(id);
    }

    public List<Blog> getBlogsByCategory(String categoryName) {
        return blogRepository.findByCategoryNameIgnoreCase(categoryName);
    }
}
