package com.example.nutra.repository;

import com.example.nutra.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("SELECT v.country, COUNT(v) FROM Visit v GROUP BY v.country")
    List<Object[]> countVisitsByCountry();
}
