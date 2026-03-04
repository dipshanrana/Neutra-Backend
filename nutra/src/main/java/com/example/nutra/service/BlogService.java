package com.example.nutra.service;

import com.example.nutra.Exception.ResourceNotFoundException;
import com.example.nutra.model.Blog;
import com.example.nutra.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public Blog addBlog(Blog blog, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            blog.setImage(image.getBytes());
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

    public Blog updateBlog(Long id, Blog updatedBlog, MultipartFile image) throws IOException {
        Blog existingBlog = getBlogById(id);
        existingBlog.setTitle(updatedBlog.getTitle());
        existingBlog.setContent(updatedBlog.getContent());
        existingBlog.setAuthor(updatedBlog.getAuthor());
        existingBlog.setRelatedProducts(updatedBlog.getRelatedProducts());
        if (image != null && !image.isEmpty()) {
            existingBlog.setImage(image.getBytes());
        }
        return blogRepository.save(existingBlog);
    }

    public void deleteBlog(Long id) {
        Blog blog = getBlogById(id);
        blogRepository.delete(blog);
    }
}
