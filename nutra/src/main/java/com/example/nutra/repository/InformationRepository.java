package com.example.nutra.repository;

import com.example.nutra.model.Information;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InformationRepository extends JpaRepository<Information, Long> {
    List<Information> findByCategoryNameIgnoreCase(String categoryName);
}
