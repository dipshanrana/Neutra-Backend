package com.example.nutra.controller;

import com.example.nutra.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping("/record")
    public ResponseEntity<Map<String, String>> recordVisit(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }

        analyticsService.recordVisit(ipAddress);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Visit recorded");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        long totalVisits = analyticsService.getTotalVisits();
        List<Object[]> countryStats = analyticsService.getVisitsByCountry();

        Map<String, Object> response = new HashMap<>();
        response.put("totalVisits", totalVisits);

        List<Map<String, Object>> byCountry = new java.util.ArrayList<>();
        for (Object[] row : countryStats) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("country", row[0]);
            stat.put("clicks", row[1]);
            byCountry.add(stat);
        }
        response.put("byCountry", byCountry);

        return ResponseEntity.ok(response);
    }
}
