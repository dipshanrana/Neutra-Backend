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

    public void recordVisit(String ipAddress, String username, String reason) {
        String country = "Unknown";
        String actualIp = ipAddress;

        try {
            String url;
            if (ipAddress != null && !ipAddress.equals("0:0:0:0:0:0:0:1") && !ipAddress.equals("127.0.0.1")
                    && !ipAddress.isEmpty()) {
                url = "http://ip-api.com/json/" + ipAddress;
            } else {
                // If localhost, get the info for the current public IP of the server/local
                // machine
                url = "http://ip-api.com/json/";
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && "success".equals(response.get("status"))) {
                country = (String) response.get("country");
                if (response.containsKey("query")) {
                    actualIp = (String) response.get("query");
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to get country for IP: " + ipAddress);
        }

        AnalyticsVisit visit = AnalyticsVisit.builder()
                .ipAddress(actualIp) // Store the actual public IP if we fetched it
                .country(country)
                .username(username)
                .reason(reason)
                .visitedAt(LocalDateTime.now())
                .build();
        visitRepository.save(visit);
    }

    public List<AnalyticsVisit> getAllVisits() {
        return visitRepository.findAll();
    }

    public long getTotalVisits() {
        return visitRepository.count();
    }

    public List<Object[]> getVisitsByCountry() {
        return visitRepository.countVisitsByCountry();
    }
}
