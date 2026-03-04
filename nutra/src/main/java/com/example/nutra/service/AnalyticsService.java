package com.example.nutra.service;

import com.example.nutra.model.AnalyticsVisit;
import com.example.nutra.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final VisitRepository visitRepository;
    private final RestTemplate restTemplate;

    public void recordVisit(String ipAddress) {
        String country = "Unknown";

        try {
            if (ipAddress != null && !ipAddress.equals("0:0:0:0:0:0:0:1") && !ipAddress.equals("127.0.0.1")) {
                String url = "http://ip-api.com/json/" + ipAddress;
                @SuppressWarnings("unchecked")
                Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                if (response != null && "success".equals(response.get("status"))) {
                    country = (String) response.get("country");
                }
            } else {
                country = "Localhost";
            }
        } catch (Exception e) {
            System.err.println("Failed to get country for IP: " + ipAddress);
        }

        AnalyticsVisit visit = AnalyticsVisit.builder()
                .ipAddress(ipAddress)
                .country(country)
                .visitedAt(LocalDateTime.now())
                .build();
        visitRepository.save(visit);
    }

    public long getTotalVisits() {
        return visitRepository.count();
    }

    public List<Object[]> getVisitsByCountry() {
        return visitRepository.countVisitsByCountry();
    }
}
