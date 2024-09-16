package com.indium.spring_boot_assignment.service;

import com.indium.spring_boot_assignment.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WicketService {

    @Autowired
    DeliveryRepository deliveryRepository;

    @Cacheable(value = "wicketCache", key = "#bowlerName")
    public String getWicketsSummaryByBowler(String bowlerName) {
        List<Object[]> results = deliveryRepository.findWicketsSummaryByBowlerName(bowlerName);

        if (results != null && !results.isEmpty()) {
            StringBuilder response = new StringBuilder("Wickets summary for bowler " + bowlerName + ":\n");
            for (Object[] result : results) {
                String name = (String) result[0];
                Long count = ((Number) result[1]).longValue();
                String players = (String) result[2];
                response.append("Bowler Name: ").append(name)
                        .append(", Wickets Taken: ").append(count)
                        .append(", Players Out: ").append(players)
                        .append("\n");
            }
            return response.toString();
        } else {
            return "No wickets found for bowler: " + bowlerName;
        }
    }
    @CacheEvict(value = {"wicketCache"}, allEntries = true)
    public void clearCache() {

    }
}

