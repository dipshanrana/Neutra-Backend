package com.example.nutra.repository;

import com.example.nutra.model.AnalyticsVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<AnalyticsVisit, Long> {

    @Query("SELECT v.country, COUNT(v) FROM AnalyticsVisit v GROUP BY v.country")
    List<Object[]> countVisitsByCountry();

}
