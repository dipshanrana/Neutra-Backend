package com.example.nutra.repository;

import com.example.nutra.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByCategoryNameIgnoreCase(String categoryName);
}
